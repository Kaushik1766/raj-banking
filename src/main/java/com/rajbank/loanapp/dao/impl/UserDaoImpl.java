package com.rajbank.loanapp.dao.impl;

import com.rajbank.loanapp.dao.UserDao;
import com.rajbank.loanapp.model.User;
import com.rajbank.loanapp.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private static final Logger LOGGER = LogManager.getLogger(UserDaoImpl.class);

    @Override
    public Optional<User> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "from User u where lower(u.username) = lower(:username)", User.class);
            query.setParameter("username", username);
            return query.uniqueResultOptional();
        } catch (RuntimeException ex) {
            LOGGER.error("Error finding user by username [{}]", username, ex);
            throw ex;
        }
    }

    @Override
    public User save(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            return user;
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("Error saving user [{}]", user.getUsername(), ex);
            throw ex;
        }
    }

    @Override
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select count(u) from User u", Long.class).getSingleResult();
        } catch (RuntimeException ex) {
            LOGGER.error("Error counting users", ex);
            throw ex;
        }
    }
}
