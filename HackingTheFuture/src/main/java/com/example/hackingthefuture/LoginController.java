package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private Button button_forgotpw;

    @FXML
    private Button button_login;

    @FXML
    private Button button_signup;

    @FXML
    private PasswordField pf_password;

    @FXML
    private TextField tf_username;

    private Stage stage;

    private Scene scene;

    @FXML
    void login(ActionEvent event) throws IOException, MessagingException {
        String usernameOrEmail = tf_username.getText();
        String password = pf_password.getText();

        if(usernameOrEmail.isEmpty() || password.isEmpty()){
            Function.warning("Login Error",null,"Please fill in the username/email and password.");
        }else{

            String sqlQuery = "SELECT * FROM user WHERE username='"+usernameOrEmail+"' OR email='" + usernameOrEmail +"' AND password='"+Function.hashPassword(password)+"'";

            try (Connection con = Function.getConnection();
                 PreparedStatement preparedStatement = con.prepareStatement(sqlQuery)) {

                ResultSet resultSet = Function.getData(sqlQuery);

                if(resultSet.next()){
//                    String code = Function.generateRandomNumber();
//                    PassData data = PassData.getInstance();
//                    data.setCode(code);
//                    data.setResend(3);
                    String hashedPasswordDB = resultSet.getString("password");
                    String hashedPasswordEntered = Function.hashPassword(password);

                    if (hashedPasswordEntered != null && hashedPasswordEntered.equals(hashedPasswordDB)) {
                    UserClass.setUsername(resultSet.getString("username"));
                    UserClass.setEmail(resultSet.getString("email"));
                    UserClass.setPassword(Function.hashPassword(password));
                    UserClass.setRole(resultSet.getString("role"));
                    UserClass.setCoordinateX(resultSet.getDouble("coordinateX"));
                    UserClass.setCoordinateY(resultSet.getDouble("coordinateY"));


//                    JavaMail.sendmail(resultSet.getString("email"),"The login verification code is : " + code,"Login Verification");
                    String roleDB = resultSet.getString("role");
                    Function.inform("Login Successful",null,"Succesfully logged in!");
//                    Node sourceNode = (Node) event.getSource();
//                    Function.nextPage("LoginVerify.fxml",sourceNode,"Login Verification");
                     openHomePage(roleDB);
                }else{
                    Function.inform("Login Error",null,"Incorrect username/email and password.");
                }
                }else{
                    Function.inform("Login Error",null,"User not found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void openHomePage(String roleDB) throws IOException {
        if (roleDB.equals("Educator")) {
            try {

                URL fxmlLocation = getClass().getResource("/com/example/hackingthefuture/Educator.fxml");
                if (fxmlLocation == null) {
                    throw new FileNotFoundException("Fxml File Not Found");
                }
                FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
                Scene scene = new Scene(fxmlLoader.load());
                EducatorController educatorController = fxmlLoader.getController();
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) tf_username.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else if (roleDB.equals("Young Student")) {
            try {

                URL fxmlLocation = getClass().getResource("/com/example/hackingthefuture/Student.fxml");
                if (fxmlLocation == null) {
                    throw new FileNotFoundException("Fxml File Not Found");
                }
                FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
                Scene scene = new Scene(fxmlLoader.load());
                StudentController studentController = fxmlLoader.getController();
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) tf_username.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                URL fxmlLocation = getClass().getResource("/com/example/hackingthefuture/Parent.fxml");
                if (fxmlLocation == null) {
                    throw new FileNotFoundException("Fxml File Not Found");
                }
                FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
                Scene scene = new Scene(fxmlLoader.load());
                ParentController parentController = fxmlLoader.getController();
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) tf_username.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            // ...
        }
    }

    @FXML
    public void sign(ActionEvent event) throws IOException {

        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Signup.fxml",sourceNode,"Signup");
    }

    @FXML
    void forgot(ActionEvent event) throws IOException {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("ForgotPassword.fxml",sourceNode,"Forgot Password");
    }
}
