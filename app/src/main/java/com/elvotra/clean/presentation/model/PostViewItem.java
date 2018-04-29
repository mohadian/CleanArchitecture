package com.elvotra.clean.presentation.model;

public class PostViewItem {
    private int id;

    private String user;

    private String useremail;

    private String avatar;

    private String title;

    private String body;

    private int commentsCount;

    public PostViewItem(int id, String user, String useremail, String avatar, String title, String body, int commentsCount) {
        this.id = id;
        this.user = user;
        this.useremail = useremail;
        this.avatar = avatar;
        this.title = title;
        this.body = body;
        this.commentsCount = commentsCount;
    }

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getUseremail() {
        return useremail;
    }

    public String getAvatar() {
        return avatar;
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
