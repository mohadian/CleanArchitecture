package com.elvotra.clean.presentation.model.mapper;

import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.model.User;
import com.elvotra.clean.presentation.model.PostViewItem;
import com.elvotra.clean.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class PostViewItemMapper {

    private static PostViewItemMapper INSTANCE;

    public static PostViewItemMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostViewItemMapper();
        }
        return INSTANCE;
    }

    private PostViewItemMapper() {
    }

    public List<PostViewItem> transform(List<Post> posts) {
        List<PostViewItem> postViewItems = new ArrayList<>(posts.size());
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            postViewItems.add(transform(post, post.getUser()));
        }
        return postViewItems;
    }

    public PostViewItem transform(Post post, User user) {
        String username = (user != null) ? user.getName() : "Unknown";
        String useremail = (user != null && user.getEmail() != null) ? user.getEmail() : "";
        String avatar = Constants.AVATAR_BASE_URL + ((user != null && user.getEmail() != null) ? user.getEmail() : "");
        String comments = (post.getComments() != null && post.getComments().size() > 0) ? String.valueOf(post.getComments().size()) : "";

        return new PostViewItem(post.getId(), username, useremail, avatar, post.getTitle(), post.getBody(), comments);
    }

}
