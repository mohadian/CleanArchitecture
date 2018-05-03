package com.elvotra.clean.presentation.model;

import com.google.common.base.Objects;

public class PostViewItem {
    private int id;

    private String user;

    private String useremail;

    private String avatar;

    private String title;

    private String body;

    private String commentsCount;

    public PostViewItem(int id, String user, String useremail, String avatar, String title, String body, String commentsCount) {
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

    public String getCommentsCount() {
        return commentsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostViewItem that = (PostViewItem) o;
        return id == that.id &&
                Objects.equal(user, that.user) &&
                Objects.equal(useremail, that.useremail) &&
                Objects.equal(avatar, that.avatar) &&
                Objects.equal(title, that.title) &&
                Objects.equal(body, that.body) &&
                Objects.equal(commentsCount, that.commentsCount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, user, useremail, avatar, title, body, commentsCount);
    }
}
