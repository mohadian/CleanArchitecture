package com.elvotra.clean.data.local;

import android.support.annotation.NonNull;

import com.elvotra.clean.data.local.model.CommentEntity;
import com.elvotra.clean.data.local.model.PostEntity;
import com.elvotra.clean.data.local.model.UserEntity;
import com.elvotra.clean.data.local.model.mapper.PostsEntityDataMapper;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.PostsRepository;

import java.util.Collections;
import java.util.List;

public class TypicodeLocalDataSource implements PostsRepository {

    private static volatile TypicodeLocalDataSource INSTANCE;

    private TypicodeDao typicodeDao;

    private TypicodeLocalDataSource(@NonNull TypicodeDao typicodeDao) {
        this.typicodeDao = typicodeDao;
    }

    public static TypicodeLocalDataSource getInstance(@NonNull TypicodeDao typicodeDao) {
        if (INSTANCE == null) {
            synchronized (TypicodeLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TypicodeLocalDataSource(typicodeDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getPosts(@NonNull final LoadPostsCallback callback) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {

                List<PostEntity> postEntities = typicodeDao.getUsersPosts();
                List<UserEntity> userEntities = typicodeDao.getUsers();
                List<CommentEntity> commentsEntities = typicodeDao.getComments();
                if (postEntities.isEmpty()) {
                    callback.onPostsLoaded(Collections.<Post>emptyList());
                } else {
                    List<Post> postArrayList = PostsEntityDataMapper.getInstance().transform( postEntities, userEntities, commentsEntities);
                    callback.onPostsLoaded(postArrayList);
                }
            }
        };

        new Thread(runnable).start();
    }

    @Override
    public void getPost(@NonNull String postId, @NonNull GetPostCallback callback) {

    }
}
