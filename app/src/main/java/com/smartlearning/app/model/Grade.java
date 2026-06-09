package com.smartlearning.app.model;

public class Grade {
    private int id;
    private int subjectId;
    private int userId;
    private String subjectName;
    private double score;
    private String createdAt;

    public Grade() {}

    public Grade(int subjectId, int userId, String subjectName, double score) {
        this.subjectId = subjectId;
        this.userId = userId;
        this.subjectName = subjectName;
        this.score = score;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
