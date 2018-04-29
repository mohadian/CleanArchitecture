package com.elvotra.clean.data.remote;

import com.elvotra.clean.data.remote.model.PostCommentData;
import com.elvotra.clean.data.remote.model.PostData;
import com.elvotra.clean.data.remote.model.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TypicodeApi {

    @GET("posts")
    Call<List<PostData>> getPosts();

    @GET("users")
    Call<List<UserData>> getUsers();

    @GET("comments")
    Call<List<PostCommentData>> getComments();
}
