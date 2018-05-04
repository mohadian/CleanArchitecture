package com.elvotra.clean.data.local;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.elvotra.clean.domain.model.Comment;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.model.User;
import com.elvotra.clean.domain.repository.IPostsRepository;
import com.elvotra.clean.threading.SingleExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TypicodeLocalDataSourceTest {
    private static final String POST_TITLE = "title";
    private static final String POST_BODY = "body";
    private static final String USER_NAME = "name";
    private static final String USER_USERNAME = "username";
    private static final String USER_EMAIL = "user@test.com";
    private static final String COMMENT_USER_NAME = "comment_name";
    private static final String COMMENT_USER_EMAIL = "comment_email";
    private static final String COMMENT_BODY = "comment_body";

    private TypicodeLocalDataSource localDataSource;

    private TypicodeDatabase typicodeDatabase;

    @Before
    public void setup() {
        // using an in-memory database for testing
        typicodeDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                TypicodeDatabase.class)
                .build();
        TypicodeDao typicodeDao = typicodeDatabase.typicodeDao();

        TypicodeLocalDataSource.destroyInstance();
        localDataSource = TypicodeLocalDataSource.getInstance(new SingleExecutors(), typicodeDao);
    }

    @After
    public void cleanUp() {
        typicodeDatabase.close();
        TypicodeLocalDataSource.destroyInstance();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(localDataSource);
    }

    @Test
    public void getPosts_shouldReturnSavedPosts_whenPostsAvailable() {
        List<Post> posts = createPostList(2, 3);
        final Post newPost = posts.get(0);

        localDataSource.savePosts(posts);

        localDataSource.getPosts(false, new IPostsRepository.LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                assertNotNull(posts);
                assertEquals(2, posts.size());
                Post post = posts.get(0);
                assertNotNull(post.getComments());
                assertEquals(3, post.getComments().size());
                assertThat(post, is(newPost));
            }

            @Override
            public void onError(int statusCode) {
                fail();
            }
        });
    }

    @Test
    public void getPosts_shouldReturnEmptyList_whenNoPostsAvailable() {
        localDataSource.getPosts(false, new IPostsRepository.LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                assertEquals(0, posts.size());
            }

            @Override
            public void onError(int statusCode) {
                fail();
            }
        });
    }

    @Test
    public void getPost_shouldReturnStoredPost_whenAvailable() {
        List<Post> posts = createPostList(2, 3);
        final Post newPost = posts.get(0);

        localDataSource.savePosts(posts);

        localDataSource.getPost(newPost.getId(), new IPostsRepository.LoadPostCallback() {
            @Override
            public void onPostLoaded(Post post) {
                assertNotNull(post);
                assertNotNull(post.getComments());
                assertEquals(3, post.getComments().size());
                assertThat(post, is(newPost));
            }

            @Override
            public void onError(int statusCode) {
                fail();
            }
        });
    }

    @Test
    public void getPost_shouldReturnStoredPost_whenNotAvailable() {
        List<Post> posts = createPostList(2, 3);

        localDataSource.savePosts(posts);

        localDataSource.getPost(-1, new IPostsRepository.LoadPostCallback() {
            @Override
            public void onPostLoaded(Post post) {
                fail();
            }

            @Override
            public void onError(int statusCode) {
                assertEquals(-1, statusCode);
            }
        });
    }

    @Test
    public void deleteAllData_getPostsReturnEmptyList() {
        IPostsRepository.LoadPostsCallback mockLoadPostsCallback = mock(IPostsRepository.LoadPostsCallback.class);
        ArgumentCaptor<List<Post>> captorListArgument = new ArgumentCaptor<>();
        List<Post> posts = createPostList(1, 1);
        localDataSource.savePosts(posts);

        localDataSource.deleteAllData();

        localDataSource.getPosts(false, mockLoadPostsCallback);

        verify(mockLoadPostsCallback, never()).onError(anyInt());
        verify(mockLoadPostsCallback).onPostsLoaded(captorListArgument.capture());
        assertTrue(captorListArgument.getValue().isEmpty());
    }

    @Test
    public void savePosts_shouldStorePosts() {
        List<Post> posts = createPostList(1, 1);
        final Post newPost = posts.get(0);

        localDataSource.savePosts(posts);

        localDataSource.getPost(newPost.getId(), new IPostsRepository.LoadPostCallback() {
            @Override
            public void onPostLoaded(Post post) {
                assertThat(post, is(newPost));
            }

            @Override
            public void onError(int statusCode) {
                fail();
            }
        });
    }

    private List<Post> createPostList(int postCount, int commentsCount) {
        List<Post> posts = new ArrayList<>(postCount);
        for (int i = 0; i < postCount; i++) {
            User user = new User(i, USER_NAME, USER_USERNAME, USER_EMAIL);
            List<Comment> comments = new ArrayList<>(commentsCount);
            for (int j = 0; j < commentsCount; j++) {
                Comment comment = new Comment(i, j + (i * commentsCount), COMMENT_USER_NAME, COMMENT_USER_EMAIL, COMMENT_BODY);
                comments.add(comment);
            }
            final Post newPost = new Post(i, user, POST_TITLE, POST_BODY, comments);
            posts.add(newPost);
        }

        return posts;
    }
}