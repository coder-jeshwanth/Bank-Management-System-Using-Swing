package com.bms.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPATrans {
	private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = 
	        Persistence.createEntityManagerFactory("abc");

	    public static EntityManager getEntityManager() {
	        return ENTITY_MANAGER_FACTORY.createEntityManager();
	    }

	    public static void close() {
	        if (ENTITY_MANAGER_FACTORY != null) {
	            ENTITY_MANAGER_FACTORY.close();
	        }
	    }
	    
	    public static void commitTransaction(EntityManager entityManager) {
	        if (entityManager != null) {
	            EntityTransaction transaction = entityManager.getTransaction();
	            if (transaction != null && transaction.isActive()) {
	                try {
	                    transaction.commit();
	                } catch (Exception e) {
	                    if (transaction.isActive()) {
	                        transaction.rollback();
	                    }
	                    throw new RuntimeException("Transaction commit failed: " + e.getMessage(), e);
	                }
	            }
	        }
	    }
}
