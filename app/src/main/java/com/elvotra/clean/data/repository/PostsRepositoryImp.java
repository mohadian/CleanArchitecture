package com.elvotra.clean.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;

import java.util.List;

public class PostsRepositoryImp implements IPostsRepository {

    private IPostsRepository postsRemoteRepository;
    private IPostsRepository postsLocalRepository;

    private static PostsRepositoryImp INSTANCE;

    public static PostsRepositoryImp getInstance(IPostsRepository postsRemoteRepository, IPostsRepository postsLocalRepository) {
        if (INSTANCE == null) {
            INSTANCE = new PostsRepositoryImp(postsRemoteRepository, postsLocalRepository);
        }
        return INSTANCE;
    }

    public PostsRepositoryImp(IPostsRepository postsRemoteRepository, IPostsRepository postsLocalRepository) {
        this.postsRemoteRepository = postsRemoteRepository;
        this.postsLocalRepository = postsLocalRepository;
    }

    @Override
    public void getPosts(final boolean forrceUpdate, @NonNull final LoadPostsCallback callback) {
        if(forrceUpdate){
            loadDataFromRemoteDataSource(forrceUpdate, callback);
        } else {
            postsLocalRepository.getPosts(forrceUpdate, new LoadPostsCallback() {
                @Override
                public void onPostsLoaded(List<Post> posts) {
                    if(posts.isEmpty()){
                        loadDataFromRemoteDataSource(forrceUpdate, callback);
                    } else {
                        callback.onPostsLoaded(posts);
                    }
                }

                @Override
                public void onError(int statusCode) {
                    loadDataFromRemoteDataSource(forrceUpdate, callback);
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
