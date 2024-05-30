package com.example.hackingthefuture;

import javafx.scene.control.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class FriendRequest {
    private String sender;
    private CheckBox checkBox;

    public FriendRequest(String sender) {
        this.sender = sender;
        this.checkBox = new CheckBox();
    }
    public String getSender() {
        return sender;
    }
    public CheckBox getCheckBox() {
        return checkBox;
    }
}
