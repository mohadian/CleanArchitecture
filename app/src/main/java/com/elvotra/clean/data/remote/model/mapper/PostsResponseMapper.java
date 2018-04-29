package com.elvotra.clean.data.remote.model.mapper;

import com.elvotra.clean.data.remote.model.PostCommentData;
import com.elvotra.clean.data.remote.model.PostData;
import com.elvotra.clean.data.remote.model.UserData;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostsResponseMapper {

    private static PostsResponseMapper INSTANCE;

    public static PostsResponseMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostsResponseMapper();
        }
        return INSTANCE;
    }

    private PostsResponseMapper() {
    }

    public List<Post> transform(List<PostData> response, List<UserData> userData, List<PostCommentData> postCommentData) {
        List<Post> result = new ArrayList<>(response.size());
        Map<Integer, User> userDataMap = createMap(userData);
        Map<Integer, Integer> postCommentsCount = createPostCommentCountMap(postCommentData);
        for (int i = 0; i < response.size(); i++) {
            PostData postData = response.get(i);
            User user = userDataMap.get(postData.getUserId());
            Integer commentsCount = postCommentsCount.get(postData.getId());
            Post post = new Post(postData.getId(), user, postData.getTitle(), postData.getBody(), commentsCount);
            result.add(post);
        }
        return result;
    }

    private Map<Integer, Integer> createPostCommentCountMap(List<PostCommentData> postCommentDataList) {
        Map<Integer, Integer> userDataMap = new HashMap<>();

        for (int i = 0; i < postCommentDataList.size(); i++) {
            PostCommentData postCommentData = postCommentDataList.get(i);
            int postId = postCommentData.getPostId();
            userDataMap.put(postId, (userDataMap.get(postId) == null) ? 1 : userDataMap.get(postId) + 1);
        }
        return userDataMap;
    }

    private Map<Integer, User> createMap(List<UserData> usersData) {
        Map<Integer, User> userDataMap = new HashMap<>(usersData.size());

        for (int i = 0; i < usersData.size(); i++) {
            UserData userData = usersData.get(i);
            User user = new User(userData.getId(), userData.getName(), userData.getUsername(), userData.getEmail());
            userDataMap.put(userData.getId(), user);
        }
        return userDataMap;
    }
}
