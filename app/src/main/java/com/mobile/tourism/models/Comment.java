package com.mobile.tourism.models;

public class Comment {
    private Long id;
    private String text;
    private Integer placeId;
    private String email;
    private int upvotes;
    private int downvotes;

    public Comment(Long id, String text, Integer placeId, int upvotes, int downvotes) {
        this.id = id;
        this.text = text;
        this.placeId = placeId;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getters and setters
} 