package com.elvotra.clean.presentation.model.mapper;

import com.elvotra.clean.domain.model.Comment;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.model.User;
import com.elvotra.clean.presentation.model.PostCommentViewItem;
import com.elvotra.clean.presentation.model.PostDetailsViewItem;

import java.util.ArrayList;
import java.util.List;

public class PostDetailsViewItemMapper {

    private static PostDetailsViewItemMapper INSTANCE;
    private static final String AVATAR = "https://api.adorable.io/avatars/285/";

    public static PostDetailsViewItemMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostDetailsViewItemMapper();
        }
        return INSTANCE;
    }

    private PostDetailsViewItemMapper() {
    }

    public PostDetailsViewItem transform(Post post) {
        List<Comment> comments = post.getComments();
        List<PostCommentViewItem> postCommentViewItems = new ArrayList<>(comments.size());
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            String avatar = AVATAR + ((comment.getEmail() != null) ? comment.getEmail() : "");
            PostCommentViewItem commentViewItem = new PostCommentViewItem(comment.getId(), comment.getName(), comment.getEmail(), avatar, comment.getBody());
            postCommentViewItems.add(commentViewItem);
        }
        User user = post.getUser();
        return transform(post, user, postCommentViewItems);
    }

    public PostDetailsViewItem transform(Post post, User user, List<PostCommentViewItem> commentViewItems) {
        String username = (user != null) ? user.getName() : "Unknown";
        String useremail = (user != null && user.getEmail() != null) ? user.getEmail() : "";
        String avatar = AVATAR + ((user != null && user.getEmail() != null) ? user.getEmail() : "");

        return new PostDetailsViewItem(post.getId(), username, useremail, avatar, post.getTitle(), post.getBody(), commentViewItems);
    }

}
