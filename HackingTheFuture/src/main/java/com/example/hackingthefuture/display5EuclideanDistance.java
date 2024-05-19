package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.io.IOException;

public class display5EuclideanDistance {

    @FXML
    private TextArea display;

    @FXML
    private Button back;

    @FXML
    private Button booking;

    private double coordinateX;
    private double coordinateY;



    public void setUserCoordinates(double coordinateX, double coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }


    public void launchTestDistance() {
        testDistance distance = new testDistance();
        display.setText(distance.initialize());
        //System.out.println(distances);
        //display.setText(distances);
    }

    public void displayDistances (String distances){
        display.setText(distances);
    }



    @FXML
    private void onBackButtonClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/hackingthefuture/Parent.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ParentController parentController = fxmlLoader.getController();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* public void displayDistance(List<EuclideanDistance.Destination> list) {
        StringBuilder s = new StringBuilder();

        if (list != null && !list.isEmpty()) {
            int count = Math.min(list.size(), 5);
            s.append("Suggested destinations:\n").append("===================================\n");
            for (int i = 0; i < count; i++) {
                EuclideanDistance.Destination distance = list.get(i);
                s.append(String.format("[%d] ", i + 1)).append(distance.getName()).append("\n");
            }
            display.setText(s.toString());
        } else {
            display.setText("Distance list is null. Cannot display distance.");
        }
    }
*/

    @FXML
    public void setBooking(ActionEvent event) throws IOException {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("/com/example/hackingthefuture/selectBooking.fxml", sourceNode, "Make a Booking");
    }


}


