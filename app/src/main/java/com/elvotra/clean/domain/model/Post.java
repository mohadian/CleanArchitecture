package com.elvotra.clean.domain.model;

import java.util.List;

public class Post {
    private int id;

    private User user;

    private String title;

    private String body;

    private List<Comment> comments;

    public Post(int id, User user, String title, String body, List<Comment> comments) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.body = body;
        this.comments = comments;
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

    public List<Comment> getComments() {
        return comments;
    }
}
