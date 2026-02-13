package com.info.service;

import com.info.dao.UserDAO;
import com.info.exception.UserNotFoundException;
import com.info.modal.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserService {
    private final static Scanner sc  = new Scanner(System.in);
    private final UserDAO userDAO = new UserDAO();

    public static void addUser() {
        System.out.println("\n--- Create New User ---");
        sc.nextLine();

        System.out.print("Enter user full name: ");
        String name = sc.nextLine().trim();
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            System.out.println("Error: Name must contain only alphabets and spaces.");
            return;
        }

        System.out.print("Enter user email: ");
        String email = sc.nextLine().trim();

        if (email.contains(" ")) {
            System.out.println("Error: Email cannot contain spaces.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            System.out.println("Error: Invalid email format.");
            return;
        }

        List<String> phoneList = new ArrayList<>();
        System.out.print("Enter primary phone number (10 digits): ");
        String primaryPhone = sc.nextLine().trim();

        if (!primaryPhone.matches("^\\d{10}$")) {
            System.out.println("Error: Number must be exactly 10 digits and contain only numbers.");
            return;
        }
        phoneList.add(primaryPhone);

        while (true) {
            System.out.print("Do you want to add another number? (y/n): ");
            String choice = sc.nextLine().trim().toLowerCase();

            if (choice.charAt(0) == 'y') {
                System.out.print("Enter additional phone number: ");
                String additionalPhone = sc.nextLine().trim();

                if (additionalPhone.matches("^\\d{10}$")) {
                    phoneList.add(additionalPhone);
                } else {
                    System.out.println("Error: Invalid number format.");
                }
            } else {
                break;
            }
        }

        User newUser = new User(name, email, phoneList);
        try {
            UserDAO.createUser(newUser);
            System.out.println("SUCCESS: User created successfully.");
        } catch (Exception e) {
            System.out.println("DB Error: " + e.getMessage());
        }
    }
    public static void fetchUserById() {
        System.out.println("\n--- Search User by ID ---");
        System.out.print("Enter the User ID: ");

        try {
            int id = sc.nextInt();

            User user = UserDAO.fetchUserById(id);

            System.out.println("----------------------------");
            System.out.println("User Details Found:");
            System.out.println("ID    : " + user.getId());
            System.out.println("Name  : " + user.getName());
            System.out.println("Email : " + user.getEmail());
            System.out.println("Phones: " + String.join(", ", user.getPhoneList()));
            System.out.println("----------------------------");
            System.out.println("Operation Successful: User data retrieved.");

        } catch (UserNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid Input! Please enter a numeric ID.");
            sc.next();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public static void updateProcess() {
        try {
            System.out.print("Enter User ID to update: ");
            int id = sc.nextInt();
            sc.nextLine();

            User user = UserDAO.fetchUserById(id);

            System.out.println("\n--- Current Record ---");
            System.out.println("Name  : " + user.getName());
            System.out.println("Email : " + user.getEmail());
            System.out.println("Phones: " + user.getPhoneList());
            System.out.println("----------------------");

            // 2. Update Basic Info
            System.out.print("Enter new Full Name: ");
            String newName = sc.nextLine().trim();
            if (!newName.matches("^[a-zA-Z\\s]+$")) {
                System.out.println("Error: Name must contain only alphabets and spaces.");
                return;
            }

            System.out.print("Enter new Email: ");
            String newEmail = sc.nextLine().trim();
            if (newEmail.contains(" ")) {
                System.out.println("Error: Email cannot contain spaces.");
                return;
            }

            if (!newEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                System.out.println("Error: Invalid email format.");
                return;
            }

            UserDAO.updateBasicInfo(id, newName, newEmail);
            System.out.println("SUCCESS: Basic info updated.");

            // 3. Phone Update Logic
            System.out.print("\nDo you want to update phone numbers? (y/n): ");
            String choice = sc.nextLine().trim();

            if (choice.equalsIgnoreCase("y")) {
                UserDAO.showUserPhones(id);

                System.out.print("Enter the Phone ID (the numeric ID shown above) to change: ");
                int phoneId = sc.nextInt();
                sc.nextLine();

                System.out.print("Enter the new 10-digit number: ");
                String newNumber = sc.nextLine();

                if (newNumber.matches("\\d{10}")) {
                    UserDAO.updatePhoneById(phoneId, newNumber);
                    System.out.println("SUCCESS: Phone number updated.");
                } else {
                    System.out.println("ERROR: Invalid phone format. Update skipped.");
                }
            }

        } catch (UserNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public static void deleteUser() {
        System.out.println("\n--- Delete User Process ---");
        System.out.print("Enter User ID to delete: ");

        try {
            int id = sc.nextInt();

            UserDAO.deleteUser(id);

        } catch (UserNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input! Please enter a numeric ID.");
            sc.next();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public static void getAllUsers() {
        System.out.println("\n--- Fetching All User Records ---");
        try {
            List<User> users = UserDAO.fetchAllUsers();

            if (users.isEmpty()) {
                System.out.println("No records found in the database.");
            } else {
                System.out.println("------------------------------------------------------------------");
                System.out.printf("%-5s | %-20s | %-25s | %-20s\n", "ID", "Name", "Email", "Phone Numbers");
                System.out.println("------------------------------------------------------------------");

                for (User u : users) {
                    String phoneStr = (u.getPhoneList() != null && !u.getPhoneList().isEmpty())
                            ? String.join(", ", u.getPhoneList())
                            : "No Numbers";

                    System.out.printf("%-5d | %-20s | %-25s | %-20s\n",
                            u.getId(), u.getName(), u.getEmail(), phoneStr);
                }
                System.out.println("------------------------------------------------------------------");
                System.out.println("SUCCESS: Total records retrieved: " + users.size());
            }
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: " + e.getMessage());
        }
    }
}
