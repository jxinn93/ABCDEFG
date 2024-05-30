package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class DisplayBooking {
    @FXML
    private TextArea display;
    @FXML
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hackingthefuture";
    private static final String USER = "root";
    private static final String PASS = "";

   public void initialize(){
        display.setText(setDisplay());
    }
    public String setDisplay() {
       String username = UserClass.getUsername();
        StringBuilder s = new StringBuilder();

        try {
            Connection con = DriverManager.getConnection(DB_URL,USER,PASS);

            String query = "SELECT * FROM parent WHERE username = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int id = rs.getInt("userID");
                String destination = rs.getString("Destination");
                String bookingDate = rs.getString("TimeSlot");
                s.append(String.format("ID:  %d, Username: %s, Destination: %s, Booking date: %s\n", id, username, destination, bookingDate));
                System.out.println();

            }

            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s.toString();

    }

public void onBackButtonClicked(ActionEvent event) {
    Node source = (Node) event.getSource();
    Function.nextPage("Parent.fxml" , source, "Parent");
}
}
