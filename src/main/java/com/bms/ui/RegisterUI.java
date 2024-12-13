package com.bms.ui;

import java.awt.GridLayout;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.PersistenceException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.bms.entity.User;
import com.bms.service.UserService;

public class RegisterUI {
    private JFrame frame;
    private JTextField nameField, ageField, phoneField, emailField;
    private JPasswordField pinField, confirmPinField;
    private UserService userService = new UserService();

    public RegisterUI() {
        frame = new JFrame("Register");
        frame.setLayout(new GridLayout(8, 2)); // Increased row count for the new confirmation field
        frame.setSize(400, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new JLabel("Name:"));
        nameField = new JTextField();
        frame.add(nameField);

        frame.add(new JLabel("Age:"));
        ageField = new JTextField();
        frame.add(ageField);

        frame.add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        frame.add(phoneField);

        frame.add(new JLabel("Email:"));
        emailField = new JTextField();
        frame.add(emailField);

        frame.add(new JLabel("ATM Pin:"));
        pinField = new JPasswordField();
        frame.add(pinField);

        frame.add(new JLabel("Confirm ATM Pin:"));
        confirmPinField = new JPasswordField();
        frame.add(confirmPinField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> register());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose();
            new MainMenuUI();
        });

        frame.add(registerButton);
        frame.add(backButton);

        frame.setVisible(true);
    }

    private void register() {
        try {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String phoneNumber = phoneField.getText();
            String email = emailField.getText();
            String pin = new String(pinField.getPassword());
            String confirmPin = new String(confirmPinField.getPassword());

            // Validate age
            if (age < 18) {
                throw new IllegalArgumentException("Age should be greater than 18.");
            }

            // Validate phone number
            if (!phoneNumber.matches("\\d{10}")) {
                throw new IllegalArgumentException("Phone number should be exactly 10 digits.");
            }

            
            
            // Validate email using regex
            if (!isValidEmail(email)) {
                throw new IllegalArgumentException("Invalid email format.");
            }

            // Validate ATM pin (4 digits)
            if (!pin.matches("\\d{4}")) {
                throw new IllegalArgumentException("ATM Pin should be 4 digits.");
            }

            // Validate pin confirmation
            if (!pin.equals(confirmPin)) {
                throw new IllegalArgumentException("ATM Pins do not match.");
            }

            // If all validations pass, proceed to register
            User user = userService.registerUser(name, age, phoneNumber, email, pin);

            JOptionPane.showMessageDialog(frame, "Registration Successful!\n" +
                    "Account Number: " + user.getAccountNumber() + "\n" +
                    "Card Number: " + user.getCardNumber() + "\n" +
                    "CVV: " + user.getCvv());

            frame.dispose();
            new MainMenuUI();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input: Please ensure age is a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid Input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (javax.persistence.PersistenceException ex) {
            // Handle duplicate entry error or constraint violation
            if (ex.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
                JOptionPane.showMessageDialog(frame, "Phone number already exists. Please use a different number.", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "PhoneNumber already exist: " + ex.getMessage(), "message", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "PhoneNumber already exist. " , "message", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to validate email format
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
