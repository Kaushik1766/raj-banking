package com.rajbank.loanapp.dao.impl;

import com.rajbank.loanapp.dao.CustomerDao;
import com.rajbank.loanapp.model.Customer;
import com.rajbank.loanapp.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;

public class CustomerDaoImpl implements CustomerDao {

    private static final Logger LOGGER = LogManager.getLogger(CustomerDaoImpl.class);

    @Override
    public Optional<Customer> findById(String customerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Customer.class, customerId));
        } catch (RuntimeException ex) {
            LOGGER.error("Error finding customer by id [{}]", customerId, ex);
            throw ex;
        }
    }

    @Override
    public Customer save(Customer customer) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(customer);
            tx.commit();
            return customer;
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("Error saving customer [{}]", customer.getCustomerId(), ex);
            throw ex;
        }
    }

    @Override
    public Customer update(Customer customer) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Customer merged = session.merge(customer);
            tx.commit();
            return merged;
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("Error updating customer [{}]", customer.getCustomerId(), ex);
            throw ex;
        }
    }

    @Override
    public boolean existsByPanNumber(String panNumber) {
        return exists("pan_number", panNumber);
    }

    @Override
    public boolean existsByAadharNumber(String aadharNumber) {
        return exists("aadhar_number", aadharNumber);
    }

    private boolean exists(String column, String value) {
        String hql = "aadhar_number".equals(column)
                ? "select count(c) from Customer c where c.aadharNumber = :value"
                : "select count(c) from Customer c where c.panNumber = :value";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("value", value);
            return query.getSingleResult() > 0;
        } catch (RuntimeException ex) {
            LOGGER.error("Error checking existence for column [{}]", column, ex);
            throw ex;
        }
    }
}
