package com.example.hackingthefuture;

import com.example.hackingthefuture.CreateQuizController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;



public class EducatorController extends ViewProfileController{
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
                viewProfileController.profile(nameSearch,email, coordinateX, coordinateY ,role, nameSearch); // Pass searched username to ViewProfileController
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

    public void createEventBTN(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateEvent.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            CreateEventController createEventController = fxmlLoader.getController();
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

    public void createQuizBTN(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateQuiz.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            CreateQuizController createQuizController = fxmlLoader.getController();
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

    public void viewEventBTN(ActionEvent actionEvent) {
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


    public void Discussion(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Discussion.fxml"));
            Parent root = loader.load();
            DiscussionController controller = loader.getController();
            controller.initialize();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Discussion");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
