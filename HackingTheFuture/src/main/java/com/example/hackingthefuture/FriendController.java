package com.example.hackingthefuture;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;

import java.awt.*;

import java.sql.*;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class FriendController {
    @FXML
    private ListView<FriendRequest> friendRequestsList;
    @FXML
    private Button acceptButton;
    @FXML
    private Button rejectButton;


    private String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
    private String SUser = "root";
    private String SPass = "";

    public void initialize() throws SQLException {
        loadFriendRequests();
    }
    private void loadFriendRequests() throws SQLException {
        ObservableList<FriendRequest> requests = FXCollections.observableArrayList();
        String username = UserClass.getUsername();

        try (
            Connection con = DriverManager.getConnection(SUrl, SUser, SPass)){

            String query = "SELECT Sender FROM  friendrequests WHERE Receiver = ? AND Status = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, "pending");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String sender = rs.getString("Sender");
                requests.add(new FriendRequest(sender));
            }
        }
         friendRequestsList.setItems(requests);
         friendRequestsList.setCellFactory(new Callback<ListView<FriendRequest>, ListCell<FriendRequest>>() {
             @Override
             public ListCell<FriendRequest> call(ListView<FriendRequest> param) {
                 return new ListCell<FriendRequest>() {
                     @Override
                     protected void updateItem(FriendRequest item, boolean empty) {
                         super.updateItem(item, empty);
                         if (item != null && !empty) {
                             setText(item.getSender() + " has requested to add you as friend!");
                             setGraphic(item.getCheckBox());
                         } else {
                             setText(null);
                             setGraphic(null);
                         }
                     }
                 };
             }
         });

    }

    @FXML
    private void acceptButtonClicked(ActionEvent event) throws SQLException {
        handleSelectedRequests("accepted");
        Function.success("Success", "YEAH", "You have accepted the request");
    }
    @FXML
    private void rejectButtonClicked(ActionEvent event) throws SQLException {
        handleSelectedRequests("rejected");
        Function.success("Rejected", "Oops", "You have rejected the request");
    }

    private void handleSelectedRequests(String status) throws SQLException {
        String username = UserClass.getUsername();
        ObservableList<FriendRequest> updatedRequests = FXCollections.observableArrayList(friendRequestsList.getItems());
        ObservableList<FriendRequest> requestsToRemove = FXCollections.observableArrayList();

        try (Connection con = DriverManager.getConnection(SUrl, SUser, SPass)) {
            String updateQuery = "UPDATE friendrequests SET Status = ? WHERE Sender = ? AND Receiver = ?";
            String insertQuery = "INSERT INTO friends (Sender, Receiver) VALUES (?, ?)";
            String deleteQuery = "DELETE FROM friendrequests WHERE Sender = ? AND Receiver = ?";

            for (FriendRequest request: friendRequestsList.getItems()) {
                if (request.getCheckBox().isSelected()) {
                    String sender = request.getSender();

                    if ("accepted".equals(status)) {
                    try (PreparedStatement psUpdate = con.prepareStatement(updateQuery);
                         PreparedStatement psInsert = con.prepareStatement(insertQuery)){
                        psUpdate.setString(1, status);
                        psUpdate.setString(2, sender);
                        psUpdate.setString(3, username);
                        psUpdate.executeUpdate();

                        psInsert.setString(1, sender);
                        psInsert.setString(2, username);
                        psInsert.executeUpdate();

                        psInsert.setString(1, username);
                        psInsert.setString(2, sender);
                        psInsert.executeUpdate();

                    }


                    } else if("rejected".equals(status)) {
                        try (PreparedStatement psDelete = con.prepareStatement(deleteQuery)) {
                            psDelete.setString(1, sender);
                            psDelete.setString(2, username);
                            psDelete.executeUpdate();

                        }
                    }requestsToRemove.add(request);
                    }
            }
        }
        updatedRequests.removeAll(requestsToRemove);
        friendRequestsList.setItems(updatedRequests);


    }

    public void onBackButtonClicked(ActionEvent event) {
        Node source = (Node) event.getSource();
        Function.nextPage("Student.fxml", source, "Student");
    }



}

