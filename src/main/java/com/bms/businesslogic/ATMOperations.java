package com.bms.businesslogic;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import com.bms.entity.Transaction;
import com.bms.entity.User;
import com.bms.service.UserService;
import com.bms.util.JPATrans;

public class ATMOperations {
	
	private UserService userService = new UserService();

	


    // Deposit money into the user's account
    public void deposit(User user, double amount) {
    	try {
    		// Update balance
            user.setBalance(user.getBalance() + amount);
            
            userService.deposit(user, amount);
            
            userService.transaction(user, "DEPOSIT", amount, LocalDateTime.now());

            System.out.println("Deposit successful! Current Balance: $" + user.getBalance());
    	}
    	catch(Exception e) {
    		System.out.println("deposit handled in Atm operations");
    	}
        
        
    }

    // Withdraw money from the user's account
    public void withdraw(User user, double amount) {
    	
    	try {
    		if (amount <= 0) {
                throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
            }
            if (user.getBalance() < amount) {
                throw new IllegalArgumentException("Insufficient funds.");
            }
            
            
            userService.withdraw(user, amount);

            userService.transaction(user, "WITHDRAWl", amount, LocalDateTime.now());

            System.out.println("Withdrawal successful! Current Balance: $" + user.getBalance());
    	}
    	catch(Exception e) {
    		System.out.println("Withdraw handled");
    	}
        
    }
}
