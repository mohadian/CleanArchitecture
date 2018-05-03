package com.elvotra.clean.presentation.presenter;

import com.elvotra.clean.DomainDataUtil;
import com.elvotra.clean.TestUseCaseScheduler;
import com.elvotra.clean.domain.executor.UseCaseHandler;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;
import com.elvotra.clean.domain.usecase.GetPostsUseCase;
import com.elvotra.clean.presentation.contract.PostsContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostsPresenterTest {

    private static final boolean FORCE_UPDATE = true;
    private static final boolean NOT_FORCE_UPDATE = false;

    private List<Post> posts;
    private PostsPresenter postsPresenter;

    @Captor
    private ArgumentCaptor<IPostsRepository.LoadPostsCallback> captorLoadPostsCallback;
    @Captor
    private ArgumentCaptor<Boolean> captorForceUpdate;

    @Mock
    private IPostsRepository postsRepository;
    @Mock
    private PostsContract.View mockView;

    @Before
    public void setUp() {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetPostsUseCase getPostsUseCase = new GetPostsUseCase(postsRepository);

        postsPresenter = new PostsPresenter(mockView, getPostsUseCase, useCaseHandler);
    }

    private PostsPresenter createPostPresenter() {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetPostsUseCase getPostsUseCase = new GetPostsUseCase(postsRepository);

        return new PostsPresenter(mockView, getPostsUseCase, useCaseHandler);
    }

    @Test
    public void createPresenter_shouldSetPresenterOnView() {
        postsPresenter = createPostPresenter();

        verify(mockView).setPresenter(postsPresenter);
    }

    @Test
    public void loadPosts_shouldDisplayPostsAndUpdateView_whenPostsAvailable() {
        posts = DomainDataUtil.createPostList(2, 1);
        ArgumentCaptor<List> getPostsCaptor = ArgumentCaptor.forClass(List.class);
        when(mockView.isActive()).thenReturn(true);

        postsRepository.savePosts(posts);
        postsPresenter.loadPosts(FORCE_UPDATE);

        verify(postsRepository).getPosts(anyBoolean(), captorLoadPostsCallback.capture());
        captorLoadPostsCallback.getValue().onPostsLoaded(posts);

        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView).showProgress();
        inOrder.verify(mockView).isActive();
        inOrder.verify(mockView).hideProgress();
        inOrder.verify(mockView).showPostsList(getPostsCaptor.capture());
        assertTrue(getPostsCaptor.getAllValues().get(0).size() == 2);
    }

    @Test
    public void loadPosts_shouldDisplayNoResults_whenPostsNotAvailable() {
        posts = Collections.emptyList();
        when(mockView.isActive()).thenReturn(true);

        postsRepository.savePosts(posts);
        postsPresenter.loadPosts(FORCE_UPDATE);

        verify(postsRepository).getPosts(anyBoolean(), captorLoadPostsCallback.capture());
        captorLoadPostsCallback.getValue().onPostsLoaded(posts);

        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView).showProgress();
        inOrder.verify(mockView).isActive();
        inOrder.verify(mockView).hideProgress();
        inOrder.verify(mockView).showNoResults();
    }

    @Test
    public void loadPosts_shouldNotUpdateView_whenViewDestroyedBeforePostsRetrieved() {
        when(mockView.isActive()).thenReturn(false);
        posts = DomainDataUtil.createPostList(1, 1);
        postsRepository.savePosts(posts);

        postsPresenter.loadPosts(FORCE_UPDATE);

        verify(postsRepository).getPosts(anyBoolean(), captorLoadPostsCallback.capture());
        captorLoadPostsCallback.getValue().onPostsLoaded(posts);

        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView).showProgress();
        inOrder.verify(mockView).isActive();
        inOrder.verify(mockView, never()).hideProgress();
        inOrder.verify(mockView, never()).showNoResults();
    }

    @Test
    public void loadPosts_shouldNotUpdateView_whenViewDestroyedBeforeErrorReturns() {
        when(mockView.isActive()).thenReturn(false);

        postsPresenter.loadPosts(FORCE_UPDATE);

        verify(postsRepository).getPosts(anyBoolean(), captorLoadPostsCallback.capture());
        captorLoadPostsCallback.getValue().onError(-1);

        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView).showProgress();
        inOrder.verify(mockView).isActive();
        inOrder.verify(mockView, never()).hideProgress();
        inOrder.verify(mockView, never()).showNoResults();
    }

    @Test
    public void loadPosts_shouldDisplayError_whenPostsNotRetrieved() {
        when(mockView.isActive()).thenReturn(true);

        postsPresenter.loadPosts(FORCE_UPDATE);

        verify(postsRepository).getPosts(anyBoolean(), captorLoadPostsCallback.capture());
        captorLoadPostsCallback.getValue().onError(-1);

        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView).showProgress();
        inOrder.verify(mockView).isActive();
        inOrder.verify(mockView).hideProgress();
        inOrder.verify(mockView, never()).showNoResults();
        inOrder.verify(mockView).showError(anyString());
    }

    @Test
    public void loadPosts_shouldNotDisplayError_whenPostsNotRetrieved() {
        when(mockView.isActive()).thenReturn(true);

        postsPresenter.loadPosts(FORCE_UPDATE);

        verify(postsRepository).getPosts(anyBoolean(), captorLoadPostsCallback.capture());
        captorLoadPostsCallback.getValue().onPostsLoaded(null);

        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView).showProgress();
        inOrder.verify(mockView).isActive();
        inOrder.verify(mockView).hideProgress();
        inOrder.verify(mockView).showNoResults();
        inOrder.verify(mockView, never()).showError(anyString());
    }

    @Test
    public void openPostDetails() {
        int POST_ID = 100;
        postsPresenter.openPostDetails(POST_ID);
        mockView.showPostDetails(POST_ID);
    }

    @Test
    public void resume() {
        postsPresenter.resume();
        verify(postsRepository).getPosts(captorForceUpdate.capture(), captorLoadPostsCallback.capture());
        assertEquals(NOT_FORCE_UPDATE, captorForceUpdate.getValue().booleanValue());
    }
}