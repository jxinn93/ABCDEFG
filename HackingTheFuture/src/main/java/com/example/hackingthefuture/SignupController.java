package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private Button button_login;
    @FXML
    private Button button_signup;
    @FXML
    private PasswordField pf_password;
    @FXML
    private ChoiceBox<String> cb_role;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField pf_cpassword;
    private PassData passData;
    private Stage stage;
    private Scene scene;
    private String[] roles = {"Educator", "Parent", "Young Student"};

    @FXML
    public void login(ActionEvent event) throws IOException {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Login.fxml",sourceNode,"Login");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cb_role.getItems().addAll(roles);
        passData = PassData.getInstance();
    }

    @FXML
    public boolean verifyEmail(ActionEvent event) throws IOException, MessagingException {
        String username = tf_username.getText();
        String email = tf_email.getText();
        String password = pf_password.getText();
        String c_password = pf_cpassword.getText();
        String role = cb_role.getValue();
        int points = 0;

        if (username.isEmpty() || password.isEmpty() || c_password.isEmpty() || email.isEmpty() || role.isEmpty()) {
            Function.warning("Error", null, "Please fill in all the required fields.");
            return false;
        } else {
            if (!(Function.isValidEmail(email)) && !(Function.isMatchPassword(password, c_password))) {
                Function.warning("Invalid and Not Match", null, "Email invalid and Password not match.");
                return false;
            } else if (!(Function.isValidEmail(email))) {
                Function.warning("Invalid email", null, "Please enter a valid email address.");
                return false;
            } else if (!(Function.isMatchPassword(password, c_password))) {
                Function.warning("Password Not Match", null, "Password and Confirm Password must be the same.");
                return false;
            } else if (!(Function.strongpasswrod(password))) {
                Function.warning("Weak Password", null, "It should contains at least one lowercase English character.\nIt should contains at least one uppercase English character.\nIt should contains at least one special character.\nThe special characters are: !@#$%^&*()-+ \nIts length should be at least 8.\nIt should contains at least one digit.");
                return false;
            } else {

                try {
                    if (Function.usernameExists(username) && Function.emailExists(email)) {
                        Function.warning("Username and Email Exist", null, "Username and Email already exits!\nPlease use another username and email.");
                        return false;
                    } else if (Function.usernameExists(username)) {
                        Function.warning("Username Exist", null, "Username already exits!\nPlease use another username.");
                        return false;
                    } else if (Function.emailExists(email)) {
                        Function.warning("Email Exist", null, "Email already exits!\nPlease use another email.");
                        return false;
                    } else {
                        // If all validation passes, save the signup details
                        String hashedPassword = Function.hashPassword(password);
                        double xCoordinate = Math.random() * 1000 - 500; // Example: Generate random x coordinate
                        double yCoordinate = Math.random() * 1000 - 500; // Example: Generate random y coordinate
                            PassData data = PassData.getInstance();
                            data.setUsername(username);
                            data.setEmail(email);
                            data.setPassword(hashedPassword);
                            data.setRole(role);
                            data.setxCoordinate(xCoordinate);
                            data.setyCoordinate(yCoordinate);

                            Node sourceNode = (Node) event.getSource();
                            Function.nextPage("SignupVerify.fxml",sourceNode,"Verification Email");
                            return true;
                    }
                } catch (Exception e) {
                    Function.warning("Error", null, "Database connection error.");
                    e.printStackTrace();
                    return false;
                }
            }
        }
        }

 public void signup(ActionEvent event) throws IOException {
     if (passData.isEmailVerified()) {
         try {
             if (verifyEmail(event)) {
                 login(event);
             } else {
                 Function.warning("Error", null, "Signup failed. Please try again later.");
             }
         } catch (MessagingException e) {
             throw new RuntimeException(e);
         }
     } else {
         Function.warning("Email Not Verified", null, "Please verify your email address before signing up.");
     }
 }



            }


