package com.example.hackingthefuture;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookingController {
    @FXML
    private Button back;

    @FXML
    private javafx.scene.control.TextArea distance;

    @FXML

    private javafx.scene.control.TextField id;
    private List<EuclideanDistance.Destination> sortedDestinations = new ArrayList<>();
    private EuclideanDistance.Destination selectedDestination;
    private String destinationName;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hackingthefuture";
    private static final String USER = "root";
    private static final String PASS = "";
    private List<String> childrenList;

    public void setSelectedDestination(EuclideanDistance.Destination selectedDestination) {
        this.selectedDestination = selectedDestination;
    }


    public void initialize(){
        id.setOnAction(this::handleEnterPressed);
        fetchChildrenList();
    }
    private void fetchChildrenList(){
        ParentController parentController = new ParentController();
        childrenList = parentController.getChildrenUsernames();
    }


    @FXML
    public void destination(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/hackingthefuture/5Shortest.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            display5EuclideanDistance display5EuclideanDistance = fxmlLoader.getController();
            testDistance distanceCalculator = new testDistance();
            String distances = distanceCalculator.initialize();
            display5EuclideanDistance.displayDistances(distances);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
        }






    @FXML
    private void handleEnterPressed(ActionEvent events) {
        String ID = id.getText();
        if (ID.isEmpty()) {
            Function.warning("Invalid ID", null, "Please fill in the ID");
            return;
        }

        int newid;
        try {
            newid = Integer.parseInt(ID);
        } catch (NumberFormatException e) {
            Function.warning("Invalid ID", null, "Please enter a valid number");
            return;
        }

        if (newid <= 0 || newid > 5) {
            Function.warning("Invalid ID", null, "Please enter the ID within the range of 1 to 5");
            return;
        }
        testDistance distanceCalculator2 = new testDistance();
        String distances = distanceCalculator2.initialize();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            double[] userCoordinates = distanceCalculator2.getUserCoordinates(connection, UserClass.getUsername());
            sortedDestinations = EuclideanDistance.getTop5Destinations(
                    EuclideanDistance.readFromFile("C:/HackingTheFuture2.0/HackingTheFuture/src/main/resources/com/example/hackingthefuture/BookingDestination.txt"),
                    userCoordinates[0], userCoordinates[1]
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Handling enter pressed, sortedDestinations: " + sortedDestinations);
        if (!sortedDestinations.isEmpty()) {
            EuclideanDistance.Destination selectedDestination = sortedDestinations.get(newid - 1);
            destinationName = selectedDestination.getName();
            System.out.println(destinationName);

        // Display the selected destination details

            StringBuilder s = new StringBuilder();
            s.append("Enter destination ID for booking: ").append(ID).append("\n===================================\n");
            s.append("Selected booking for: ").append(selectedDestination.getName()).append("\n\n");
            s.append("Available Time Slots: \n");

            // Retrieve and display available time slots for the destination
            List<String> availableTimeSlots = getAvailableTimeSlots(selectedDestination);
            for (String timeSlot : availableTimeSlots) {
                s.append(timeSlot).append("\n");
            }




            distance.setText(s.toString());
        } else {
            distance.setText("No destinations available to display.");
        }
    }

    // Method to retrieve available time slots for the selected destination
    public List<String> getAvailableTimeSlots(EuclideanDistance.Destination destination) {
        List<String> timeSlots = new ArrayList<>();

        // Get current date
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.plusDays(1); // Start from the next day
        LocalDate endDate = startDate.plusDays(6); // Seven days from the start date

        // Format for displaying dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<LocalDate> userSchedule = new ArrayList<>();

        if (!childrenList.isEmpty()) {

            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
                for (String child : childrenList) {
                    String query = "SELECT eventDate FROM student WHERE username= ?";
                    try (PreparedStatement ps = connection.prepareStatement(query)) {
                        ps.setString(1, child);
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                LocalDate eventDate = rs.getDate("eventDate").toLocalDate();
                                userSchedule.add(eventDate);
                            }
                        }
                    }
                }


                // Generate time slots for each day starting from the next day up to seven days later
                while (startDate.isBefore(endDate.plusDays(1))) {

                    // Check if the current date is available
                    if (!userSchedule.contains(startDate)) {
                        timeSlots.add("[" + (timeSlots.size() + 1) + "] " + startDate.format(formatter));
                    }
                    startDate = startDate.plusDays(1); // Move to the next day
                }
                return timeSlots;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Function.warning("Children Not Found", null, "Failed to connect with your children. Please confirm your kids' username at your profile first.");
        }

        return timeSlots;
    }

    @FXML
    public void confirmBook(ActionEvent event) throws IOException {
        Node sourceNode = (Node) event.getSource();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("timeSlot.fxml")); // Load the FXML file directly
        Parent root = loader.load();

        // Get the stage from the sourceNode (usually a button)
        Stage stage = (Stage) sourceNode.getScene().getWindow();

        // Create a new scene and set it in the stage
        Scene scene = new Scene(root);
        stage.setTitle("Confirm Booking");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // Pass data to the controller if needed
        timeSlotController controller = loader.getController();
        controller.setAvailableTimeSlots(getAvailableTimeSlots(selectedDestination));
        controller.setDestination(destinationName);
    }
}




