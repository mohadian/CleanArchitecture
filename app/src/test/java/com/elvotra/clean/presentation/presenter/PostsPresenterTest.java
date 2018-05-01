package com.elvotra.clean.presentation.presenter;

import android.support.annotation.NonNull;

import com.elvotra.clean.TestMainThread;
import com.elvotra.clean.TestThreadExecutor;
import com.elvotra.clean.domain.executor.IExecutor;
import com.elvotra.clean.domain.executor.IMainThread;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;
import com.elvotra.clean.presentation.contract.PostsContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PostsPresenterTest {

    @Mock
    private PostsContract.View mockView;

    PostsPresenter postsPresenter;

    @Before
    public void setUp() throws Exception {
        IExecutor testExecutor = new TestThreadExecutor();
        IMainThread testMainThread = new TestMainThread();
        IPostsRepository testRepository = new TestRepository();
        postsPresenter = new PostsPresenter(testExecutor, testMainThread, mockView, testRepository);
    }

    @Test
    public void loadPosts() {
        postsPresenter.loadPosts();
        verify(mockView).showProgress();
    }

    @Test
    public void openPostDetails() {
    }

    @Test
    public void resume() {
    }

    @Test
    public void destroy() {
    }

    @Test
    public void onError() {
    }

    @Test
    public void onPostsRetrieved() {
    }

    @Test
    public void onRetrievalFailed() {
    }

    private class TestRepository implements IPostsRepository {
        @Override
        public void getPosts(@NonNull LoadPostsCallback callback) {

        }

        @Override
        public void getPost(@NonNull int postId, @NonNull LoadPostCallback callback) {

        }

        @Override
        public void deleteAllData() {

        }

        @Override
        public void savePosts(List<Post> posts) {

        }
    }
}