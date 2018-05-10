package com.elvotra.clean.data.repository

import android.support.annotation.VisibleForTesting

import com.elvotra.clean.domain.model.Post
import com.elvotra.clean.domain.repository.IPostsRepository

import java.util.ArrayList

class PostsRepository private constructor(private val postsRemoteRepository: IPostsRepository, private val postsLocalRepository: IPostsRepository) : IPostsRepository {

    override fun getPosts(forceUpdate: Boolean, callback: IPostsRepository.LoadPostsCallback) {
        if (forceUpdate) {
            loadDataFromRemoteDataSource(forceUpdate, callback)
        } else {
            postsLocalRepository.getPosts(forceUpdate, object : IPostsRepository.LoadPostsCallback {
                override fun onPostsLoaded(posts: ArrayList<Post>) {
                    if (posts.isEmpty()) {
                        loadDataFromRemoteDataSource(forceUpdate, callback)
                    } else {
                        callback.onPostsLoaded(posts)
                    }
                }

                override fun onError(statusCode: Int) {
                    loadDataFromRemoteDataSource(forceUpdate, callback)
                }
            })
        }
    }

    override fun getPost(postId: Int, callback: IPostsRepository.LoadPostCallback) {
        postsLocalRepository.getPost(postId, callback)
    }

    private fun loadDataFromRemoteDataSource(forceUpdate: Boolean, callback: IPostsRepository.LoadPostsCallback) {
        postsRemoteRepository.getPosts(forceUpdate, object : IPostsRepository.LoadPostsCallback {
            override fun onPostsLoaded(posts: ArrayList<Post>) {
                refreshLocalDataSource(posts)
                callback.onPostsLoaded(posts)
            }

            override fun onError(statusCode: Int) {
                callback.onError(statusCode)
            }
        })
    }

    private fun refreshLocalDataSource(posts: ArrayList<Post>) {
        postsLocalRepository.deleteAllData()
        postsLocalRepository.savePosts(posts)
    }

    override fun deleteAllData() {
        postsLocalRepository.deleteAllData()
    }

    override fun savePosts(posts: ArrayList<Post>) {
        postsLocalRepository.savePosts(posts)
    }

    companion object {

        private var INSTANCE: PostsRepository? = null

        fun getInstance(postsRemoteRepository: IPostsRepository, postsLocalRepository: IPostsRepository): PostsRepository? {
            if (INSTANCE == null) {
                INSTANCE = PostsRepository(postsRemoteRepository, postsLocalRepository)
            }
            return INSTANCE
        }

        @VisibleForTesting
        internal fun destroyInstance() {
            INSTANCE = null
        }
    }
}
