package com.example.seminarsystem;



import models.faculty;

public class Session {
    private static faculty currentFaculty;

    public static void setCurrentFaculty(faculty faculty) {
        currentFaculty = faculty;
    }

    public static faculty getCurrentFaculty() {
        return currentFaculty;
    }

    public static void clear() {
        currentFaculty = null;
    }
}
