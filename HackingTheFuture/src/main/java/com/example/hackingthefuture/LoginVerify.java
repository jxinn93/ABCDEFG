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

public class LoginVerify {
    @FXML
    private Button button_verify;
    @FXML
    private Button button_signup;
    @FXML
    private Label email;
    @FXML
    private TextField tf_vcode;

    PassData data = PassData.getInstance();

    LoginDetail login = LoginDetail.getInstance();

    public void initialize(URL location, ResourceBundle resources) {
        // Use receivedData in the initialize method
        email.setText(login.getEmail());
    }

    @FXML
    void resend(ActionEvent event) throws MessagingException {
        String code = Function.generateRandomNumber();
        data.setCode(code);
        data.setResend(3);
        JavaMail.sendmail(login.getEmail(), "The Verification code is : " + code,"Email Verification");
        Function.inform("Resend code",null,"Successfully Resent the Verification Code.");
    }

    @FXML
    void verify(ActionEvent event) {
        String code = tf_vcode.getText();
        int resend = data.getResend();

        if(data.getCode().equals(code)){
            if(data.getResend() == 0){
                Function.warning("Attempt finished",null,"Please resend the verification code.");
            } else{
                login.setLogin(true);
                Function.inform("Login Successful",null,"Succesfully logged in!");
//                Node sourceNode = (Node) event.getSource();
//                Function.nextPage("Home.fxml",sourceNode,"Home");
            }
        }else{
            if(resend == 0){
                Function.warning("Attempt finished",null,"Please resend the verification code.");
            }else{
                resend--;
                data.setResend(resend);
                Function.inform("Attempt Left",null,"Attempt left : "+data.getResend());
            }
        }
    }
}
