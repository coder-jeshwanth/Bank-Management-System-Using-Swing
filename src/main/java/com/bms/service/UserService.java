package com.bms.service;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import com.bms.dao.UserDAO;
import com.bms.entity.Transaction;
import com.bms.entity.User;
import com.bms.util.JPAUtil;

public class UserService {
    private UserDAO userDAO = new UserDAO();
    private EntityManager entityManager;
    
    
    public UserService() {
        entityManager = JPAUtil.getEntityManager();
    }

    // Create a new user (auto-generates account number, card number, and CVV)
    public User registerUser(String name, int age, String phoneNumber, String email, String pin) {
        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setAtmPin(pin);
        user.setBalance(0.0); // Initial balance is zero
        userDAO.saveUser(user); // Save the user in the database
        return user;
    }

    // Authenticate a user by phone number and PIN
    public User authenticateByPhoneNumber(String phoneNumber, String pin) {
        User user = userDAO.getUserByPhoneNumber(phoneNumber);
        if (user != null && user.getAtmPin().equals(pin)) {
            return user; // Authentication successful
        }
        return null; // Authentication failed
    }

    // Authenticate a user by card number and PIN
    public User authenticateByCardNumber(String cardNumber, String pin) {
        User user = userDAO.getUserByCardNumber(cardNumber);
        if (user != null && user.getAtmPin().equals(pin)) {
            return user; // Authentication successful
        }
        return null; // Authentication failed
    }

    // Get user details by card number
    public User getUserByCardNumber(String cardNumber) {
        return userDAO.getUserByCardNumber(cardNumber); // Fetch user from database
    }

    // Get user details by phone number
    public User getUserByPhoneNumber(String phoneNumber) {
        return userDAO.getUserByPhoneNumber(phoneNumber); // Fetch user from database
    }

    // Check the user's balance
    public double checkBalance(User user) {
        return user.getBalance(); // Return the current balance
    }

    public void deposit(User user, double amount) {
    	
    	try {
    		
    		
            
         // Begin transaction
            entityManager.getTransaction().begin();   

            // Merge the updated user to persist the changes in the DB
            entityManager.merge(user);

            // Commit transaction
            entityManager.getTransaction().commit();
            
            
    	}
    	catch(Exception e) {
    		System.out.println("User service  handled");
    	}
        
          
            
        
    }


    public void withdraw(User user, double amount) {
    	try {
    		if (amount > 0 && amount <= user.getBalance()) {
                

                // Update balance
                user.setBalance(user.getBalance() - amount);
                
             // Begin transaction
                entityManager.getTransaction().begin();

                // Merge the updated user to persist the changes in the DB
                entityManager.merge(user);
                           
                // Commit transaction
                entityManager.getTransaction().commit();
               
                
            } else {
                throw new IllegalArgumentException("Insufficient balance or invalid amount.");
            }
    	}catch(Exception e) {
			System.out.println("Withdw handled");
		}
        
    }
    
    public void transaction(User user, String type, double amt,LocalDateTime timestamp) {
    	
    	Transaction transaction = new Transaction(user, type, amt, timestamp);
    	user.addTransaction(transaction);  // Add transaction to user
    	
    	// Begin transaction
        entityManager.getTransaction().begin(); 
    	         
       
        entityManager.merge(transaction);
        
        // Commit transaction
        entityManager.getTransaction().commit();
        
        System.out.println("BEfore touching mini");
        
        System.out.println(transaction);
    }
    


}
