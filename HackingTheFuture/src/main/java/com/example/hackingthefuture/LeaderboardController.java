package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardController {

    public Connection con;
    public Label nameLabel;
    public Label pointsLabel;

    @FXML
    public void initialize() {
        List<Student> students = retrieveFromDatabase();

        bubbleSortStudents(students);

        StringBuilder sbName = new StringBuilder();
        StringBuilder sbPoints = new StringBuilder();
        int position = 1;
        for (Student student : students) {
            sbName.append(String.format("%-10d %-25s \n", position++, student.getUsername()));
            sbPoints.append(String.format("%15d \n", student.getPoints()));
        }
        nameLabel.setText(sbName.toString());
        pointsLabel.setText(sbPoints.toString());
    }

    private List<Student> retrieveFromDatabase() {
        List<Student> students = new ArrayList<>();
        try {
            String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
            String SUser = "root";
            String SPass = "";
            con = DriverManager.getConnection(SUrl, SUser, SPass);
            String query = "SELECT username, points, pointLastUpdated FROM user WHERE role = 'Young Student'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                int points = rs.getInt("points");
                Timestamp lastUpdatedTimestamp = rs.getTimestamp("pointLastUpdated");
                LocalDateTime lastUpdatedPoint;
                if (lastUpdatedTimestamp != null) {
                    lastUpdatedPoint = lastUpdatedTimestamp.toLocalDateTime();
                } else {
                    lastUpdatedPoint = null;
                }
                students.add(new Student(username, points, lastUpdatedPoint));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return students;
    }

    // Method to perform bubble sort
    private void bubbleSortStudents(List<Student> students) {
        for (int i = 0; i < students.size() - 1; i++) {
            for (int j = 0; j < students.size() - i - 1; j++) {
                if (compareStudents(students.get(j), students.get(j + 1)) > 0) {
                    Collections.swap(students, j, j + 1);
                }
            }
        }
    }

    // Method to compare two students based on points and last updated point
    private int compareStudents(Student s1, Student s2) {
        if (s1.getPoints() != s2.getPoints()) {
            return s2.getPoints() - s1.getPoints();
        } else {
            return s1.getLastUpdatedPoint().compareTo(s2.getLastUpdatedPoint());
        }
    }

    public void backBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Student.fxml", sourceNode, "Student");
    }

    public void viewProfileBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("ViewProfile.fxml", sourceNode, "ViewProfile");
    }
}