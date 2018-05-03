package com.elvotra.clean.presentation.model;

import com.google.common.base.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostCommentViewItem that = (PostCommentViewItem) o;
        return id == that.id &&
                Objects.equal(name, that.name) &&
                Objects.equal(email, that.email) &&
                Objects.equal(avatar, that.avatar) &&
                Objects.equal(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, email, avatar, body);
    }
}
