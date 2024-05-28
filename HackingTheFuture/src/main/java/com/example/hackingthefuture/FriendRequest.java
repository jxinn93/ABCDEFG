package com.example.hackingthefuture;

import java.util.ArrayList;
import java.util.List;

public class FriendRequest  {
    private List<String> sentRequests;
    private List<String> receivedRequests;
    private List<String> friends;

    public FriendRequest(){
        this.sentRequests=new ArrayList<>();
        this.receivedRequests = new ArrayList<>();
        this.friends = new ArrayList<>();
    }

    public void sendFriendRequest(YoungStudents friend){//check if friend is already in the friend list or if request has been sent before
        if(this.friends.contains(friend.getUsername())){
            System.out.println("Friend is already in your friend list.");
        }
        else if(sentRequests.contains(friend.getUsername())){
            System.out.println("Friend request has already been sent to this user");
        }
        else{
            System.out.println("Friend request send to "+friend.getUsername());
        }
    }

    public void receiveFriendRequest(YoungStudents sender){//Receive and manage a friend request
        this.receivedRequests.add(sender.getUsername());
        System.out.println("Received friend request from: "+sender.getUsername());
    }

    public void acceptFriendRequest(String senderUsername){//Accept a friend request
        if(this.receivedRequests.contains(senderUsername)){
            this.friends.add(senderUsername);
            this.receivedRequests.remove(senderUsername);
            System.out.println("Friend request from "+senderUsername+" accepted.");
        }
    }
}