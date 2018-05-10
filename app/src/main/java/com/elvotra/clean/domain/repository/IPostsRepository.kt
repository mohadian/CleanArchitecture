package com.elvotra.clean.domain.repository

import com.elvotra.clean.domain.model.Post
import java.util.ArrayList

interface IPostsRepository {

    interface LoadPostsCallback {

        fun onPostsLoaded(posts: ArrayList<Post>)

        fun onError(statusCode: Int)
    }

    interface LoadPostCallback {

        fun onPostLoaded(post: Post)

        fun onError(statusCode: Int)
    }

    fun getPosts(forceUpdate: Boolean, callback: LoadPostsCallback)

    fun getPost(postId: Int, callback: LoadPostCallback)

    fun deleteAllData()

    fun savePosts(posts: ArrayList<Post>)
}
