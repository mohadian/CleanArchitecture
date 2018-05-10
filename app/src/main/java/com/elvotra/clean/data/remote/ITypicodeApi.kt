package com.elvotra.clean.data.remote

import com.elvotra.clean.data.remote.model.PostCommentData
import com.elvotra.clean.data.remote.model.PostData
import com.elvotra.clean.data.remote.model.UserData

import retrofit2.Call
import retrofit2.http.GET
import java.util.ArrayList

interface ITypicodeApi {

    @get:GET("posts")
    val posts: Call<ArrayList<PostData>>

    @get:GET("users")
    val users: Call<ArrayList<UserData>>

    @get:GET("comments")
    val comments: Call<ArrayList<PostCommentData>>
}
