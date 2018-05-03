package com.elvotra.clean.presentation.model;

import com.google.common.base.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDetailsViewItem that = (PostDetailsViewItem) o;
        return id == that.id &&
                Objects.equal(user, that.user) &&
                Objects.equal(useremail, that.useremail) &&
                Objects.equal(avatar, that.avatar) &&
                Objects.equal(title, that.title) &&
                Objects.equal(body, that.body) &&
                Objects.equal(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, user, useremail, avatar, title, body, comments);
    }
}
