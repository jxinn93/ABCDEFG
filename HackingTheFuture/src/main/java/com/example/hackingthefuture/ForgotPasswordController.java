package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.mail.MessagingException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgotPasswordController {
    @FXML
    private Button button_next;
    @FXML
    private Button button_login;
    @FXML
    private TextField tf_email;

    @FXML
    void back(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Login.fxml",sourceNode,"Login");
    }

    @FXML
    void submit(ActionEvent event) throws MessagingException {
        String email = tf_email.getText();
        if (email.isEmpty()) {
            Function.warning("Error",null,"Please fill in the email address field.");
        }
        if(!Function.isValidEmail(email)){
            Function.warning("Invalid Email",null,"Email address is invalid.");
        }else{
            String sqlQuery = "SELECT * FROM user WHERE email='"+email+"'";
            ResultSet resultSet = Function.getData(sqlQuery);
            // Process the ResultSet as needed
            try {
                if(resultSet.next()){
//                    String code = Function.generateRandomNumber();
//                    PassData data = PassData.getInstance();
//                    data.setUsername(resultSet.getString("username"));
//                    data.setEmail(email);
//                    data.setCode(code);
//                    data.setResend(3);
//                    JavaMail.sendmail(resultSet.getString("email"),"Verify Code : " + code,"Reset Password");
                    PassData data = PassData.getInstance();
                    data.setEmail(email);
                    Node sourceNode = (Node) event.getSource();
                    Function.nextPage("ResetPassword.fxml",sourceNode,"Verify");
                }else{
                    Function.inform("Email Error",null,"Your email hasn't been sign up.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
