package com.elvotra.clean.data.repository;

import android.support.annotation.NonNull;

import com.elvotra.clean.domain.model.Comment;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.model.User;
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
        postsLocalRepository.getPosts(new LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                callback.onPostsLoaded(posts);
            }

            @Override
            public void onError(int statusCode) {
                loadDataFromRemoteDataSource(callback);
            }
        });
    }

    @Override
    public void getPost(@NonNull int postId, @NonNull LoadPostCallback callback) {
        postsLocalRepository.getPost(postId, callback);
    }

    private void loadDataFromRemoteDataSource(@NonNull final LoadPostsCallback callback) {
        postsRemoteRepository.getPosts(new LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                refreshLocalDataSource(posts);
                callback.onPostsLoaded(posts);
            }

            @Override
            public void onError(int statusCode) {
                callback.onError(statusCode);
            }
        });
    }

    private void refreshLocalDataSource(List<Post> posts) {
        postsLocalRepository.deleteAllData();
        for (Post post : posts) {
            postsLocalRepository.savePost(post);
            postsLocalRepository.saveUser(post.getUser());
            for (Comment comment : post.getComments()) {
                postsLocalRepository.saveComment(comment);
            }
        }
    }

    @Override
    public void deleteAllData() {

    }

    @Override
    public void savePost(Post post) {

    }

    @Override
    public void saveUser(User user) {

    }

    @Override
    public void saveComment(Comment comment) {

    }
}
