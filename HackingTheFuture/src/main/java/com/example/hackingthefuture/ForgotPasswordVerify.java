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
    private Label emailLabel;


    @FXML
    private TextField tf_vcode;
    private String emailText;
    PassData data = PassData.getInstance();
    public ForgotPasswordVerify() {}

    public ForgotPasswordVerify(String emailEntered) {
        this.emailText = emailEntered;
    }
    public String getEmailEntered(){
        return emailLabel.getText();
    }

    private void setEmailLabel(){
        emailLabel.setText("Email: " + data.getEmailEntered());
        System.out.println(emailLabel.getText());
    }
    public void initialize(URL location, ResourceBundle resources) {
        // Use receivedData in the initialize method
        setEmailLabel();
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
        int send = data.getSend();

        if(data.getCode().equals(code)) {
            Node sourceNode = (Node) event.getSource();
            Function.nextPage("ResetPassword.fxml",sourceNode,"Reset");
            if(data.getSend() == 1) {
                Function.success("Successfully",null,"Please enter the new password.");
            } else if (data.getResend() ==0) {
                Function.warning("Attempt finished",null,"Please resend the verify code.");

            } else {

                    PassData.getInstance().clearAllValues();
                    Function.warning("Unsuccessfully",null,"Unsuccessfully Set the New Password.");
                    Function.nextPage("login.fxml",sourceNode,"Login");
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

    public void back(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("ForgotPassword.fxml",sourceNode,"ForgotPassword");
    }


    public void Get(ActionEvent event) throws MessagingException {
        String code = Function.generateRandomNumber();
        data.setCode(code);
        data.setSend(1);
        JavaMail.sendmail(data.getEmail(), "The Verify code is : " + code,"Verify Email");
        Function.inform("Resend code",null,"Verify Code Successfully.");
    }
}
