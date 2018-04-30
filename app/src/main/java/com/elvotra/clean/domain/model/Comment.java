package com.elvotra.clean.domain.model;

import com.google.common.base.Objects;

public class Comment {
    private int postId;
    private int id;
    private String name;
    private String email;
    private String body;

    public Comment(int postId, int id, String name, String email, String body) {
        this.postId = postId;
        this.id = id;
        this.name = name;
        this.email = email;
        this.body = body;
    }

    public int getPostId() {
        return postId;
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

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equal(id, comment.id) &&
                Objects.equal(postId, comment.postId) &&
                Objects.equal(name, comment.name) &&
                Objects.equal(email, comment.email) &&
                Objects.equal(body, comment.body);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, postId, name, email, body);
    }
}
