package com.revplay.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class RevPlayApp {
    private static final Logger logger = LogManager.getLogger(RevPlayApp.class);
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        showAppHeader();
        showLoading("Initializing RevPlay");

        logger.info("RevPlay application started");
        boolean running = true;

        while (running) {
            System.out.println("🎵 Main Menu 🎵");
            System.out.println("1. Login");
            System.out.println("2. User Register");
            System.out.println("3. Artist Register");
            System.out.println("4. Forgot Password");
            System.out.println("5. Exit");

            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    RevPlayAppMethonds.handleLogin();
                    break;
                case 2:
                    RevPlayAppMethonds.handleRegister();
                    break;
                case 3:
                    RevPlayAppMethonds.handleArtistRegister();
                    break;
                case 4:
                    RevPlayAppMethonds.handleForgotPassword();
                    break;
                case 5:
                    logger.info("Application exited by user");
                    System.out.println("Thank you for using RevPlay!");
                    running = false;
                    break;

                default:
                    logger.error("Invalid option choice: " + choice);
                    System.out.println("❌ Invalid option. Try again.");
            }
        }
    }

    private static void showAppHeader() {
        System.out.println("╭───────────╮");
        System.out.println("│  RevPlay  │");
        System.out.println("╰───────────╯");
        System.out.println("Version 1.0.0");
        System.out.println();
    }

    private static void showLoading(String message) {
        System.out.print(message + " ");
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(300);
                System.out.print("🎵 ");
            }
            System.out.println("\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
