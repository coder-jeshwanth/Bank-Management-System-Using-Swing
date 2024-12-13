package com.bms.businesslogic;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import com.bms.entity.Transaction;
import com.bms.entity.User;
import com.bms.service.UserService;
import com.bms.util.JPATrans;

public class ATMOperations {
	
	private UserService userService = new UserService();
	private EntityManager entityManager;
	
	public ATMOperations(){
		 entityManager = JPATrans.getEntityManager();	
	}

    // Deposit money into the user's account
    public void deposit(User user, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        
        userService.deposit(user, amount);

        // Log the transaction
        Transaction transaction = new Transaction(user, "DEPOSIT", amount, LocalDateTime.now());
        
        
        user.addTransaction(transaction);  // Add transaction to user
       
       
        System.out.println("Deposit successful! Current Balance: $" + user.getBalance());
    }

    // Withdraw money from the user's account
    public void withdraw(User user, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if (user.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        
        
        userService.withdraw(user, amount);

        // Log the transaction
        Transaction transaction = new Transaction(user, "WITHDRAWL", amount, LocalDateTime.now());
        
        user.addTransaction(transaction);  // Add transaction to user

        System.out.println("Withdrawal successful! Current Balance: $" + user.getBalance());
    }
}
