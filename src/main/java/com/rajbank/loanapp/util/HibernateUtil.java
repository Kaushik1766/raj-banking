package com.rajbank.loanapp.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Bootstraps a single application-wide Hibernate {@link SessionFactory}.
 * Sessions are opened per-request/per-operation via {@link #getSessionFactory()}
 * and transactions are managed explicitly by the DAO layer.
 */
public final class HibernateUtil {

    private static final Logger LOGGER = LogManager.getLogger(HibernateUtil.class);

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (RuntimeException ex) {
            LOGGER.error("Hibernate SessionFactory creation failed", ex);
            throw ex;
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        if (!SESSION_FACTORY.isClosed()) {
            SESSION_FACTORY.close();
        }
    }
}
