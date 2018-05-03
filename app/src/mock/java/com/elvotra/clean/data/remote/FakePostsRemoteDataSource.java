package com.elvotra.clean.data.remote;

import android.support.annotation.NonNull;

import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FakePostsRemoteDataSource implements IPostsRepository {

    private static FakePostsRemoteDataSource INSTANCE;

    private static Map<Integer, Post> postsHash = new LinkedHashMap<>();

    private FakePostsRemoteDataSource() {
        postsHash = new HashMap<>();
    }

    public static FakePostsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakePostsRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getPosts(boolean forceUpdate, @NonNull LoadPostsCallback callback) {
        callback.onPostsLoaded(new ArrayList<>(postsHash.values()));
    }

    @Override
    public void getPost(int postId, @NonNull LoadPostCallback callback) {
        callback.onPostLoaded(postsHash.get(postId));
    }

    @Override
    public void deleteAllData() {
        postsHash.clear();
    }

    @Override
    public void savePosts(List<Post> posts) {
        for (Post post : posts) {
            postsHash.put(post.getId(), post);
        }
    }
}
