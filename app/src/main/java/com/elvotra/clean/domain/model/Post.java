package com.elvotra.clean.domain.model;

import com.google.common.base.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equal(id, post.id) &&
                Objects.equal(user, post.user) &&
                Objects.equal(title, post.title) &&
                Objects.equal(body, post.body) ;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, user, title, body);
    }
}
