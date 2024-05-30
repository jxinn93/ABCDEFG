package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DisplayQuiz {
    @FXML
    private TextArea display;
    @FXML
    private CheckBox science;
    @FXML
    private CheckBox technology;
    @FXML
    private CheckBox engineering;
    @FXML
    private CheckBox mathematics;


    private static final String DB_URL = "jdbc:mysql://localhost:3306/hackingthefuture";
    private static final String USER = "root";
    private static final String PASS = "";
    private final List<LinkPosition> linkPositions = new ArrayList<>();

    public void initialize() {
        display.setText(setDisplayAll());
        display.addEventFilter(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);

    }

    public String setDisplayAll() {
        Function.warning("Take Note",null, "You only have one attempt to take the quiz");
        StringBuilder s = new StringBuilder();
        linkPositions.clear();
        try {
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM createquiz");

            while (rs.next()) {
                String quizTitle = rs.getString("quizTitle");
                String quizDescription = rs.getString("quizDescription");
                String quizLink = rs.getString("quizLink");

                s.append(String.format("Quiz Title: %s\n", quizTitle));
                s.append(String.format("Quiz Description: %s\n", quizDescription));
                int start = s.length();
                s.append(String.format("Quiz Link: %s\n\n", quizLink));
                int end = start + quizLink.length();
                linkPositions.add(new LinkPosition(quizTitle, quizLink, start, end));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return s.toString();
    }

    private void handleMouseClick(MouseEvent mouseEvent) {

        int caretPosition = display.getCaretPosition();
        linkPositions.forEach(linkPosition -> {
            if (caretPosition >= linkPosition.start && caretPosition <= linkPosition.end) {

                try {
                    if (!isQuizCompleted(linkPosition.quizTitle)) {
                        markQuizCompleted(linkPosition.quizTitle);
                        awardPoints();
                        openLink(linkPosition.url);
                        Function.success("Success!",null, "2 marks have been rewarded");
                    } else {
                        System.out.println("You have completed this quiz already");
                        Function.warning("Failed",null, "You have already completed the quiz");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void openLink(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void backBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Student.fxml", sourceNode, "student");
    }

    public void viewProfileBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("ViewProfile.fxml", sourceNode, "Profile");
    }

    public void filterQuizzes(ActionEvent event) throws SQLException {
        List<String> selectedThemes = new ArrayList<>();
        if (science.isSelected()) selectedThemes.add("Science");
        if (technology.isSelected()) selectedThemes.add("Technology");
        if (engineering.isSelected()) selectedThemes.add("Engineering");
        if (mathematics.isSelected()) selectedThemes.add("Mathematics");

        if (selectedThemes.isEmpty()) {
            display.setText(setDisplayAll());
        } else {
            display.setText(setDisplayedFiltered(selectedThemes));
        }
        display.addEventFilter(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);
    }

    private String setDisplayedFiltered(List<String> selectedThemes) {
        StringBuilder s = new StringBuilder();
        linkPositions.clear();
        String themeFilter = selectedThemes.stream()
                .map(theme -> "'" + theme + "'")
                .collect(Collectors.joining(", "));
        String query = "SELECT * FROM createquiz WHERE selectedTheme IN (" + themeFilter + ")";

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String quizTitle = rs.getString("quizTitle");
                String quizDescription = rs.getString("quizDescription");
                String quizLink = rs.getString("quizLink");

                s.append(String.format("Quiz Title: %s\n", quizTitle));
                s.append(String.format("Quiz Description: %s\n", quizDescription));
                int start = s.length();
                s.append(String.format("Quiz Link: %s\n\n", quizLink));
                int end = start + quizLink.length();
                linkPositions.add(new LinkPosition(quizTitle, quizLink, start, end));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return s.toString();
    }

    private boolean isQuizCompleted(String quizTitle) throws SQLException {
        String username = UserClass.getUsername();
        String query = "SELECT status FROM quiz WHERE username = ? AND quizTitle = ?";

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, quizTitle);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("status");
            }
        }
        return false;
    }

    private void markQuizCompleted(String quizTitle) throws SQLException {
        String username = UserClass.getUsername();
        String queryCheck = "SELECT * FROM quiz WHERE username = ? AND quizTitle = ?";
        String queryInsert = "INSERT INTO quiz (username, quizTitle, status) VALUES (?, ?, TRUE)";
        String queryUpdate = "UPDATE quiz SET status = TRUE WHERE username = ? AND quizTitle = ?";

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement psCheck = con.prepareStatement(queryCheck);
             PreparedStatement psInsert = con.prepareStatement(queryInsert);
             PreparedStatement psUpdate = con.prepareStatement(queryUpdate)) {

            psCheck.setString(1, username);
            psCheck.setString(2, quizTitle);
            ResultSet rs = psCheck.executeQuery();
            if(rs.next()) {
                boolean status = rs.getBoolean("status");
                if (!status) {
                    psUpdate.setString(1, username);
                    psUpdate.setString(2, quizTitle);
                    psUpdate.executeUpdate();
                } else {
                    Function.warning("Failed",null, "You have already completed the quiz");
                }
            } else {
                psInsert.setString(1,username);
                psInsert.setString(2,quizTitle);
                psInsert.executeUpdate();
            }
        }

    }

    private void awardPoints() throws SQLException {
        String username = UserClass.getUsername();
        String query = "UPDATE user SET points = points + 2, pointLastUpdated=? WHERE username = ?";
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(query)) {
             LocalDateTime now = LocalDateTime.now();
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
             String formattedDateTime = now.format(formatter);
             ps.setString(1, formattedDateTime);
             ps.setString(2, username);
             ps.executeUpdate();
        }
    }
}


class LinkPosition {

    String url;
    String quizTitle;
    int start;
    int end;

    LinkPosition(String quizTitle, String url, int start, int end) {
        this.quizTitle = quizTitle;
        this.url = url;
        this.start = start;
        this.end = end;
    }

}

