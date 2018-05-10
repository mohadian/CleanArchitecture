package com.elvotra.clean.data.local

import android.support.annotation.VisibleForTesting
import com.elvotra.clean.data.local.model.CommentEntity
import com.elvotra.clean.data.local.model.PostEntity
import com.elvotra.clean.data.local.model.UserEntity
import com.elvotra.clean.data.local.model.mapper.PostsEntityDataMapper
import com.elvotra.clean.domain.model.Comment
import com.elvotra.clean.domain.model.Post
import com.elvotra.clean.domain.model.User
import com.elvotra.clean.domain.repository.IPostsRepository
import com.elvotra.clean.threading.AppExecutors
import timber.log.Timber
import java.util.*

class TypicodeLocalDataSource private constructor(private val appExecutors: AppExecutors, private val typicodeDao: TypicodeDao) : IPostsRepository {

    override fun getPosts(forceUpdate: Boolean, callback: IPostsRepository.LoadPostsCallback) {
        val runnable = Runnable {
            val postEntities = typicodeDao.posts as ArrayList<PostEntity>
            val userEntities = typicodeDao.users as ArrayList<UserEntity>
            val commentsEntities = typicodeDao.comments as ArrayList<CommentEntity>
            appExecutors.mainThread().execute {
                Timber.d("Found %d posts", postEntities.size)
                val postArrayList = PostsEntityDataMapper.getInstance().transform(postEntities, userEntities, commentsEntities)
                callback.onPostsLoaded(postArrayList)
            }
        }

        appExecutors.diskIO().execute(runnable)
    }

    override fun getPost(postId: Int, callback: IPostsRepository.LoadPostCallback) {
        val runnable = Runnable {
            val postEntity = typicodeDao.getPostById(postId)
            if (postEntity != null) {
                Timber.d("Found %s", postEntity)

                val userEntity = typicodeDao.getUserById(postEntity.userId)
                val commentsEntities = typicodeDao.getCommentsByPostId(postId) as ArrayList<CommentEntity>
                appExecutors.mainThread().execute {
                    val postArrayList = PostsEntityDataMapper.getInstance().transform(postEntity, userEntity, commentsEntities)
                    callback.onPostLoaded(postArrayList)
                }
            } else {
                Timber.w("Cannot find the post in local database")
                callback.onError(-1)
            }
        }

        appExecutors.diskIO().execute(runnable)
    }

    override fun deleteAllData() {
        appExecutors.diskIO().execute {
            Timber.d("delete local database")
            typicodeDao.deleteUsers()
            typicodeDao.deletePosts()
            typicodeDao.deleteComments()
        }
    }

    override fun savePosts(posts: ArrayList<Post>) {
        for (post in posts) {
            savePost(post)
            saveUser(post.user)
            post.comments?.forEach { comment: Comment ->
                saveComment(comment)
            }
        }
    }

    private fun savePost(post: Post) {
        appExecutors.diskIO().execute {
            typicodeDao.insertUser(PostsEntityDataMapper.getInstance().transformToUserEntity(post))
            typicodeDao.insertPost(PostsEntityDataMapper.getInstance().transform(post))
        }
    }

    private fun saveUser(user: User?) {
        appExecutors.diskIO().execute { typicodeDao.insertUser(PostsEntityDataMapper.getInstance().transform(user)) }
    }

    private fun saveComment(comment: Comment) {
        appExecutors.diskIO().execute { typicodeDao.insertComment(PostsEntityDataMapper.getInstance().transform(comment)) }
    }

    companion object {

        @Volatile
        private var INSTANCE: TypicodeLocalDataSource? = null

        fun getInstance(appExecutors: AppExecutors, typicodeDao: TypicodeDao): TypicodeLocalDataSource? {
            if (INSTANCE == null) {
                synchronized(TypicodeLocalDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = TypicodeLocalDataSource(appExecutors, typicodeDao)
                    }
                }
            }
            return INSTANCE
        }

        @VisibleForTesting
        internal fun destroyInstance() {
            INSTANCE = null
        }
    }
}
