package com.rajbank.loanapp.dao.impl;

import com.rajbank.loanapp.dao.SequenceDao;
import com.rajbank.loanapp.model.SequenceCounter;
import com.rajbank.loanapp.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SequenceDaoImpl implements SequenceDao {

    private static final Logger LOGGER = LogManager.getLogger(SequenceDaoImpl.class);

    @Override
    public long nextValue(String sequenceName) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            SequenceCounter counter = session.get(SequenceCounter.class, sequenceName, LockMode.PESSIMISTIC_WRITE);
            if (counter == null) {
                counter = new SequenceCounter(sequenceName, 1L);
                session.persist(counter);
            } else {
                counter.setNextValue(counter.getNextValue() + 1);
            }
            long value = counter.getNextValue();
            tx.commit();
            return value;
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("Error generating next value for sequence [{}]", sequenceName, ex);
            throw ex;
        }
    }
}
