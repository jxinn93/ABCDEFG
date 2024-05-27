package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class timeSlotController {
    @FXML
    private Button back;

    @FXML
    private Button confirmBooking;

    @FXML
    private TextField time;

    private List<String> availableTimeSlots ;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hackingthefuture";
    private static final String USER = "root";
    private static final String PASS = "";
    public String username = UserClass.getUsername();
    public String destination = "";


    public void setAvailableTimeSlots(List<String> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;


    }
    public List <String> getAvailableTimeSlots() {
        return availableTimeSlots;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getDestination() {
        return destination;
    }





    @FXML
    public void backBooking(ActionEvent event) throws IOException {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("selectBooking.fxml",sourceNode,"Back");
    }


    @FXML
    private void confirmBookingAction(ActionEvent event) throws SQLException {
        // Get the input text
        String inputText = time.getText();

        // Validate the input text
        if (inputText.isEmpty()) {
            Function.warning("Invalid Input", null, "Please fill in the input field");
            return;
        }

        // Convert input to integer
        int selectedId;
        try {
            selectedId = Integer.parseInt(inputText);
        } catch (NumberFormatException e) {
            Function.warning("Invalid ID", null, "Please enter a valid number");
            return;
        }
        if (availableTimeSlots == null || availableTimeSlots.isEmpty()) {
            Function.warning("No Time Slots", null, "Time slots are not available.");
            return;
        }
        // Check if the selected ID is within the range of available time slots

        if (selectedId < 1 || selectedId > availableTimeSlots.size()) {
            Function.warning("Invalid ID", null, "Please enter a valid ID");
            return;
        }

        // Get the selected time slot based on the ID
        String selectedTimeSlot = availableTimeSlots.get(selectedId - 1);

        Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
        String checkQuery = "SELECT * FROM parent WHERE username = ? AND TimeSlot = ?";
        PreparedStatement psCheck = con.prepareStatement(checkQuery);
        psCheck.setString(1, username);
        psCheck.setString(2, selectedTimeSlot);
        ResultSet rsCheck = psCheck.executeQuery();

        if (rsCheck.next()) {
            Function.warning("Booking Failed", null, "You are already booking for another event on this date.");
        } else {
            // Process the selected time slot here, such as booking the appointment
            showAlert("Selected time slot", "Selected time slot: " + selectedTimeSlot + "\nBooking successfully!! Enjoy your trip!!");
            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String query = "INSERT INTO parent (username, Destination, TimeSlot) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    ;
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, destination);
                    preparedStatement.setString(3, selectedTimeSlot);
                    preparedStatement.executeUpdate();

                }

            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
