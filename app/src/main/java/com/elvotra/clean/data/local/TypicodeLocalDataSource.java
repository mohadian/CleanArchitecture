package com.elvotra.clean.data.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.elvotra.clean.data.local.model.CommentEntity;
import com.elvotra.clean.data.local.model.PostEntity;
import com.elvotra.clean.data.local.model.UserEntity;
import com.elvotra.clean.data.local.model.mapper.PostsEntityDataMapper;
import com.elvotra.clean.domain.model.Comment;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.model.User;
import com.elvotra.clean.domain.repository.IPostsRepository;
import com.elvotra.clean.threading.AppExecutors;

import java.util.List;

import timber.log.Timber;

public class TypicodeLocalDataSource implements IPostsRepository {

    private static volatile TypicodeLocalDataSource INSTANCE;

    private TypicodeDao typicodeDao;

    private AppExecutors appExecutors;

    private TypicodeLocalDataSource(@NonNull AppExecutors appExecutors, @NonNull TypicodeDao typicodeDao) {
        this.typicodeDao = typicodeDao;
        this.appExecutors = appExecutors;
    }

    public static TypicodeLocalDataSource getInstance(@NonNull AppExecutors appExecutors, @NonNull TypicodeDao typicodeDao) {
        if (INSTANCE == null) {
            synchronized (TypicodeLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TypicodeLocalDataSource(appExecutors, typicodeDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getPosts(boolean forceUpdate, @NonNull final LoadPostsCallback callback) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                final List<PostEntity> postEntities = typicodeDao.getPosts();
                final List<UserEntity> userEntities = typicodeDao.getUsers();
                final List<CommentEntity> commentsEntities = typicodeDao.getComments();
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        Timber.d("Found %d posts", postEntities.size());
                        List<Post> postArrayList = PostsEntityDataMapper.getInstance().transform(postEntities, userEntities, commentsEntities);
                        callback.onPostsLoaded(postArrayList);
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getPost(final int postId, final @NonNull LoadPostCallback callback) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                final PostEntity postEntity = typicodeDao.getPostById(postId);
                if (postEntity != null) {
                    Timber.d("Found %s", postEntity);

                    final UserEntity userEntity = typicodeDao.getUserById(postEntity.getUserId());
                    final List<CommentEntity> commentsEntities = typicodeDao.getCommentsByPostId(postId);
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            Post postArrayList = PostsEntityDataMapper.getInstance().transform(postEntity, userEntity, commentsEntities);
                            callback.onPostLoaded(postArrayList);
                        }
                    });
                } else {
                    Timber.w("Cannot find the post in local database");
                    callback.onError(-1);
                }
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAllData() {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Timber.d("delete local database");
                typicodeDao.deleteUsers();
                typicodeDao.deletePosts();
                typicodeDao.deleteComments();
            }
        });
    }

    @Override
    public void savePosts(final List<Post> posts) {
        for (Post post : posts) {
            savePost(post);
            saveUser(post.getUser());
            for (Comment comment : post.getComments()) {
                saveComment(comment);
            }
        }
    }

    private void savePost(final Post post) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                typicodeDao.insertUser(PostsEntityDataMapper.getInstance().transformToUserEntity(post));
                typicodeDao.insertPost(PostsEntityDataMapper.getInstance().transform(post));
            }
        });
    }

    private void saveUser(final User user) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                typicodeDao.insertUser(PostsEntityDataMapper.getInstance().transform(user));
            }
        });
    }

    private void saveComment(final Comment comment) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                typicodeDao.insertComment(PostsEntityDataMapper.getInstance().transform(comment));
            }
        });
    }

    @VisibleForTesting
    static void destroyInstance() {
        INSTANCE = null;
    }
}
