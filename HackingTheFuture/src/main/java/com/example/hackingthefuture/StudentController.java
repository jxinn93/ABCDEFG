package com.example.hackingthefuture;

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

public class StudentController extends ViewProfileController{


    @FXML
    private Label nameLabel;
    @FXML
    private TextField nameSearchTF;

    public void initialize() {
        nameLabel.setText(UserClass.getUsername());
    }

    @FXML
    public void viewProfileBTN(ActionEvent actionEvent) {
        Node sourceNode = (Node) actionEvent.getSource();
        Function.nextPage("ViewProfile.fxml", sourceNode, "Profile");
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
                viewProfileController.setNameSearch(nameSearch);

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



    public void viewQuizBTN(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Function.nextPage("viewQuiz.fxml", source, "Quiz");

    }

    public void viewEventBTN(ActionEvent actionEvent) {
        Node sourceNode = (Node) actionEvent.getSource();
        Function.nextPage("viewEvent.fxml", sourceNode, "Event");
    }

    public void logoutBTN(ActionEvent actionEvent) {
        Node sourceNode = (Node) actionEvent.getSource();
        Function.nextPage("Login.fxml", sourceNode, "Login");
    }


    public void viewLeaderboardBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Leaderboard.fxml", sourceNode, "Leaderboard");
    }

    public void DisplayN(ActionEvent event) throws SQLException {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Friend.fxml", sourceNode, "Friend");
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

