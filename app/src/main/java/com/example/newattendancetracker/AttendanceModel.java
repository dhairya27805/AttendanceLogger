package com.example.newattendancetracker;

public class AttendanceModel {
    private int id;
    private String subject;
    private String status;
    private String date;

    public AttendanceModel(int id, String subject, String status, String date) {
        this.id = id;
        this.subject = subject;
        this.status = status;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }
}
