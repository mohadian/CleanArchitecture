package com.elvotra.clean.data.remote.model;

public class PostData {
    private int userId;

    private int id;

    private String title;

    private String body;

    public PostData(int userId, int id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}