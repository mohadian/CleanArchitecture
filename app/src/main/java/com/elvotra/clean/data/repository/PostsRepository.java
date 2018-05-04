package com.elvotra.clean.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;

import java.util.List;

public class PostsRepository implements IPostsRepository {

    private IPostsRepository postsRemoteRepository;
    private IPostsRepository postsLocalRepository;

    private static PostsRepository INSTANCE;

    public static PostsRepository getInstance(IPostsRepository postsRemoteRepository, IPostsRepository postsLocalRepository) {
        if (INSTANCE == null) {
            INSTANCE = new PostsRepository(postsRemoteRepository, postsLocalRepository);
        }
        return INSTANCE;
    }

    private PostsRepository(IPostsRepository postsRemoteRepository, IPostsRepository postsLocalRepository) {
        this.postsRemoteRepository = postsRemoteRepository;
        this.postsLocalRepository = postsLocalRepository;
    }

    @Override
    public void getPosts(final boolean forceUpdate, @NonNull final LoadPostsCallback callback) {
        if(forceUpdate){
            loadDataFromRemoteDataSource(forceUpdate, callback);
        } else {
            postsLocalRepository.getPosts(forceUpdate, new LoadPostsCallback() {
                @Override
                public void onPostsLoaded(List<Post> posts) {
                    if(posts.isEmpty()){
                        loadDataFromRemoteDataSource(forceUpdate, callback);
                    } else {
                        callback.onPostsLoaded(posts);
                    }
                }

                @Override
                public void onError(int statusCode) {
                    loadDataFromRemoteDataSource(forceUpdate, callback);
                }
            });
        }
    }

    @Override
    public void getPost(int postId, @NonNull LoadPostCallback callback) {
        postsLocalRepository.getPost(postId, callback);
    }

    private void loadDataFromRemoteDataSource(boolean forceUpdate, @NonNull final LoadPostsCallback callback) {
        postsRemoteRepository.getPosts(forceUpdate, new LoadPostsCallback() {
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
        postsLocalRepository.savePosts(posts);
    }

    @Override
    public void deleteAllData() {
        postsLocalRepository.deleteAllData();
    }

    @Override
    public void savePosts(List<Post> posts) {
        postsLocalRepository.savePosts(posts);
    }

    @VisibleForTesting
    static void destroyInstance() {
        INSTANCE = null;
    }
}
