package com.rajbank.loanapp.dao;

import com.rajbank.loanapp.model.RememberMeToken;

import java.util.Optional;

public interface RememberMeTokenDao {

    Optional<RememberMeToken> findBySelector(String selector);

    RememberMeToken save(RememberMeToken token);

    void delete(RememberMeToken token);

    void deleteByUsername(String username);
}
