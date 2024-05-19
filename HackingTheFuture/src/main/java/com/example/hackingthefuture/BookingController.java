package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingController {
    @FXML
    private Button back;

    @FXML
    private TextArea distance;

    @FXML

    private javafx.scene.control.TextField id;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hackingthefuture";
    private static final String USER = "root";
    private static final String PASS = "";

    private static List<EuclideanDistance.Destination> destinationsList = new ArrayList<>();

    public static void setDestinationsList(List<EuclideanDistance.Destination> list) {
        destinationsList = list;
    }

    public static List<EuclideanDistance.Destination> getDestinationsList() {
        return destinationsList;
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

    /*private void displayDistance(List<EuclideanDistance.Destination> list, double userx, double usery) {
        StringBuilder s = new StringBuilder();

        if (list != null && !list.isEmpty()) {
            int count = Math.min(list.size(), 5);
            s.append("Suggested destinations:\n").append("===================================\n");
            for (int i = 0; i < count; i++) {
                EuclideanDistance.Destination distance = list.get(i);
                s.append(String.format("[%d] ", i + 1)).append(distance.getName()).append("\n");
            }
            distance.setText(s.toString());
        } else {
            distance.setText("Distance list is null. Cannot display distance.");
        }
    }
     */

    public List<EuclideanDistance.Destination> getDestinationsListFromDatabase(){
        List<EuclideanDistance.Destination> destinationsList = getDestinationsListFromDatabase();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            String query = "SELECT coordinateX, coordinateY FROM user WHERE userID =  ?" ;
            try(PreparedStatement ps = connection.prepareStatement(query)){

                try (ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        double x = rs.getDouble("coordinateX");
                        System.out.println(x);
                        double y = rs.getDouble("coordinateY");
                        System.out.println(y);
                        destinationsList.add(new EuclideanDistance.Destination("", x , y));
                    }

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return destinationsList;
    }


    @FXML
    void login(ActionEvent event) throws IOException, MessagingException {
        String ID = id.getText();
        if (ID.isEmpty()) {
            Function.warning("Invalid ID", null, "Please fill in the ID");
        }

        int newid = Integer.parseInt(ID);
        if (newid <= 0 || newid > 5)
            Function.warning("Invalid ID", null, "Please enter the ID within the list");
    }
        }




