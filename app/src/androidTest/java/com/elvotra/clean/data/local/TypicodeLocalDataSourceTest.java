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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TypicodeLocalDataSourceTest {
    private TypicodeLocalDataSource localDataSource;

    private TypicodeDatabase typicodeDatabase;

    @Before
    public void setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        typicodeDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                TypicodeDatabase.class)
                .build();
        TypicodeDao tasksDao = typicodeDatabase.typicodeDao();

        // Make sure that we're not keeping a reference to the wrong instance.
        TypicodeLocalDataSource.clearInstance();
        localDataSource = TypicodeLocalDataSource.getInstance(new SingleExecutors(), tasksDao);
    }

    @After
    public void cleanUp() {
        typicodeDatabase.close();
        TypicodeLocalDataSource.clearInstance();
    }


    @Test
    public void getPosts() {
    }

    @Test
    public void getPost() {
    }

    @Test
    public void deleteAllData() {
    }

    @Test
    public void savePosts_shouldStorePosts() {
        User user = new User(1, "name", "username", "user@test.com");
        List<Comment> comments = new ArrayList<>(1);
        Comment comment = new Comment(1, 1, "comment_name", "comment_email", "comment_body");
        comments.add(comment);
        final Post newPost = new Post(1, user, "title", "body", comments);
        List<Post> posts = new ArrayList<>(1);
        posts.add(newPost);

        // When saved into the persistent repository
        localDataSource.savePosts(posts);

        // Then the task can be retrieved from the persistent repository
        localDataSource.getPost(newPost.getId(), new IPostsRepository.LoadPostCallback() {
            @Override
            public void onPostLoaded(Post post) {
                assertThat(post, is(newPost));
            }

            @Override
            public void onError(int statusCode) {
                fail("Callback error");
            }
        });
    }
}