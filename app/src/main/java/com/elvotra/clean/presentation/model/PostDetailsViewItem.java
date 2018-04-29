package com.elvotra.clean.presentation.model;

import java.util.List;

public class PostDetailsViewItem {
    private int id;

    private String user;

    private String useremail;

    private String avatar;

    private String title;

    private String body;

    private List<PostCommentViewItem> comments;

    public PostDetailsViewItem(int id, String user, String useremail, String avatar, String title, String body, List<PostCommentViewItem> comments) {
        this.id = id;
        this.user = user;
        this.useremail = useremail;
        this.avatar = avatar;
        this.title = title;
        this.body = body;
        this.comments = comments;
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

    public List<PostCommentViewItem> getComments() {
        return comments;
    }
}
