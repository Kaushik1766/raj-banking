package com.rajbank.loanapp.service;

import com.rajbank.loanapp.dao.RememberMeTokenDao;
import com.rajbank.loanapp.dao.UserDao;
import com.rajbank.loanapp.dao.impl.RememberMeTokenDaoImpl;
import com.rajbank.loanapp.dao.impl.UserDaoImpl;
import com.rajbank.loanapp.model.RememberMeToken;
import com.rajbank.loanapp.model.User;
import com.rajbank.loanapp.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.rajbank.loanapp.util.Constants.REMEMBER_ME_MAX_AGE_SECONDS;

/**
 * Handles credential verification and the "Keep me signed in" remember-me
 * token lifecycle. The remember-me cookie only ever carries an opaque
 * selector/verifier pair (never the account password); the verifier is
 * hashed before being persisted, following the standard selector/token
 * pattern to keep the cookie safe to store client-side.
 */
public class AuthService {

    private static final Logger LOGGER = LogManager.getLogger(AuthService.class);

    private final UserDao userDao;
    private final RememberMeTokenDao rememberMeTokenDao;

    public AuthService() {
        this(new UserDaoImpl(), new RememberMeTokenDaoImpl());
    }

    public AuthService(UserDao userDao, RememberMeTokenDao rememberMeTokenDao) {
        this.userDao = userDao;
        this.rememberMeTokenDao = rememberMeTokenDao;
    }

    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        Optional<User> user = userDao.findByUsername(username.trim());
        if (user.isPresent() && user.get().isActive() && PasswordUtil.matches(password, user.get().getPasswordHash())) {
            LOGGER.info("User [{}] authenticated successfully", username);
            return user;
        }
        LOGGER.warn("Authentication failed for username [{}]", username);
        return Optional.empty();
    }

    /** Creates and persists a new remember-me token, returning the raw cookie value "selector:verifier". */
    public String createRememberMeToken(User user) {
        String selector = PasswordUtil.generateRandomToken();
        String verifier = PasswordUtil.generateRandomToken();

        RememberMeToken token = new RememberMeToken();
        token.setUsername(user.getUsername());
        token.setSelector(selector);
        token.setTokenHash(PasswordUtil.hash(verifier));
        token.setExpiryDate(LocalDateTime.now().plusSeconds(REMEMBER_ME_MAX_AGE_SECONDS));
        rememberMeTokenDao.save(token);

        return selector + ":" + verifier;
    }

    /** Resolves a remember-me cookie value back to the authenticated user, if the token is still valid. */
    public Optional<User> resolveRememberMe(String cookieValue) {
        if (cookieValue == null || !cookieValue.contains(":")) {
            return Optional.empty();
        }
        String[] parts = cookieValue.split(":", 2);
        String selector = parts[0];
        String verifier = parts[1];

        Optional<RememberMeToken> tokenOpt = rememberMeTokenDao.findBySelector(selector);
        if (tokenOpt.isEmpty()) {
            return Optional.empty();
        }
        RememberMeToken token = tokenOpt.get();
        if (token.getExpiryDate().isBefore(LocalDateTime.now()) || !PasswordUtil.matches(verifier, token.getTokenHash())) {
            rememberMeTokenDao.delete(token);
            return Optional.empty();
        }
        return userDao.findByUsername(token.getUsername());
    }

    public void invalidateRememberMe(String username) {
        rememberMeTokenDao.deleteByUsername(username);
    }
}
