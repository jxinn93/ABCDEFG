package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    }

    @FXML
    public void signup(ActionEvent event) throws IOException, MessagingException {
        String username = tf_username.getText();
        String email = tf_email.getText();
        String password = pf_password.getText();
        String c_password = pf_cpassword.getText();
        String role = cb_role.getValue();
        int points = 0;
//        double xCoordinate = Math.random() * 1000 - 500; // Example: Generate random x coordinate
//        double yCoordinate = Math.random() * 1000 - 500; // Example: Generate random y coordinate

        if(username.isEmpty() || password.isEmpty() || c_password.isEmpty() || email.isEmpty() || role.isEmpty()){
            Function.warning("Error",null,"Please fill in all the required fields.");
        }else{
            if (!(Function.isValidEmail(email)) && !(Function.isMatchPassword(password,c_password))){
                Function.warning("Invalid and Not Match",null,"Email invalid and Password not match.");
            } else if (!(Function.isValidEmail(email))) {
                Function.warning("Invalid email",null,"Please enter a valid email address.");
            } else if (!(Function.isMatchPassword(password,c_password))) {
                Function.warning("Password Not Match",null,"Password and Confirm Password must be the same.");
            }else if(!(Function.strongpasswrod(password))){
                Function.warning("Weak Password",null,"It should contains at least one lowercase English character.\nIt should contains at least one uppercase English character.\nIt should contains at least one special character.\nThe special characters are: !@#$%^&*()-+ \nIts length should be at least 8.\nIt should contains at least one digit.");
            }
            else{
                if(Function.username(username) && Function.email(email)){
                    Function.warning("Username and Email Exist",null,"Username and Email already exits!\nPlease use another username and email.");
                } else if (Function.username(username)) {
                    Function.warning("Username Exist",null,"Username already exits!\nPlease use another username.");
                } else if (Function.email(email)) {
                    Function.warning("Email Exist",null,"Email already exits!\nPlease use another email.");
                } else {
                    // If all validation passes, save the signup details
                    String hashedPassword = Function.hashPassword(password);
                    double xCoordinate = Math.random() * 1000 - 500; // Example: Generate random x coordinate
                    double yCoordinate = Math.random() * 1000 - 500; // Example: Generate random y coordinate
                    boolean saved = Function.saveSignupDetails(username, email, hashedPassword, role, xCoordinate, yCoordinate, points);
                    PassData data = PassData.getInstance();
                    data.setUsername(username);
                    data.setEmail(email);
                    data.setPassword(hashedPassword);
                    data.setRole(role);
                    data.setxCoordinate(xCoordinate);
                    data.setyCoordinate(yCoordinate);
                    if (saved) {
                        Function.success("Success", null, "Signup successful!");
                        login(event);
                    } else {
                        Function.warning("Error", null, "Signup failed. Please try again later.");
                    }
                }
//                else{
//                    String code = Function.generateRandomNumber();
//                    String hashedPW = Function.hashPassword(password);
//
//                    JavaMail.sendmail(email,"The Verification code is : " + code,"Verification Email");
//                    PassData data = PassData.getInstance();
//                    data.setUsername(username);
//                    data.setEmail(email);
//                    data.setPassword(hashedPW);
//                    data.setRole(role);
//                    data.setxCoordinate(xCoordinate);
//                    data.setyCoordinate(yCoordinate);
//                    data.setCode(code);
//                    data.setResend(3);
//
//                    Node sourceNode = (Node) event.getSource();
//                    Function.nextPage("SignupVerify.fxml",sourceNode,"Verification Email");
//                }
            }
        }
    }
}
