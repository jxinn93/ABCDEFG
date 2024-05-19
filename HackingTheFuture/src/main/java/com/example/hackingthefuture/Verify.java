package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.mail.MessagingException;
import java.net.URL;
import java.util.ResourceBundle;

public class Verify implements Initializable {
    @FXML
    private Button button_verify;
    @FXML
    private Button button_signup;
    @FXML
    private Label email;
    @FXML
    private TextField tf_vcode;

    PassData data = PassData.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Use receivedData in the initialize method
        email.setText(data.getEmail());
    }

    @FXML
    void resend(ActionEvent event) throws MessagingException {
        String code = Function.generateRandomNumber();
        data.setCode(code);
        data.setResend(3);
        JavaMail.sendmail(data.getEmail(), "The Verify code is : " + code,"Verify Email");
        Function.inform("Resend code",null,"Resend Verify Code Successfully.");
    }

    @FXML
    void verify(ActionEvent event) {
        String code = tf_vcode.getText();
        int resend = data.getResend();

        if(data.getCode().equals(code)){
            if(data.getResend() == 0){
                Function.warning("Attempt finished",null,"Please resend the verify code.");
            }else{
                Object[] objects = {data.getUsername(), data.getPassword(), data.getEmail(), data.getRole(), data.getxCoordinate(), data.getyCoordinate()};
                String sql = "INSERT INTO user (username, password, email, role, coordinateX, coordinateY) VALUES (?, ?, ?, ?, ?, ?)";

                Node sourceNode = (Node) event.getSource();
                if(Function.insert(sql,objects)){
                    String username = data.getUsername();
                    Database.createDatabase(username);
                    PassData.getInstance().clearAllValues();
                    Function.success("Successfully",null,"Sign Up Successfully.");

                    Function.nextPage("Login.fxml",sourceNode,"Login");
                }else{
                    PassData.getInstance().clearAllValues();
                    Function.warning("Unsuccessfully",null,"Please sign again or contact admin.");
                    Function.nextPage("SignUp.fxml",sourceNode,"Sign Up");
                }
            }
        }else{
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
