package com.elvotra.clean.data.remote;

import android.support.annotation.NonNull;

import com.elvotra.clean.data.remote.model.PostCommentData;
import com.elvotra.clean.data.remote.model.PostData;
import com.elvotra.clean.data.remote.model.UserData;
import com.elvotra.clean.data.remote.model.mapper.PostsResponseMapper;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TypicodeRemoteDataSource implements IPostsRepository {

    private static TypicodeRemoteDataSource INSTANCE;

    private List<UserData> userDataListCached;
    private List<PostData> postDataListCached;
    private List<PostCommentData> postCommentDataListCached;

    public static TypicodeRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TypicodeRemoteDataSource();
        }
        return INSTANCE;
    }

    private TypicodeRemoteDataSource() {
    }

    @Override
    public void getPosts(boolean forceUpdate, @NonNull final LoadPostsCallback callback) {
        fetchUsers(new LoadUsersCallback() {
            @Override
            public void onUsersLoaded() {
                fetchComments(new LoadPostCommentsCallback() {
                    @Override
                    public void onPostCommentsLoaded() {
                        fetchPosts(callback);
                    }

                    @Override
                    public void onError(int statusCode) {
                        callback.onError(statusCode);
                    }
                });
            }

            @Override
            public void onError(int statusCode) {
                callback.onError(statusCode);
            }
        });
    }

    private void fetchPosts(@NonNull final LoadPostsCallback callback) {
        ITypicodeApi client = TypicodeService.createService(ITypicodeApi.class);

        Call<List<PostData>> call = client.getPosts();
        call.enqueue(new Callback<List<PostData>>() {
            @Override
            public void onResponse(Call<List<PostData>> call, Response<List<PostData>> response) {
                postDataListCached = response.body();
                List<Post> posts = PostsResponseMapper.getInstance().transform(postDataListCached, userDataListCached, postCommentDataListCached);
                callback.onPostsLoaded(posts);
            }

            @Override
            public void onFailure(Call<List<PostData>> call, Throwable throwable) {
                callback.onError(-1);
            }
        });
    }

    private void fetchUsers(final LoadUsersCallback loadUsersCallback) {
        ITypicodeApi client = TypicodeService.createService(ITypicodeApi.class);

        Call<List<UserData>> call = client.getUsers();
        call.enqueue(new Callback<List<UserData>>() {
            @Override
            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                userDataListCached = response.body();
                loadUsersCallback.onUsersLoaded();
            }

            @Override
            public void onFailure(Call<List<UserData>> call, Throwable throwable) {
                loadUsersCallback.onError(-1);
            }
        });
    }

    private void fetchComments(final LoadPostCommentsCallback postCommentsCallback) {
        ITypicodeApi client = TypicodeService.createService(ITypicodeApi.class);

        Call<List<PostCommentData>> call = client.getComments();
        call.enqueue(new Callback<List<PostCommentData>>() {
            @Override
            public void onResponse(Call<List<PostCommentData>> call, Response<List<PostCommentData>> response) {
                postCommentDataListCached = response.body();
                postCommentsCallback.onPostCommentsLoaded();
            }

            @Override
            public void onFailure(Call<List<PostCommentData>> call, Throwable throwable) {
                postCommentsCallback.onError(-1);
            }
        });
    }

    @Override
    public void getPost(int postId, final @NonNull LoadPostCallback callback) {

    }

    @Override
    public void deleteAllData() {
    }

    @Override
    public void savePosts(List<Post> posts) {
    }

    private interface LoadUsersCallback {
        void onUsersLoaded();

        void onError(int statusCode);
    }

    private interface LoadPostCommentsCallback {
        void onPostCommentsLoaded();

        void onError(int statusCode);
    }
}
