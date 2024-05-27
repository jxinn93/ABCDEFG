package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class CreateEventController {
    @FXML
    public TextField eventTitleTF;
    public TextField eventDescriptionTF;
    public TextField eventVenueTF;
    public DatePicker eventDateTF;
    public TextField startTimeTF;
    public TextField endTimeTF;
    public Connection con;

    @FXML
    public void initialize() {
        try {
            String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
            String SUser = "root";
            String SPass = "";
            con = DriverManager.getConnection(SUrl, SUser, SPass);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewProfileBTN(ActionEvent actionEvent) {
        Node sourceNode = (Node) actionEvent.getSource();
        Function.nextPage("viewProfile.fxml", sourceNode, "Profile");
    }

    @FXML
    public void backBTN(ActionEvent actionEvent) {
        Node sourceNode = (Node) actionEvent.getSource();
        Function.nextPage("Educator.fxml", sourceNode, "Educator");
    }

    @FXML
    public void saveBTN(ActionEvent actionEvent) {
        String username=UserClass.getUsername();
        String eventTitle = eventTitleTF.getText();
        String eventDescription = eventDescriptionTF.getText();
        String eventVenue = eventVenueTF.getText();
        LocalDate eventDate =eventDateTF.getValue();
        LocalTime startTime;
        LocalTime endTime;
        try {
            startTime = LocalTime.parse(startTimeTF.getText());
            endTime = LocalTime.parse(endTimeTF.getText());
        }
        catch (DateTimeParseException e) {
            Alert.showAlert("Please enter a valid Event Time!", "Error");
            return; // Exit the method since the time is not valid
        }
        try{
            if(eventTitle.isEmpty()){
                Alert.showAlert("Event Title is required!", "Error");
            }
            else if (eventDescription.isEmpty()) {
                Alert.showAlert("Event Description is required!", "Error");
            }
            else if (eventVenue.isEmpty()) {
                Alert.showAlert("Event Venue is required!", "Error");
            }
            else if(eventDate==null){
                Alert.showAlert("Please select Event Date!", "Error");
            }
            else{
                int numberOfEvent=1;
                String query = "SELECT * FROM createevent WHERE username=?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    numberOfEvent = rs.getInt("numberOfEvent")+1;
                }

                query = "INSERT INTO createevent (username,eventTitle,eventDescription,eventVenue,eventDate,startTime,endTime,numberOfEvent) VALUES (?,?,?,?,?,?,?,?)";
                ps = con.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, eventTitle);
                ps.setString(3, eventDescription);
                ps.setString(4, eventVenue);
                ps.setDate(5, java.sql.Date.valueOf(eventDate));
                ps.setTime(6, java.sql.Time.valueOf(startTime));
                ps.setTime(7, java.sql.Time.valueOf(endTime));
                ps.setInt(8, numberOfEvent);
                ps.executeUpdate();
                eventTitleTF.setText("");
                eventDescriptionTF.setText("");
                eventVenueTF.setText("");
                eventDateTF.setValue(null);
                startTimeTF.setText(null);
                endTimeTF.setText(null);
                Alert.showDialog("Save Successfully!", "Success");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());;
        }
    }
}


