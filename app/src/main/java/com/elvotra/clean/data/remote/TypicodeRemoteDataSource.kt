package com.elvotra.clean.data.remote

import com.elvotra.clean.data.remote.model.PostCommentData
import com.elvotra.clean.data.remote.model.PostData
import com.elvotra.clean.data.remote.model.UserData
import com.elvotra.clean.data.remote.model.mapper.PostsResponseMapper
import com.elvotra.clean.domain.model.Post
import com.elvotra.clean.domain.repository.IPostsRepository
import com.elvotra.clean.utils.Constants

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class TypicodeRemoteDataSource private constructor() : IPostsRepository {

    private val client: ITypicodeApi = TypicodeService.createService(Constants.TYPICODE_API_BASE_URL, ITypicodeApi::class.java)

    private var userDataListCached: ArrayList<UserData>? = null
    private var postDataListCached: ArrayList<PostData>? = null
    private var postCommentDataListCached: ArrayList<PostCommentData>? = null

    override fun getPosts(forceUpdate: Boolean, callback: IPostsRepository.LoadPostsCallback) {
        fetchUsers(object : LoadUsersCallback {
            override fun onUsersLoaded() {
                fetchComments(object : LoadPostCommentsCallback {
                    override fun onPostCommentsLoaded() {
                        fetchPosts(callback)
                    }

                    override fun onError(statusCode: Int) {
                        callback.onError(statusCode)
                    }
                })
            }

            override fun onError(statusCode: Int) {
                callback.onError(statusCode)
            }
        })
    }

    private fun fetchPosts(callback: IPostsRepository.LoadPostsCallback) {
        val call = client.posts
        call.enqueue(object : Callback<ArrayList<PostData>> {
            override fun onResponse(call: Call<ArrayList<PostData>>, response: Response<ArrayList<PostData>>) {
                postDataListCached = response.body()
                val posts = PostsResponseMapper.instance.transform(postDataListCached, userDataListCached, postCommentDataListCached)
                callback.onPostsLoaded(posts)
            }

            override fun onFailure(call: Call<ArrayList<PostData>>, throwable: Throwable) {
                callback.onError(-1)
            }
        })
    }

    private fun fetchUsers(loadUsersCallback: LoadUsersCallback) {
        val call = client.users
        call.enqueue(object : Callback<ArrayList<UserData>> {
            override fun onResponse(call: Call<ArrayList<UserData>>, response: Response<ArrayList<UserData>>) {
                userDataListCached = response.body()
                loadUsersCallback.onUsersLoaded()
            }

            override fun onFailure(call: Call<ArrayList<UserData>>, throwable: Throwable) {
                loadUsersCallback.onError(-1)
            }
        })
    }

    private fun fetchComments(postCommentsCallback: LoadPostCommentsCallback) {
        val call = client.comments
        call.enqueue(object : Callback<ArrayList<PostCommentData>> {
            override fun onResponse(call: Call<ArrayList<PostCommentData>>, response: Response<ArrayList<PostCommentData>>) {
                postCommentDataListCached = response.body()
                postCommentsCallback.onPostCommentsLoaded()
            }

            override fun onFailure(call: Call<ArrayList<PostCommentData>>, throwable: Throwable) {
                postCommentsCallback.onError(-1)
            }
        })
    }

    override fun getPost(postId: Int, callback: IPostsRepository.LoadPostCallback) {

    }

    override fun deleteAllData() {}

    override fun savePosts(posts: ArrayList<Post>) {}

    private interface LoadUsersCallback {
        fun onUsersLoaded()

        fun onError(statusCode: Int)
    }

    private interface LoadPostCommentsCallback {
        fun onPostCommentsLoaded()

        fun onError(statusCode: Int)
    }

    companion object {

        private var INSTANCE: TypicodeRemoteDataSource = TypicodeRemoteDataSource()

        val instance: TypicodeRemoteDataSource
            get() {
                return INSTANCE
            }
    }
}
