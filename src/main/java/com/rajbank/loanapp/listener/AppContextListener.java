package com.rajbank.loanapp.listener;

import com.rajbank.loanapp.dao.UserDao;
import com.rajbank.loanapp.dao.impl.UserDaoImpl;
import com.rajbank.loanapp.model.Role;
import com.rajbank.loanapp.model.User;
import com.rajbank.loanapp.util.HibernateUtil;
import com.rajbank.loanapp.util.PasswordUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Bootstraps the Hibernate SessionFactory on startup and seeds one default
 * Maker and one default Checker account so the application is usable out of
 * the box. Releases Hibernate resources on shutdown.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = LogManager.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HibernateUtil.getSessionFactory();
        seedDefaultUsers();
        LOGGER.info("Bank Loan Application started");
    }

    private void seedDefaultUsers() {
        UserDao userDao = new UserDaoImpl();
        if (userDao.count() > 0) {
            return;
        }
        User maker = new User();
        maker.setUsername("maker1");
        maker.setPasswordHash(PasswordUtil.hash("Maker@123"));
        maker.setRole(Role.MAKER);
        maker.setFullName("Default Maker");
        userDao.save(maker);

        User checker = new User();
        checker.setUsername("checker1");
        checker.setPasswordHash(PasswordUtil.hash("Checker@123"));
        checker.setRole(Role.CHECKER);
        checker.setFullName("Default Checker");
        userDao.save(checker);

        LOGGER.info("Seeded default Maker [maker1] and Checker [checker1] accounts");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.shutdown();
        LOGGER.info("Bank Loan Application shut down");
    }
}
