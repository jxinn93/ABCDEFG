package com.example.hackingthefuture;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class testDistance {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hackingthefuture";
    private static final String USER = "root";
    private static final String PASS = "";
    String username = UserClass.getUsername();

    public String initialize() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            if (connection != null) {
                System.out.println("Connected to the database!");

            } else {
                System.out.println("Failed to connect to the database.");
            }
            double[] userCoordinates = getUserCoordinates(connection, username);
            return calculateDistances(userCoordinates[0], userCoordinates[1]);

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String calculateDistances( double coordinateX, double coordinateY) throws SQLException {
        StringBuilder result = new StringBuilder();
        System.out.println(coordinateX + " " + coordinateY);
        List<EuclideanDistance.Destination> destinations = EuclideanDistance.readFromFile("C:/HackingTheFuture2.0/HackingTheFuture/src/main/resources/com/example/hackingthefuture/BookingDestination.txt");
        List<EuclideanDistance.Destination> sortedDestinations = EuclideanDistance.getTop5Destinations(destinations, coordinateX, coordinateY);
        for (int i = 0; i < Math.min(sortedDestinations.size(), 5); i++) {
            EuclideanDistance.Destination distance = sortedDestinations.get(i);
            result.append(String.format("[%d] ", i + 1)).append(distance.getName()).append("\n")
                    .append(String.format("   %.2f km away", distance.getDistance(coordinateX, coordinateY))).append("\n\n");
        }
        return result.toString();
    }

    public static double[] getUserCoordinates(Connection connection, String username) throws SQLException {

        double[] coordinates = new double[2];
        String query = "SELECT coordinateX, coordinateY FROM user WHERE username =  ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    coordinates[0] = rs.getDouble("coordinateX");

                    System.out.println(coordinates[0]);
                    coordinates[1] = rs.getDouble("coordinateY");

                    System.out.println(coordinates[1]);


                }
            }
        }
        return coordinates;
    }




    }


