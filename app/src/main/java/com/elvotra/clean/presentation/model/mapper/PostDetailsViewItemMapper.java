package com.elvotra.clean.presentation.model.mapper;

import com.elvotra.clean.domain.model.Comment;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.model.User;
import com.elvotra.clean.presentation.model.PostCommentViewItem;
import com.elvotra.clean.presentation.model.PostDetailsViewItem;
import com.elvotra.clean.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class PostDetailsViewItemMapper {

    private static PostDetailsViewItemMapper INSTANCE;

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
            String avatar = Constants.AVATAR_BASE_URL + ((comment.getEmail() != null) ? comment.getEmail() : "");
            PostCommentViewItem commentViewItem = new PostCommentViewItem(comment.getId(), comment.getName(), comment.getEmail(), avatar, comment.getBody());
            postCommentViewItems.add(commentViewItem);
        }
        User user = post.getUser();
        return transform(post, user, postCommentViewItems);
    }

    public PostDetailsViewItem transform(Post post, User user, List<PostCommentViewItem> commentViewItems) {
        String username = (user != null) ? user.getName() : "Unknown";
        String useremail = (user != null && user.getEmail() != null) ? user.getEmail() : "";
        String avatar = Constants.AVATAR_BASE_URL + ((user != null && user.getEmail() != null) ? user.getEmail() : "");

        return new PostDetailsViewItem(post.getId(), username, useremail, avatar, post.getTitle(), post.getBody(), commentViewItems);
    }

}
