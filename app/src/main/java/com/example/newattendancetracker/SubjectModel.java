package com.example.newattendancetracker;

public class SubjectModel {
    private String subjectName;
    private int totalClasses;
    private int presentClasses;
    private float percentage;

    public SubjectModel(String subjectName, int totalClasses, int presentClasses, float percentage) {
        this.subjectName = subjectName;
        this.totalClasses = totalClasses;
        this.presentClasses = presentClasses;
        this.percentage = percentage;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public int getPresentClasses() {
        return presentClasses;
    }

    public float getPercentage() {
        return percentage;
    }
}
