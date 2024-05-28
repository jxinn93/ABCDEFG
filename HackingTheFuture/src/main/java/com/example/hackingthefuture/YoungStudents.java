package com.example.hackingthefuture;

import java.util.ArrayList;
import java.util.List;

public class YoungStudents {
    private String username ;
    private List<String> friends;
    private FriendRequest friendRequest;
    private List <String> receivedRequest;

    public YoungStudents(String username){
        this.username = username;
        this.friends = new ArrayList<String>();
        this.friendRequest = new FriendRequest();
        this.receivedRequest = new ArrayList<>();
    }

    public String getUsername(){
        return username;
    }
    public List<String> getFriends(){
        return friends;
    }

    public FriendRequest getFriendRequest(){
        return friendRequest;
    }

    public List<String> getReceivedRequest(){
        return receivedRequest;
    }

    public void sendFriendRequest(YoungStudents friend){
        this.friendRequest.sendFriendRequest(friend);
    }
    public void receivedFriendRequest(YoungStudents friend){
        this.receivedRequest.add(friend.getUsername());
    }

    public void acceptFriendRequest(String senderUsername){
        this.friendRequest.acceptFriendRequest(senderUsername);
    }
    public void removeFriend(String username){
        this.friends.remove(username);
    }


}
