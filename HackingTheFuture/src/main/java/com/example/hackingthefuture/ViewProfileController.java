package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewProfileController {
    @FXML
    public Label nameLabel;
    public Label roleLabel;
    public Label emailLabel;
    public Label coordinateLabel;
    public Label label4;
    public Label label2R;
    public Label label3;


    public Connection con;
    private List<FamilyMember> parentsList = ParentChild.parentsList;
    private String nameSearch;

    @FXML
    public void initialize() {
        System.out.println("Initializing ViewProfileController...");
        ParentChild.initialize();
        System.out.println(parentsList);
        String username = UserClass.getUsername();
        String email = UserClass.getEmail();
        Double coordinateX = UserClass.getCoordinateX();
        Double coordinateY = UserClass.getCoordinateY();
        String role = UserClass.getRole();
        String profileUsername = "";
        profile(username, email, coordinateX, coordinateY, role, profileUsername);
    }

    public List<String> getChildrenUsernames() {
        String targetUsername = UserClass.getUsername();
        for (FamilyMember parent : parentsList) {
            if (parent.getUsername().equals(targetUsername)) {
                List<Child> children = parent.getChildren();
                List<String> childrenUsernames = new ArrayList<>();
                for (Child child : children) {
                    childrenUsernames.add(child.getUsername());
                }
                return childrenUsernames;
            }
        }
        return new ArrayList<>();
    }



    public void profile(String username, String email, Double coordinateX, Double coordinateY, String role, String profileUsername) {

        String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
        String SUser = "root";
        String SPass = "";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(SUrl, SUser, SPass);
            if (con != null) {
                System.out.println("Connected to the database!");

            } else {
                System.out.println("Failed to connect to the database.");
            }


            System.out.println("Role: " + role);

            //set labels
            nameLabel.setText("Username: " + (username != null ? username : ""));

            emailLabel.setText("Email: " + (email != null ? email : ""));

            if (coordinateX != null && coordinateY != null) {
                String formattedCoordinate = String.format("Coordinate: %.2f, %.2f", coordinateX, coordinateY);
                coordinateLabel.setText(formattedCoordinate);
            } else {
                coordinateLabel.setText("");
            }
            roleLabel.setText("Role: " + (role != null ? role : ""));


            switch (role) {
                case "Educator":
                    int numberOfEvent = 0;
                    int numberOfQuiz = 0;

                    String query = "SELECT COUNT(*) AS numberOfEvent FROM createevent WHERE username=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, username);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        numberOfEvent = rs.getInt("numberOfEvent");
                    }
                    System.out.println("Number of events created: " + numberOfEvent);
                    label3.setText("Number of events created: " + numberOfEvent);

                    query = "SELECT COUNT(*) AS numberOfQuiz FROM createquiz WHERE username=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, username);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        numberOfQuiz = rs.getInt("numberOfQuiz");
                    }
                    System.out.println("Number of quizzes created: " + numberOfQuiz);
                    label4.setText("Number of quiz created: " + numberOfQuiz);

                    label2R.setVisible(false);
                    break;

                case "Parent":
                    label2R.setVisible(false);
                    label4.setVisible(false);
                    String loggedInUsername = UserClass.getUsername();
                    String targetUsername = (profileUsername != null && !profileUsername.isEmpty()) ? profileUsername : loggedInUsername;

                    FamilyMember loggedInParent = null;
                    for (FamilyMember parent : parentsList){
                        if(parent.getUsername().equals(targetUsername)){
                            loggedInParent = parent;
                            break;
                        }
                    }

                    if(loggedInParent != null) {
                        StringBuilder childrenList = new StringBuilder();
                        List<Child> children = loggedInParent.getChildren();
                        for (int i = 0; i < children.size(); i++) {
                            childrenList.append(children.get(i).getUsername());
                            if (i < children.size() - 1) {
                                childrenList.append(", ");
                            }

                        }
                        label3.setText("Children: " + childrenList.toString());
                    } else {
                        label3.setText("No children found");

                    }

                    break;

                case "Young Student":
                    int points = 0;
                    query = "SELECT * FROM user WHERE username=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, username);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        points = rs.getInt("points");
                    }

                    StringBuilder showParent = new StringBuilder();
                    for (FamilyMember parent : parentsList) {
                        for(Child child : parent.getChildren()){
                            if(child.getUsername().equals(username)){
                                showParent.append(parent.getUsername()).append(", ");
                                break;
                            }
                        }
                    }
                    if(showParent.length() > 0){
                        showParent.deleteCharAt(showParent.length() - 2);
                    }
                    label2R.setVisible(true);
                    label2R.setText("Points: " + points);
                    label3.setText("Parents: " + (showParent.length()>0 ? showParent.toString(): "No Parents Found"));
                    label4.setText("Friends: ");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void backBTN(ActionEvent actionEvent) {
        String role = UserClass.getRole();

        String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
        String SUser = "root";
        String SPass = "";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(SUrl, SUser, SPass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (con != null) {
            System.out.println("Connected to the database!");

        } else {
            System.out.println("Failed to connect to the database.");
        }

        switch (role) {

            case "Educator":
                Node sourceNode = (Node) actionEvent.getSource();
                Function.nextPage("Educator.fxml",sourceNode,"Educatr");

                break;
            case "Parent":
                sourceNode = (Node) actionEvent.getSource();
                Function.nextPage("Parent.fxml",sourceNode,"Parent");
                break;

            case "Young Student":
               sourceNode = (Node) actionEvent.getSource();
                Function.nextPage("Student.fxml",sourceNode,"Student");
                break;

        }
    }
}
