package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Verify implements Initializable  {
    @FXML
    private Button button_verify;
    @FXML
    private Button button_signup;
    @FXML
    private Label email;
    @FXML
    private TextField tf_vcode;
    private String emailText;

    PassData data = PassData.getInstance();
    public Verify(){}

    public Verify (String email){
        this.emailText = email;
    }
        public String getEmail(){
            return email.getText();
        }
        private void setEmailLabel(){
        email.setText("Email: " + data.getEmail());
        }
    public void initialize(URL location, ResourceBundle resources) {
        setEmailLabel();
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
    void submit(ActionEvent event) {
        String code = tf_vcode.getText();
        int resend = data.getResend();

        if(data.getCode().equals(code)){
            if(data.getResend() == 0){
                Function.warning("Attempt finished",null,"Please resend the verify code.");
            }else{
                Object[] objects = {data.getUsername(), data.getPassword(), data.getEmail(), data.getRole(), data.getxCoordinate(), data.getyCoordinate(), data.getPoints()};
                String sql = "INSERT INTO user (username, password, email, role, coordinateX, coordinateY, points) VALUES (?, ?, ?, ?, ?, ?, ?)";
                data.setEmailVerified(true);

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

    public void back(ActionEvent actionEvent) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Signup.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                SignupController signupController = fxmlLoader.getController();
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) tf_vcode.getScene().getWindow();
                currentStage.close();
            }

    public void Get(ActionEvent event) throws MessagingException {
        String code = Function.generateRandomNumber();
        data.setCode(code);
        data.setResend(3);
        JavaMail.sendmail(data.getEmail(), "The Verify code is : " + code,"Verify Email");
        Function.inform("Resend code",null,"Verify Code Successfully.");
    }




}



