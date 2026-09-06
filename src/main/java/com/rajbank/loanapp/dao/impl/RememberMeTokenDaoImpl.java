package com.rajbank.loanapp.dao.impl;

import com.rajbank.loanapp.dao.RememberMeTokenDao;
import com.rajbank.loanapp.model.RememberMeToken;
import com.rajbank.loanapp.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;

public class RememberMeTokenDaoImpl implements RememberMeTokenDao {

    private static final Logger LOGGER = LogManager.getLogger(RememberMeTokenDaoImpl.class);

    @Override
    public Optional<RememberMeToken> findBySelector(String selector) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<RememberMeToken> query = session.createQuery(
                    "from RememberMeToken t where t.selector = :selector", RememberMeToken.class);
            query.setParameter("selector", selector);
            return query.uniqueResultOptional();
        } catch (RuntimeException ex) {
            LOGGER.error("Error finding remember-me token by selector", ex);
            throw ex;
        }
    }

    @Override
    public RememberMeToken save(RememberMeToken token) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(token);
            tx.commit();
            return token;
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("Error saving remember-me token for user [{}]", token.getUsername(), ex);
            throw ex;
        }
    }

    @Override
    public void delete(RememberMeToken token) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            RememberMeToken managed = session.get(RememberMeToken.class, token.getId());
            if (managed != null) {
                session.remove(managed);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("Error deleting remember-me token", ex);
            throw ex;
        }
    }

    @Override
    public void deleteByUsername(String username) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.createMutationQuery("delete from RememberMeToken t where t.username = :username")
                    .setParameter("username", username)
                    .executeUpdate();
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("Error deleting remember-me tokens for user [{}]", username, ex);
            throw ex;
        }
    }
}
