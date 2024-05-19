package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.mail.MessagingException;
import java.net.URL;
import java.util.ResourceBundle;

public class ForgotPasswordVerify {
    @FXML
    private Button button_verify;
    @FXML
    private Button button_forgotpw;
    @FXML
    private Label email;
    PassData data = PassData.getInstance();
    @FXML
    private TextField tf_vcode;

    public void initialize(URL location, ResourceBundle resources) {
        // Use receivedData in the initialize method
        email.setText(data.getEmail());
    }

    @FXML
    void resend(ActionEvent event) throws MessagingException {
        String code = Function.generateRandomNumber();
        data.setCode(code);
        data.setResend(3);
        JavaMail.sendmail(data.getEmail(),"Verify Code : " + code,"Reset Password");
        Function.inform("Resend code",null,"Resend Verify Code Successfully.");
    }

    @FXML
    void verify(ActionEvent event) throws MessagingException {
        String email= data.getEmail();
        String code = tf_vcode.getText();
        int resend = data.getResend();

        if(data.getCode().equals(code)) {
            if(data.getResend() == 0) {
                Function.warning("Attempt finished",null,"Please resend the verify code.");
            } else {
                String updateQuery = "UPDATE user SET password = ? WHERE email='"+email+"'";
                String name = Function.generateRandomString();
                String password = Function.hashPassword(name);

                Object[] objects = {password};

                Node sourceNode = (Node) event.getSource();
                if(Function.update(updateQuery,objects)) {
                    Function.success("Successfully",null,"Successfully send Username and New Password to you email.");

                    JavaMail.sendmail(email,"Username : " + data.getUsername() + "\nPassword : "+name,"Username and Password!");
                    String username = data.getUsername();
                    PassData.getInstance().clearAllValues();
                    Function.nextPage("Login.fxml",sourceNode,"Login");
                } else {
                    PassData.getInstance().clearAllValues();
                    Function.warning("Unsuccessfully",null,"Unsuccessfully send the Username and New Password.");
                    Function.nextPage("login.fxml",sourceNode,"Login");
                }
            }
        } else {
            if(resend == 0){
                Function.warning("Attempt finished",null,"Please resend the verify code.");
            }else{
                resend--;
                data.setResend(resend);
                Function.inform("Attempt Left",null,"Attempt left : "+data.getResend());
            }
        }
    }
}
