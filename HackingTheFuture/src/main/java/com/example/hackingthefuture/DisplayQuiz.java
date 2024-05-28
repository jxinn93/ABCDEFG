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
                linkPositions.add(new LinkPosition(quizLink, start, end));
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
                openLink(linkPosition.url);
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
        System.out.println("Executing filtered query: " + query);

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
                linkPositions.add(new LinkPosition(quizLink, start, end));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return s.toString();
    }


}


class LinkPosition {
    String url;
    int start;
    int end;

    LinkPosition(String url, int start, int end) {
        this.url = url;
        this.start = start;
        this.end = end;
    }
}
