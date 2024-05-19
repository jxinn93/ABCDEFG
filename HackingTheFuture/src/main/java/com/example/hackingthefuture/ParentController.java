package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ParentController extends ViewProfileController {
        @FXML
        private Label nameLabel;
        @FXML
        private TextField nameSearchTF;

        public void initialize() {
            nameLabel.setText(UserClass.getUsername());
        }

        @FXML
        public void viewProfileBTN(ActionEvent actionEvent) {
            try {
                String nameSearch = nameSearchTF.getText();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewProfile.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                ViewProfileController viewProfileController = fxmlLoader.getController();

                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) nameSearchTF.getScene().getWindow();
                currentStage.close();
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }

        @FXML
        public void viewOtherProfile(ActionEvent actionEvent) {
            String nameSearch=nameSearchTF.getText();
            boolean found=false;
            try {
                String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
                String SUser = "root";
                String SPass = "";
                Connection con = DriverManager.getConnection(SUrl, SUser, SPass);
                String email="";
                Double coordinateX=0.0;
                Double coordinateY=0.0;
                String role="";
                String query = "SELECT * FROM user WHERE username=?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1,nameSearch);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    email=rs.getString("email");
                    coordinateX = rs.getDouble("coordinateX");
                    coordinateY = rs.getDouble("coordinateY");
                    role=rs.getString("role");
                    found=true;
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewProfile.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    ViewProfileController viewProfileController = fxmlLoader.getController();
                    viewProfileController.profile(nameSearch,email, coordinateX, coordinateY ,role, nameSearch);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                    Stage currentStage = (Stage) nameSearchTF.getScene().getWindow();
                    currentStage.close();
                }
                if (!found){
                    Alert.showAlert("User not found!", "Error");
                }
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void makeBookingBTN(ActionEvent actionEvent) {
            try {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("5Shortest.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                display5EuclideanDistance display5EuclideanDistance = fxmlLoader.getController();
                display5EuclideanDistance.launchTestDistance();

                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) nameSearchTF.getScene().getWindow();
                currentStage.close();
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }

        public void viewBookingBTN(ActionEvent actionEvent) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewEvent.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                ViewEventController viewEventController = fxmlLoader.getController();
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) nameSearchTF.getScene().getWindow();
                currentStage.close();
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }

        public void logoutBTN(ActionEvent actionEvent) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                LoginController loginController = fxmlLoader.getController();
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) nameSearchTF.getScene().getWindow();
                currentStage.close();
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }


    }

