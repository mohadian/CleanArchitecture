package com.elvotra.clean;

import com.elvotra.clean.domain.model.Comment;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.model.User;

import java.util.ArrayList;
import java.util.List;

public class DomainDataUtil {
    private static final String POST_TITLE = "title";
    private static final String POST_BODY = "body";
    private static final String USER_NAME = "name";
    private static final String USER_USERNAME = "username";
    private static final String USER_EMAIL = "user@test.com";
    private static final String COMMENT_USER_NAME = "comment_name";
    private static final String COMMENT_USER_EMAIL = "comment_email";
    private static final String COMMENT_BODY = "comment_body";

    public static List<Post> createPostList(int postCount, int commentsCount) {
        List<Post> posts = new ArrayList<>(postCount);
        for (int i = 0; i < postCount; i++) {
            User user = new User(i, USER_NAME, USER_USERNAME, USER_EMAIL);
            List<Comment> comments = new ArrayList<>(commentsCount);
            for (int j = 0; j < commentsCount; j++) {
                Comment comment = new Comment(i, j + (i * commentsCount), COMMENT_USER_NAME, COMMENT_USER_EMAIL, COMMENT_BODY);
                comments.add(comment);
            }
            final Post newPost = new Post(i, user, POST_TITLE, POST_BODY, comments);
            posts.add(newPost);
        }

        return posts;
    }
}
