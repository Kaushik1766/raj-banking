package com.rajbank.loanapp.dao.impl;

import com.rajbank.loanapp.dao.LoanApplicationDao;
import com.rajbank.loanapp.model.ApplicationStatus;
import com.rajbank.loanapp.model.LoanApplication;
import com.rajbank.loanapp.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class LoanApplicationDaoImpl implements LoanApplicationDao {

    private static final Logger LOGGER = LogManager.getLogger(LoanApplicationDaoImpl.class);

    @Override
    public Optional<LoanApplication> findByApplicationNumber(String applicationNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(LoanApplication.class, applicationNumber));
        } catch (RuntimeException ex) {
            LOGGER.error("Error finding loan application [{}]", applicationNumber, ex);
            throw ex;
        }
    }

    @Override
    public LoanApplication save(LoanApplication application) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(application);
            tx.commit();
            return application;
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("Error saving loan application", ex);
            throw ex;
        }
    }

    @Override
    public LoanApplication update(LoanApplication application) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            LoanApplication merged = session.merge(application);
            tx.commit();
            return merged;
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("Error updating loan application [{}]", application.getApplicationNumber(), ex);
            throw ex;
        }
    }

    @Override
    public void delete(LoanApplication application) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            LoanApplication managed = session.get(LoanApplication.class, application.getApplicationNumber());
            if (managed != null) {
                session.remove(managed);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("Error deleting loan application [{}]", application.getApplicationNumber(), ex);
            throw ex;
        }
    }

    @Override
    public List<LoanApplication> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from LoanApplication a order by a.createdDate desc", LoanApplication.class).list();
        } catch (RuntimeException ex) {
            LOGGER.error("Error listing loan applications", ex);
            throw ex;
        }
    }

    @Override
    public List<LoanApplication> findByCreatedBy(String createdBy) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<LoanApplication> query = session.createQuery(
                    "from LoanApplication a where a.createdBy = :createdBy order by a.createdDate desc",
                    LoanApplication.class);
            query.setParameter("createdBy", createdBy);
            return query.list();
        } catch (RuntimeException ex) {
            LOGGER.error("Error listing loan applications for maker [{}]", createdBy, ex);
            throw ex;
        }
    }

    @Override
    public List<LoanApplication> findByStatus(ApplicationStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<LoanApplication> query = session.createQuery(
                    "from LoanApplication a where a.status = :status order by a.createdDate asc",
                    LoanApplication.class);
            query.setParameter("status", status);
            return query.list();
        } catch (RuntimeException ex) {
            LOGGER.error("Error listing loan applications with status [{}]", status, ex);
            throw ex;
        }
    }
}
