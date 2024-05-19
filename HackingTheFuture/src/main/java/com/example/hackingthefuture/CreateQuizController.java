package com.example.hackingthefuture;

import com.example.hackingthefuture.EducatorController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreateQuizController {
    @FXML
    public TextField quizTitleTF;
    public TextField quizDescriptionTF;
    public MenuButton quizThemeMENU;
    public MenuItem Science;
    public MenuItem Technology;
    public MenuItem Engineering;
    public MenuItem Mathematics;
    public TextField quizLinkTF;
    public Connection con;

    String selectedTheme;

    @FXML
    public void initialize() {
        try {
            String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
            String SUser = "root";
            String SPass = "";
            con = DriverManager.getConnection(SUrl, SUser, SPass);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void backBTN(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Educator.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            EducatorController educatorController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) quizTitleTF.getScene().getWindow();
            currentStage.close();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void viewProfileBTN(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewProfile.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ViewProfileController viewProfileController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) quizTitleTF.getScene().getWindow();
            currentStage.close();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void saveBTN(ActionEvent actionEvent) {
        try {
            String username=UserClass.getUsername();
            String quizTitle=quizTitleTF.getText();
            String quizDescription=quizDescriptionTF.getText();
            if (quizTitle.isEmpty()) {
                Alert.showAlert("Quiz Title is required!", "Error");
            } else if (quizDescription.isEmpty()) {
                Alert.showAlert("Quiz Description is required!", "Error");
            } else if (quizThemeMENU == null) {
                Alert.showAlert("Please select a theme!", "Error");
            } else if (quizLinkTF.getText().isEmpty()) {
                Alert.showAlert("Please upload quiz link!", "Error");
            } else {
                int numberOfQuiz=1;
                String query = "SELECT * FROM createQuiz WHERE username=?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    numberOfQuiz= rs.getInt("numberOfQuiz")+1;
                }
                query = "INSERT INTO createQuiz (username,quizTitle,quizDescription,selectedTheme,quizLink,numberOfQuiz) VALUES (?,?,?,?,?,?)";
                ps = con.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, quizTitle);
                ps.setString(3, quizDescription);
                ps.setString(4, selectedTheme);
                ps.setString(5, quizLinkTF.getText());
                ps.setInt(6, numberOfQuiz);
                ps.executeUpdate();
                quizTitleTF.setText("");
                quizDescriptionTF.setText("");
                quizThemeMENU.setText("Quiz Theme");
                quizLinkTF.setText("");
                Alert.showDialog("Save Successfully!", "Success");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void scienceSelected(ActionEvent actionEvent) {
        selectedTheme = "Science";
        quizThemeMENU.setText(selectedTheme);
    }

    public void technologySelected(ActionEvent actionEvent) {
        selectedTheme = "Technology";
        quizThemeMENU.setText(selectedTheme);
    }

    public void engineeringSelected(ActionEvent actionEvent) {
        selectedTheme = "Engineering";
        quizThemeMENU.setText(selectedTheme);
    }

    public void mathematicsSelected(ActionEvent actionEvent) {
        selectedTheme = "Mathematics";
        quizThemeMENU.setText(selectedTheme);
    }

    public void goToLink(ActionEvent actionEvent) throws IOException {
        Desktop desktop=Desktop.getDesktop();
        desktop.browse(java.net.URI.create(quizLinkTF.getText()));
    }
}
