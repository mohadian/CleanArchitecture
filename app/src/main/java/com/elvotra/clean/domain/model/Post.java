package com.elvotra.clean.domain.model;

public class Post {
    private int id;

    private User user;

    private String title;

    private String body;

    private int commentsCount;

    public Post(int id, User user, String title, String body, int commentsCount) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.body = body;
        this.commentsCount = commentsCount;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getCommentsCount() {
        return commentsCount;
    }
}
