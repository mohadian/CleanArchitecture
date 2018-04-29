package com.elvotra.clean.domain.repository;

import android.support.annotation.NonNull;

import com.elvotra.clean.domain.model.Post;

import java.util.List;

public interface PostsRepository {

    interface LoadPostsCallback {

        void onPostsLoaded(List<Post> posts);

        void onError(int statusCode);
    }

    interface GetPostCallback {

        void onPostLoaded(Post post);

        void onError(int statusCode);
    }

    void getPosts(@NonNull LoadPostsCallback callback);

    void getPost(@NonNull String postId, @NonNull GetPostCallback callback);
}
