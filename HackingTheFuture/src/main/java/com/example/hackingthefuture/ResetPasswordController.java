package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;

public class ResetPasswordController {
    @FXML
    private Button button_resetpw;
    @FXML
    private Button button_login;
    @FXML
    private PasswordField pf_newpw;
    @FXML
    private PasswordField pf_cpassword;
    private String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
    private String SUser = "root";
    private String SPass = "";

    @FXML
    void back(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Login.fxml", sourceNode, "Login");
    }

    @FXML
    void submit(ActionEvent event) {
        String newPassword = pf_newpw.getText();
        String confirmPassword = pf_cpassword.getText();

        // Validate password fields
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Function.warning("Error", "Password Reset Failed", "Please fill in both password fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Function.warning("Error", "Password Reset Failed", "Passwords do not match.");
            return;
        }
        if (!(Function.strongpasswrod(newPassword))) {
            Function.warning("Weak Password", null, "It should contains at least one lowercase English character.\nIt should contains at least one uppercase English character.\nIt should contains at least one special character.\nThe special characters are: !@#$%^&*()-+ \nIts length should be at least 8.\nIt should contains at least one digit.");
            return;
        }
        // Update password in the database
        PassData data = PassData.getInstance();
        String email = data.getEmail();

        String currentPassword = null;
        String currentPasswordQuery = "SELECT password FROM user WHERE email = ?";

        try (Connection con = DriverManager.getConnection(SUrl, SUser, SPass);
             PreparedStatement ps = con.prepareStatement(currentPasswordQuery)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    currentPassword = rs.getString("password");
                }
            }

            String hashedPassword = Function.hashPassword(newPassword);
            if (hashedPassword.equals(currentPassword)) {
                Function.warning("Error", "Password Reset Failed", "The new password cannot be the same as the current password.");
                return;
            }
            String updateQuery = "UPDATE user SET password = ? WHERE email= ?";

            try (
                    PreparedStatement UpdatedPs = con.prepareStatement(updateQuery)) {
                UpdatedPs.setString(1, hashedPassword);
                UpdatedPs.setString(2, email);
                int rowsAffected = UpdatedPs.executeUpdate();
                if (rowsAffected > 0) {
                    Function.success("Success", null, "Your password has been successfully updated.");
                    Node sourceNode = (Node) event.getSource();
                    Function.nextPage("Login.fxml", sourceNode, "Login");
                } else {
                    Function.warning("Error", null, "Failed to update password. Please try again later.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Function.warning("Error", null, "Failed to update password. Please try again later.");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
