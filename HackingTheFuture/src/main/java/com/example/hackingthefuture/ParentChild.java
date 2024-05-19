package com.example.hackingthefuture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParentChild {

    public static List<FamilyMember> parentsList = new ArrayList<>();

        public static void initialize() {
        try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/SCSM11/Documents/ParentChild.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String parentName = parts[0].trim();
                String childName = parts[1].trim();

                FamilyMember parent = findOrCreate(parentsList, parentName);
                Child child = new Child(childName, parent);
                parent.addChild(child);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        }
        private static FamilyMember findOrCreate (List <FamilyMember> parentsList, String parentName){
            for (FamilyMember x : parentsList) {
                if (x.getUsername().equals(parentName)) {
                    return x;
                }
            }
            FamilyMember newParent = new FamilyMember(parentName);
            parentsList.add(newParent);
            return newParent;
        }

    }


class FamilyMember {
    private String username;
    private List<Child> children;

    public FamilyMember(String username) {
        this.username = username;
        this.children = new ArrayList<>();
    }

    public void addChild(Child child){
        children.add(child);
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public List<Child> getChildren(){
        return children;
    }
}

class Child {
    private String username;
    private FamilyMember parent;

    public Child(String username, FamilyMember parent) {
        this.username = username;
        this.parent = parent;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public FamilyMember getParent() {
        return parent;
    }
}
