package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    private Events event1;
    private Events event2;
    private Events event3;

    @FXML
    public void initialize() {
        try {
            String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
            String SUser = "root";
            String SPass = "";
            con = DriverManager.getConnection(SUrl, SUser, SPass);
            String username = UserClass.getUsername();
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

            event1 = eventList.poll();
            event2 = eventList.poll();
            event3 = eventList.poll();

            eventTF1.setText(getEventString(event1));
            eventTF2.setText(getEventString(event2));
            eventTF3.setText(getEventString(event3));

            if(UserClass.getRole().equals("Educator") || UserClass.getRole().equals("Parent")){
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
        if(UserClass.getRole().equals("Educator")){
            Node sourceNode = (Node) actionEvent.getSource();
            Function.nextPage("Educator.fxml", sourceNode, "Educator");
        }
        else if(UserClass.getRole().equals("Parent")){
            Node sourceNode = (Node) actionEvent.getSource();
            Function.nextPage("Parent.fxml", sourceNode, "Parent");
        }
        else {
            Node sourceNode = (Node) actionEvent.getSource();
            Function.nextPage("Student.fxml", sourceNode, "Student");
        }
    }

    public void viewProfileBTN(ActionEvent actionEvent) {
        Node sourceNode = (Node) actionEvent.getSource();
        Function.nextPage("viewProfile.fxml", sourceNode, "Profile");
    }

    public void Register(ActionEvent event) throws SQLException {
        String username = UserClass.getUsername();
        String eventTitle = null;
        LocalDate eventDate = null;

        Events events = new Events();
        if (event.getSource() == registerBTN1&& event1 != null ) {
            eventTitle = event1.getEventTitle();
            eventDate = event1.getEventDate();
        } else if (event.getSource() == registerBTN2 && event2 != null) {
            eventTitle = event2.getEventTitle();
            eventDate = event2.getEventDate();
        } else if (event.getSource() == registerBTN3 && event3 != null) {
            eventTitle = event3.getEventTitle();
            eventDate = event3.getEventDate();
        }
        if (eventTitle == null || eventDate == null) {
            Function.warning("Registration Failed", null, "Event details are not available.");
            return;
        }

        System.out.println("Registering user: " + username + " for event: " + eventTitle + " on date: " + eventDate);

        String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
        String SUser = "root";
        String SPass = "";
        try {
            con = DriverManager.getConnection(SUrl, SUser, SPass);
            String checkQuery = "SELECT * FROM student WHERE username = ? AND eventDate = ?";
            PreparedStatement psCheck = con.prepareStatement(checkQuery);
            psCheck.setString(1, username);
            psCheck.setDate(2, Date.valueOf(eventDate));
            ResultSet rsCheck = psCheck.executeQuery();

            if (rsCheck.next()) {
                Function.warning("Registration Failed", null, "You are already registered for another event on this date.");
            } else {
                String query = "SELECT * FROM createevent WHERE eventTitle = ? AND eventDate = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, eventTitle);
                ps.setDate(2, Date.valueOf(eventDate));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    query = "INSERT INTO student (username, registerEvent, eventDate) VALUES (?, ?, ?)";
                    ps = con.prepareStatement(query);
                    ps.setString(1, username);
                    ps.setString(2, eventTitle);
                    ps.setDate(3, Date.valueOf(eventDate));
                    ps.executeUpdate();
                    Function.success("Registration Successfull", null, "Successfully register. 5 points are rewarded.");

                    String queryUser = "UPDATE user SET points = points + ?, pointLastUpdated=? WHERE username = ? ";
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = now.format(formatter);
                    ps = con.prepareStatement(queryUser);
                    ps.setInt(1, 5);
                    ps.setString(2, formattedDateTime);
                    ps.setString(3, username);
                    ps.executeUpdate();

                } else {
                    Function.warning("Registration Failed", null, "You have failed to register for the event.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
