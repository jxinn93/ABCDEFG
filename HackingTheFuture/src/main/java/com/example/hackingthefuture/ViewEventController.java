package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.PriorityQueue;

public class ViewEventController {
    @FXML
    public Button registerBTN1;
    public Button registerBTN2;
    public Button registerBTN3;
    public Label eventTF1;
    public Label eventTF2;
    public Label eventTF3;

    public Connection con;

    @FXML
    public void initialize() {
        try {
            String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
            String SUser = "root";
            String SPass = "";
            con = DriverManager.getConnection(SUrl, SUser, SPass);

            LocalDate currentDate = LocalDate.now();
            Events events;
            PriorityQueue<Events> eventList = new PriorityQueue<>();

            String query = "SELECT * FROM createevent ";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                events = new Events();
                LocalDate eventDate = rs.getDate("eventDate").toLocalDate();
                if(eventDate.isEqual(currentDate)||eventDate.isAfter(currentDate)) {
                    events.setEventDate(eventDate);
                    events.setEventTitle(rs.getString("eventTitle"));
                    events.setEventDescription(rs.getString("eventDescription"));
                    events.setEventVenue(rs.getString("eventVenue"));
                    events.setStartTime(rs.getTime("startTime").toLocalTime());
                    events.setEndTime(rs.getTime("endTime").toLocalTime());
                    eventList.offer(events);
                }
            }
            eventTF1.setText(getEventString(eventList.poll()));
            eventTF2.setText(getEventString(eventList.poll()));
            eventTF3.setText(getEventString(eventList.poll()));
            if(UserClass.getRole().equals("Educator")){
                System.out.println(UserClass.getRole());
                registerBTN1.setVisible(false);
                registerBTN2.setVisible(false);
                registerBTN3.setVisible(false);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String getEventString(Events event) {
        if(event != null){
            return event.toString();
        } else {
            return "" ;
        }
    }


    @FXML
    public void backBTN(ActionEvent actionEvent) {
        try {
            if(UserClass.getRole().equals("Educator")){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Educator.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                EducatorController educatorController = fxmlLoader.getController();
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) eventTF1.getScene().getWindow();
                currentStage.close();
            }
            else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Student.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                StudentController studentController = fxmlLoader.getController();
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) eventTF1.getScene().getWindow();
                currentStage.close();
            }
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
            Stage currentStage = (Stage) eventTF1.getScene().getWindow();
            currentStage.close();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
