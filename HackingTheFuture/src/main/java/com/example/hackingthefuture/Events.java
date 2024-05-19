package com.example.hackingthefuture;

import java.time.LocalDate;
import java.time.LocalTime;

public class Events implements Comparable<com.example.hackingthefuture.Events>{
    public LocalDate eventDate;
    public String eventTitle;
    public String eventDescription;
    public String eventVenue;
    public LocalTime startTime;
    public LocalTime endTime;

    public Events(){}

    public Events(LocalDate eventDate,String eventTitle, String eventDescription, String eventVenue, LocalTime startTime, LocalTime endTime){
        this.eventDate = eventDate;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventVenue = eventVenue;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDate getEventDate(){
        return this.eventDate;
    }

    public String getEventTitle(){
        return this.eventTitle;
    }

    public String getEventDescription(){
        return this.eventDescription;
    }

    public String getEventVenue(){
        return this.eventVenue;
    }

    public LocalTime getStartTime(){
        return this.startTime;
    }

    public LocalTime getEndTime(){
        return this.endTime;
    }

    public void setEventDate(LocalDate eventDate){
        this.eventDate = eventDate;
    }

    public void setEventTitle(String eventTitle){
        this.eventTitle = eventTitle;
    }

    public void setEventDescription(String eventDescription){
        this.eventDescription = eventDescription;
    }

    public void setEventVenue(String eventVenue){
        this.eventVenue = eventVenue;
    }

    public void setStartTime(LocalTime startTime){
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime){
        this.endTime = endTime;
    }

    @Override
    public int compareTo(com.example.hackingthefuture.Events other) {
        return this.eventDate.compareTo(other.eventDate);
    }

    @Override
    public String toString() {
        return "Event Date: " +getEventDate()+
                "\nEvent Title: "+getEventTitle()+
                "\nEvent Description: "+getEventDescription()+
                "\nEvent Venue: "+getEventVenue()+
                "\nEvent Time: "+getStartTime()+" - "+getEndTime();
    }
}
