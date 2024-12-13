package com.bms.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.bms.businesslogic.ATMOperations;
import com.bms.entity.Transaction;
import com.bms.entity.User;
import com.bms.service.UserService;

public class DashBoardUI {
	
	
    private JFrame frame;
    private User loggedInUser;
    private UserService userService;

    public DashBoardUI(User user) {
        this.loggedInUser = user;
        this.userService = new UserService();

        frame = new JFrame("Account Operations");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 1));  // Increase rows for one more button

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getName(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(e -> deposit());

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(e -> withdraw());

        JButton checkBalanceButton = new JButton("Check Balance");
        checkBalanceButton.addActionListener(e -> checkBalance());

        JButton miniStatementButton = new JButton("Mini Statement");
        miniStatementButton.addActionListener(e -> showMiniStatement());

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());

        frame.add(welcomeLabel);
        frame.add(depositButton);
        frame.add(withdrawButton);
        frame.add(checkBalanceButton);
        frame.add(miniStatementButton);  // Add mini statement button
        frame.add(logoutButton);

        frame.setVisible(true);
    }

    private void deposit() {
        ATMOperations atmOperations = new ATMOperations();
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                
                if (amount > 0) {
                	atmOperations.deposit(loggedInUser, amount);
                	 JOptionPane.showMessageDialog(frame, "Successfully deposited $" + amount);
                }
                else
                	JOptionPane.showMessageDialog(frame, "Enter Amount greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                   
               
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception e) {
            	System.out.println("Error occured due to mini");           }
        }
    }

    private void withdraw() {
        double balance = userService.checkBalance(loggedInUser);  // Get the current balance
        ATMOperations atmOperations = new ATMOperations();

        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);

                // Check if the amount is greater than the balance
                if (amount > balance) {
                    JOptionPane.showMessageDialog(frame, "Insufficient funds! You cannot withdraw more than your balance.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (amount <= 0) {
                    // Optionally, handle case where amount is less than or equal to 0
                    JOptionPane.showMessageDialog(frame, "Withdrawal amount must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Proceed with the withdrawal
                    atmOperations.withdraw(loggedInUser, amount);
                    JOptionPane.showMessageDialog(frame, "Successfully withdrew $" + amount);
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }  catch(Exception e) {
            	System.out.println("Error occured due to mini");           
            }
        }
    } 
        
    

    private void checkBalance() {
        double balance = userService.checkBalance(loggedInUser);
        JOptionPane.showMessageDialog(frame, "Your current balance is $" + balance);
    }

    // New method to show mini statement
    private void showMiniStatement() {
        // Fetch the latest user details from the database
        User refreshedUser = userService.getUserByCardNumber(loggedInUser.getCardNumber());
        loggedInUser = refreshedUser; // Update the logged-in user to reflect the latest data

        if (refreshedUser.getTransactions().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No transactions available.", "Mini Statement", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFrame miniStatementFrame = new JFrame("Mini Statement");
        miniStatementFrame.setSize(400, 500);
        miniStatementFrame.setLayout(new GridLayout(0, 1));

        // Bank name and current date/time
        JLabel bankNameLabel = new JLabel("SBI Bank", SwingConstants.CENTER);
        bankNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel dateLabel = new JLabel("Date: " + java.time.LocalDate.now() + " Time: " + java.time.LocalTime.now(), SwingConstants.CENTER);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // User details
        JLabel userNameLabel = new JLabel("Name: " + refreshedUser.getName(), SwingConstants.LEFT);
        JLabel accountNumberLabel = new JLabel("Account No: " + refreshedUser.getCardNumber(), SwingConstants.LEFT);

        miniStatementFrame.add(bankNameLabel);
        miniStatementFrame.add(dateLabel);
        miniStatementFrame.add(userNameLabel);
        miniStatementFrame.add(accountNumberLabel);

        // Transaction details
        for (Transaction transaction : refreshedUser.getTransactions()) {
            JLabel transactionLabel = new JLabel();
            String transactionText = transaction.getType() + ": $" + transaction.getAmount() + " on " + transaction.getTimestamp();

            transactionLabel.setText(transactionText);
            transactionLabel.setHorizontalAlignment(SwingConstants.LEFT);

            if ("DEPOSIT".equalsIgnoreCase(transaction.getType())) {
                transactionLabel.setForeground(Color.GREEN); // Deposit in green
            } else if ("WITHDRAWL".equalsIgnoreCase(transaction.getType())) {
                transactionLabel.setForeground(Color.RED); // Withdrawal in red
            }

            miniStatementFrame.add(transactionLabel);
        }

        // Display total balance
        JLabel balanceLabel = new JLabel("Total Balance: $" + userService.checkBalance(refreshedUser), SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        miniStatementFrame.add(balanceLabel);

        miniStatementFrame.setVisible(true);
    }



    private void logout() {
        frame.dispose();
        new MainMenuUI(); // Redirect to the main screen
    }
}
