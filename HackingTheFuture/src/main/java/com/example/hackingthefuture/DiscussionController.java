package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class DiscussionController {


    @FXML
    public TextField message;
    public TextArea display;


    private static final String DB_URL = "jdbc:mysql://localhost:3306/hackingthefuture";
    private static final String USER = "root";
    private static final String PASS = "";
    public String username = UserClass.getUsername();

    @FXML
    public void initialize() {
        try {
            setDisplay();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Insert(ActionEvent event) throws IOException, SQLException {
        if (message.getText() != null && !message.getText().trim().isEmpty()) {
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "INSERT INTO discussion (username, Content) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, message.getText());
            ps.executeUpdate();
            con.close();
            display.appendText(username + ":  " + message.getText()+"\n");
            message.clear();
        }

    }

    public void setDisplay()throws SQLException {
        Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
        String query = "SELECT username, Content, Date FROM discussion ORDER BY Date DESC";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        StringBuilder s = new StringBuilder();
        while(rs.next()) {
            Timestamp ts = rs.getTimestamp("Date");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String formattedDate = sdf.format(ts);
            s.append(rs.getString("username") + ": " + rs.getString("Content") + "      " +formattedDate + "\n\n");
        }
        display.setText(s.toString());
        con.close();

    }

    public void onBackButtonClicked(ActionEvent event) {
        String role = UserClass.getRole();
        Connection con = null;
        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        switch (role) {

            case "Educator":
                Node sourceNode = (Node) event.getSource();
                Function.nextPage("Educator.fxml", sourceNode, "Educatr");

                break;
            case "Parent":
                sourceNode = (Node) event.getSource();
                Function.nextPage("Parent.fxml", sourceNode, "Parent");
                break;

            case "Young Student":
                sourceNode = (Node) event.getSource();
                Function.nextPage("Student.fxml", sourceNode, "Student");
                break;

        }
    }
}



