package com.elvotra.clean.domain.repository;

import android.support.annotation.NonNull;

import com.elvotra.clean.domain.model.Post;

import java.util.List;

public interface IPostsRepository {

    interface LoadPostsCallback {

        void onPostsLoaded(List<Post> posts);

        void onError(int statusCode);
    }

    interface LoadPostCallback {

        void onPostLoaded(Post post);

        void onError(int statusCode);
    }

    void getPosts(boolean forceUpdate, @NonNull LoadPostsCallback callback);

    void getPost(int postId, @NonNull LoadPostCallback callback);

    void deleteAllData();

    void savePosts(List<Post> posts);
}
