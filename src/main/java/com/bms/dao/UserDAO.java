package com.bms.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bms.entity.User;
import com.bms.util.JPAUtil;

public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private EntityManager entityManager;

    public UserDAO() {
        entityManager = JPAUtil.getEntityManager(); // Initialize EntityManager
    }

    public void saveUser(User user) {
        EntityTransaction transactionScope = entityManager.getTransaction();
        try {
            transactionScope.begin(); // Begin transaction
            
            entityManager.persist(user); // Persist user object
            entityManager.flush(); // Ensure changes are written immediately
            transactionScope.commit(); // Commit changes to DB
            
            entityManager.close();
        } catch (Exception e) {
            if (transactionScope.isActive()) {
                transactionScope.rollback(); // Rollback on failure
            }
            logger.error("Failed to save user", e);
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber", User.class);
            query.setParameter("phoneNumber", phoneNumber);
            return query.getResultStream().findFirst().orElse(null); // Fetch user by phone number
        } catch (Exception e) {
            logger.error("Failed to fetch user by phone number", e);
            throw new RuntimeException("Failed to fetch user by phone number: " + e.getMessage(), e);
        }
    }

    public User getUserByCardNumber(String cardNumber) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.cardNumber = :cardNumber", User.class);
            query.setParameter("cardNumber", cardNumber);
            return query.getResultStream().findFirst().orElse(null); // Fetch user by card number
        } catch (Exception e) {
            logger.error("Failed to fetch user by card number", e);
            throw new RuntimeException("Failed to fetch user by card number: " + e.getMessage(), e);
        }
    }

    public void updateUser(User user) {
        EntityTransaction transactionScope = entityManager.getTransaction();
        try {
            transactionScope.begin(); // Begin transaction
            entityManager.merge(user); // Merge updates to user
            entityManager.flush(); // Ensure changes are written immediately
            transactionScope.commit(); // Commit changes to DB
            entityManager.close();
        } catch (Exception e) {
            if (transactionScope.isActive()) {
                transactionScope.rollback(); // Rollback on failure
            }
            logger.error("Failed to update user", e);
            throw new RuntimeException("Failed to update user: " + e.getMessage(), e);
        }
    }

    public void close() {
        if (entityManager != null) {
            entityManager.close(); // Close the entity manager to release resources
        }
    }
}
