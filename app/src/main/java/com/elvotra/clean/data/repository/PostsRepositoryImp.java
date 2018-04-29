package com.elvotra.clean.data.repository;

import android.support.annotation.NonNull;

import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.PostsRepository;

import java.util.List;

public class PostsRepositoryImp implements PostsRepository {

    private PostsRepository postsRemoteRepository;
    private PostsRepository postsLocalRepository;

    private static PostsRepositoryImp INSTANCE;

    public static PostsRepositoryImp getInstance(PostsRepository postsRemoteRepository, PostsRepository postsLocalRepository) {
        if (INSTANCE == null) {
            INSTANCE = new PostsRepositoryImp(postsRemoteRepository, postsLocalRepository);
        }
        return INSTANCE;
    }

    public PostsRepositoryImp(PostsRepository postsRemoteRepository, PostsRepository postsLocalRepository) {
        this.postsRemoteRepository = postsRemoteRepository;
        this.postsLocalRepository = postsLocalRepository;
    }

    @Override
    public void getPosts(@NonNull final LoadPostsCallback callback) {
        postsRemoteRepository.getPosts(new LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                callback.onPostsLoaded(posts);
            }

            @Override
            public void onError(int statusCode) {
                callback.onError(statusCode);
            }
        });
    }

    @Override
    public void getPost(@NonNull String postId, @NonNull GetPostCallback callback) {

    }
}
