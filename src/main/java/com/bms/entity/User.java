package com.bms.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age;

    @Column(unique = true)
    private String phoneNumber;
    private String email;
    private String atmPin;

    @Column(unique = true)
    private String accountNumber;

    @Column(unique = true)
    private String cardNumber;
    private String cvv;
    private double balance;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    // Getters, setters, and helper methods
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setUser(this);
    }

//    public void removeTransaction(Transaction transaction) {
//        transactions.remove(transaction);
//        transaction.setUser(null);
//    }

    // Other getters and setters

    @PrePersist
    private void generateAccountDetails() {
        accountNumber = String.format("%012d", System.currentTimeMillis() + new Random().nextInt(1000));
        cardNumber = String.format("%016d", System.currentTimeMillis() + new Random().nextInt(1000));
        cvv = String.format("%03d", new Random().nextInt(1000));
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAtmPin() {
		return atmPin;
	}

	public void setAtmPin(String atmPin) {
		this.atmPin = atmPin;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
    
    
}
