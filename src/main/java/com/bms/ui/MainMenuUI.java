package com.bms.ui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainMenuUI {
    private JFrame frame;

    public MainMenuUI() {
        frame = new JFrame("ATM Machine - Main Menu");
        frame.setLayout(new GridLayout(3, 1));
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            frame.dispose();
            new RegisterUI();
        });

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            frame.dispose();
            new LoginUI();
        });

        frame.add(new JLabel("Welcome to the ATM Machine", JLabel.CENTER));
        frame.add(registerButton);
        frame.add(loginButton);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new MainMenuUI();
    }
}
