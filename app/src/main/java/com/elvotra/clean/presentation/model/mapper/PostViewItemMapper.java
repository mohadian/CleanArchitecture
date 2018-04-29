package com.elvotra.clean.presentation.model.mapper;

import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.model.User;
import com.elvotra.clean.presentation.model.PostViewItem;

import java.util.ArrayList;
import java.util.List;

public class PostViewItemMapper {

    private static PostViewItemMapper INSTANCE;
    private static final String AVATAR = "https://api.adorable.io/avatars/285/";

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
            postViewItems.add(transform(posts.get(i), posts.get(i).getUser()));
        }
        return postViewItems;
    }

    public PostViewItem transform(Post post, User user) {
        String username = (user != null) ? user.getName() : "Unknown";
        String useremail = (user != null && user.getEmail() != null) ? user.getEmail() : "";
        String avatar = AVATAR + ((user != null && user.getEmail() != null) ? user.getEmail() : "");

        PostViewItem postViewItem = new PostViewItem(post.getId(), username, useremail, avatar, post.getTitle(), post.getBody(), post.getCommentsCount());
        return postViewItem;
    }

}
