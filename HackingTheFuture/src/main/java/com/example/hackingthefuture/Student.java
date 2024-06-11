package com.example.hackingthefuture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Student {
    private String username;
    private int points;
    private LocalDateTime lastUpdatedPoint;

    public Student(String username, int points, LocalDateTime lastUpdatedPoint) {
        this.username = username;
        this.points = points;
        this.lastUpdatedPoint = lastUpdatedPoint;
    }

    public String getUsername() {
        return username;
    }

    public int getPoints() {
        return points;
    }

    public LocalDateTime getLastUpdatedPoint() {
        return lastUpdatedPoint != null ? lastUpdatedPoint : LocalDateTime.MIN;
    }


    @Override
    public String toString() {
        return String.format("%-40s %-5d", username, points);
    }
}

