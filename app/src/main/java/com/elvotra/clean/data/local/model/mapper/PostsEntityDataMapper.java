package com.elvotra.clean.data.local.model.mapper;

import com.elvotra.clean.data.local.model.CommentEntity;
import com.elvotra.clean.data.local.model.PostEntity;
import com.elvotra.clean.data.local.model.UserEntity;
import com.elvotra.clean.domain.model.Comment;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.model.User;

import java.util.ArrayList;
import java.util.Collections;
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

    //region Domain to Data models
    public PostEntity transform(Post post) {
        return new PostEntity(post.getId(),
                post.getUser().getId(),
                post.getTitle(),
                post.getBody()
        );
    }

    public UserEntity transform(User user) {
        return new UserEntity(user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail());
    }

    public CommentEntity transform(Comment comment) {
        return new CommentEntity(comment.getId(),
                comment.getPostId(),
                comment.getName(),
                comment.getEmail(),
                comment.getBody());
    }

    public UserEntity transformToUserEntity(Post post) {
        User user = post.getUser();
        return new UserEntity(user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail());
    }
    //endregion

    //region Data to Domain models
    public ArrayList<Post> transform(ArrayList<PostEntity> postsEntities, ArrayList<UserEntity> userEntities, List<CommentEntity> commentsEntities) {
        ArrayList<Post> result = new ArrayList<>(postsEntities.size());
        Map<Integer, User> usersMap = createMap(userEntities);
        Map<Integer, List<Comment>> postComments = createPostCommentsMap(commentsEntities);
        for (int i = 0; i < postsEntities.size(); i++) {
            PostEntity postEntity = postsEntities.get(i);
            User user = usersMap.get(postEntity.getUserId());
            List<Comment> comments = (postComments.containsKey(postEntity.getId())) ? postComments.get(postEntity.getId()) : Collections.<Comment>emptyList();

            Post post = new Post(postEntity.getId(), user, postEntity.getTitle(), postEntity.getBody(), comments);
            result.add(post);
        }
        return result;
    }

    public Post transform(PostEntity postEntity, UserEntity userEntity, List<CommentEntity> commentsEntities) {
        Post result ;
        Map<Integer, List<Comment>> postComments = createPostCommentsMap(commentsEntities);
        User user = new User(userEntity.getId(), userEntity.getName(), userEntity.getUsername(), userEntity.getEmail());
        List<Comment> comments = (postComments.containsKey(postEntity.getId())) ? postComments.get(postEntity.getId()) : Collections.<Comment>emptyList();

        result = new Post(postEntity.getId(), user, postEntity.getTitle(), postEntity.getBody(), comments);
        return result;
    }

    private Map<Integer, List<Comment>> createPostCommentsMap(List<CommentEntity> commentEntities) {
        Map<Integer, List<Comment>> commentsDataMap = new HashMap<>();
        List<Comment> comments;

        for (int i = 0; i < commentEntities.size(); i++) {
            CommentEntity commentData = commentEntities.get(i);
            int postId = commentData.getPostId();
            comments = (commentsDataMap.containsKey(postId)) ? commentsDataMap.get(postId) : new ArrayList<Comment>();
            comments.add(new Comment(commentData.getPostId(), commentData.getId(), commentData.getName(), commentData.getEmail(), commentData.getBody()));
            commentsDataMap.put(postId, comments);
        }
        return commentsDataMap;
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
    // endregion
}
