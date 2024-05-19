package com.example.hackingthefuture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EuclideanDistance {
    public static List<Destination> readFromFile(String filePath) {
        List<Destination> path = new ArrayList<>();
        // Check if the file exists
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return path;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String name = line.trim();
                String location = br.readLine().trim();
                br.readLine();  // Skip the delimiter line
                String[] parts = location.split(",");
                double x = Double.parseDouble(parts[0].trim());
                double y = Double.parseDouble(parts[1].trim());
                path.add(new Destination(name, x, y));
            }
        } catch (IOException e) {
            System.out.println("Problem with file output");
        }
        return path;
    }

    public static List<Destination> getTop5Destinations(List<Destination> destinations, double x, double y) {
        // Sort destinations based on distance
        destinations.sort(Comparator.comparingDouble(d -> d.getDistance(x, y)));

        // Get top 5 destinations
        return destinations.stream().limit(5).collect(Collectors.toList());
    }

    public static class Destination {
        String name;
        double x;
        double y;

        public Destination(String name, double x, double y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        public String getName() {
            return name;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getDistance(double x, double y) {
            double dx = this.x - x;
            double dy = this.y - y;
            return Math.sqrt(dx * dx + dy * dy) / 1000.0; // Convert to kilometers
        }
    }
}


