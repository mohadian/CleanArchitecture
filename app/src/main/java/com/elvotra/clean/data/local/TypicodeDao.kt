package com.elvotra.clean.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import com.elvotra.clean.data.local.model.CommentEntity
import com.elvotra.clean.data.local.model.PostEntity
import com.elvotra.clean.data.local.model.UserEntity

@Dao interface TypicodeDao {

    @get:Query("SELECT * FROM users")
    val users: List<UserEntity>

    @get:Query("SELECT * FROM posts")
    val posts: List<PostEntity>

    @get:Query("SELECT * FROM comments ORDER BY post_id")
    val comments: List<CommentEntity>

    @Query("SELECT * FROM posts WHERE posts_id = :postId")
    fun getPostById(postId: Int): PostEntity

    @Query("SELECT * FROM users WHERE users_id = :userId")
    fun getUserById(userId: Int): UserEntity

    @Query("SELECT * FROM comments WHERE post_id = :postId")
    fun getCommentsByPostId(postId: Int): List<CommentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComment(comment: CommentEntity)

    @Query("DELETE FROM users")
    fun deleteUsers()

    @Query("DELETE FROM posts")
    fun deletePosts()

    @Query("DELETE FROM comments")
    fun deleteComments()
}
