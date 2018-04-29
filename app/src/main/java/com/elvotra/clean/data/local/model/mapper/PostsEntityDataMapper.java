package com.elvotra.clean.data.local.model.mapper;

import com.elvotra.clean.data.local.model.CommentEntity;
import com.elvotra.clean.data.local.model.PostEntity;
import com.elvotra.clean.data.local.model.UserEntity;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostsEntityDataMapper {
    private static PostsEntityDataMapper INSTANCE;

    public static PostsEntityDataMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostsEntityDataMapper();
        }
        return INSTANCE;
    }

    private PostsEntityDataMapper() {
    }

    public List<Post> transform(List<PostEntity> postsEntities, List<UserEntity> userEntities, List<CommentEntity> commentsEntities) {
        List<Post> result = new ArrayList<>(postsEntities.size());
        Map<Integer, User> usersMap = createMap(userEntities);
        Map<Integer, Integer> postCommentsCount = createPostCommentCountMap(commentsEntities);
        for (int i = 0; i < postsEntities.size(); i++) {
            PostEntity postEntity = postsEntities.get(i);
            User user = usersMap.get(postEntity.getUserId());
            Integer commentsCount = postCommentsCount.get(postEntity.getId());
            Post post = new Post(postEntity.getId(), user, postEntity.getTitle(), postEntity.getBody(), commentsCount);
            result.add(post);
        }
        return result;
    }

    private Map<Integer, Integer> createPostCommentCountMap(List<CommentEntity> commentEntities) {
        Map<Integer, Integer> userDataMap = new HashMap<>();

        for (int i = 0; i < commentEntities.size(); i++) {
            CommentEntity commentEntity = commentEntities.get(i);
            int postId = commentEntity.getPostId();
            userDataMap.put(postId, (userDataMap.get(postId) == null) ? 1 : userDataMap.get(postId) + 1);
        }
        return userDataMap;
    }

    private Map<Integer, User> createMap(List<UserEntity> userEntities) {
        Map<Integer, User> userDataMap = new HashMap<>(userEntities.size());

        for (int i = 0; i < userEntities.size(); i++) {
            UserEntity userEntity = userEntities.get(i);
            User user = new User(userEntity.getId(), userEntity.getName(), userEntity.getUsername(), userEntity.getEmail());
            userDataMap.put(userEntity.getId(), user);
        }
        return userDataMap;
    }
}
