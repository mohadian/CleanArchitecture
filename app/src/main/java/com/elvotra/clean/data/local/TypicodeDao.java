package com.elvotra.clean.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.elvotra.clean.data.local.model.CommentEntity;
import com.elvotra.clean.data.local.model.PostEntity;
import com.elvotra.clean.data.local.model.UserEntity;

import java.util.List;

@Dao
public interface TypicodeDao {

    @Query("SELECT * FROM users")
    List<UserEntity> getUsers();

    @Query("SELECT * FROM posts")
    List<PostEntity> getPosts();

    @Query("SELECT * FROM posts WHERE posts_id = :postId")
    PostEntity getPostById(int postId);

    @Query("SELECT * FROM users WHERE users_id = :userId")
    UserEntity getUserById(int userId);

    @Query("SELECT * FROM comments WHERE post_id = :postId")
    List<CommentEntity> getCommentsByPostId(int postId);

    @Query("SELECT * FROM comments ORDER BY post_id")
    List<CommentEntity> getComments();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPost(PostEntity post);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(CommentEntity comment);

    @Query("DELETE FROM users")
    void deleteUsers();

    @Query("DELETE FROM posts")
    void deletePosts();

    @Query("DELETE FROM comments")
    void deleteComments();
}
