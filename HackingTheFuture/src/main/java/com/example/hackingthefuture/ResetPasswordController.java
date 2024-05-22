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

        // Update password in the database
        PassData data = PassData.getInstance();
        String email = data.getEmail();
        String hashedPassword = Function.hashPassword(newPassword);
        String updateQuery = "UPDATE user SET password = ? WHERE email= ?";

        try (Connection con = DriverManager.getConnection(SUrl, SUser, SPass);
             PreparedStatement ps = con.prepareStatement(updateQuery)) {
            ps.setString(1, hashedPassword);
            ps.setString(2, email);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected>0) {
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


    }
}
