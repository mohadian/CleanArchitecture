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

    void getPosts(@NonNull LoadPostsCallback callback);

    void getPost(@NonNull int postId, @NonNull LoadPostCallback callback);

    void deleteAllData();

    void savePosts(List<Post> posts);
}
