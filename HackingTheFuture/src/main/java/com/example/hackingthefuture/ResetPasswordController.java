package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResetPasswordController {
    @FXML
    private Button button_resetpw;
    @FXML
    private Button button_login;
    @FXML
    private PasswordField pf_newpw;
    @FXML
    private PasswordField pf_cpassword;

    @FXML
    void back(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Login.fxml",sourceNode,"Login");
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

        if (!Function.isMatchPassword(newPassword, confirmPassword)) {
            Function.warning("Error", "Password Reset Failed", "Passwords do not match.");
            return;
        }

        // Update password in the database
        PassData data = PassData.getInstance();
        String email = data.getEmail();
        String updateQuery = "UPDATE user SET password = ? WHERE email=?";
        String hashedPassword = Function.hashPassword(newPassword);
        Object[] params = {hashedPassword, email};

        String sqlQuery = "SELECT * FROM user WHERE email='"+email+"'";
        ResultSet resultSet = Function.getData(sqlQuery);
        try {
            if(resultSet.next()){
                data.setPassword(resultSet.getString("password"));
                String currentPW = data.getPassword();
                if (currentPW.equals(hashedPassword)) {
                    Function.warning("Error", "Password Reset Failed", "Password is the same as current password. Please enter another password.");
                    return;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Node sourceNode = (Node) event.getSource();
        if (Function.update(updateQuery, params)) {
            Function.success("Success", null, "Your password has been successfully updated.");
            Function.nextPage("Login.fxml", sourceNode, "Login");
        } else {
            Function.warning("Error", null, "Failed to update password. Please try again later.");
        }
    }
}
