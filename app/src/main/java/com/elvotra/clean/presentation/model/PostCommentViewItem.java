package com.elvotra.clean.presentation.model;

public class PostCommentViewItem {
    private int id;
    private String name;
    private String email;
    private String avatar;
    private String body;

    public PostCommentViewItem(int id, String name, String email, String avatar, String body) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getBody() {
        return body;
    }
}
