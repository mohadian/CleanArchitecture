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

    @Query("SELECT * FROM users WHERE users_id = :userId")
    UserEntity getUserById(long userId);

    @Query("SELECT * FROM posts ORDER BY user_id")
    List<PostEntity> getUsersPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPost(List<UserEntity> userEntities, List<PostEntity> postEntities, List<CommentEntity> commentEntities);

    @Query("DELETE FROM users WHERE users_id = :userId")
    void deleteUser(long userId);

    @Query("SELECT * FROM comments ORDER BY post_id")
    List<CommentEntity> getComments();
}
