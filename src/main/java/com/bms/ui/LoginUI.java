package com.bms.ui;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.bms.entity.User;
import com.bms.service.UserService;

public class LoginUI {
    private JFrame frame;
    private JTextField phoneOrCardField;
    private JPasswordField pinField;
    private UserService userService = new UserService();

    public LoginUI() {
        frame = new JFrame("Login");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 1));

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel optionLabel = new JLabel("Select Login Method:", SwingConstants.CENTER);
        JRadioButton phoneOption = new JRadioButton("Phone Number");
        JRadioButton cardOption = new JRadioButton("Card Number");
        ButtonGroup loginOptions = new ButtonGroup();
        loginOptions.add(phoneOption);
        loginOptions.add(cardOption);

        phoneOption.setSelected(true); // Default selection

        JLabel inputLabel = new JLabel("Enter Phone Number/Card Number:");
        phoneOrCardField = new JTextField(20);

        JLabel pinLabel = new JLabel("Enter ATM PIN:");
        pinField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            if (phoneOption.isSelected()) {
                loginByPhoneNumber();
            } else {
                loginByCardNumber();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose();
            new MainMenuUI(); // Navigate back to the main UI
        });

        frame.add(titleLabel);
        frame.add(optionLabel);
        frame.add(phoneOption);
        frame.add(cardOption);
        frame.add(inputLabel);
        frame.add(phoneOrCardField);
        frame.add(pinLabel);
        frame.add(pinField);
        frame.add(loginButton);
        frame.add(backButton);

        frame.setVisible(true);
    }

    private void loginByPhoneNumber() {
        String phoneNumber = phoneOrCardField.getText();
        String pin = new String(pinField.getPassword());

        User user = userService.authenticateByPhoneNumber(phoneNumber, pin);
        if (user != null) {
            JOptionPane.showMessageDialog(frame, "Login successful!");
            frame.dispose();
            new DashBoardUI(user); // Redirect to account operations
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid Phone Number or PIN.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loginByCardNumber() {
        String cardNumber = phoneOrCardField.getText();
        String pin = new String(pinField.getPassword());

        User user = userService.authenticateByCardNumber(cardNumber, pin);
        if (user != null) {
            JOptionPane.showMessageDialog(frame, "Login successful!");
            frame.dispose();
            new DashBoardUI(user); // Redirect to account operations
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid Card Number or PIN.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new LoginUI();
    }
}
