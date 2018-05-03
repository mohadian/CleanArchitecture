package com.elvotra.clean.presentation.presenter;

import com.elvotra.clean.DomainDataUtil;
import com.elvotra.clean.TestUseCaseScheduler;
import com.elvotra.clean.domain.executor.UseCaseHandler;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;
import com.elvotra.clean.domain.usecase.GetPostUseCase;
import com.elvotra.clean.presentation.contract.PostDetailsContract;
import com.elvotra.clean.presentation.model.PostDetailsViewItem;
import com.elvotra.clean.presentation.model.mapper.PostDetailsViewItemMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostDetailsPresenterTest {

    private static final int POST_ID = 1;
    private Post post;
    private PostDetailsPresenter postsPresenter;

    @Captor
    private ArgumentCaptor<IPostsRepository.LoadPostCallback> captorLoadPostCallback;
    @Captor
    private ArgumentCaptor<Integer> captorPostId;

    @Mock
    private IPostsRepository postsRepository;
    @Mock
    private PostDetailsContract.View mockView;

    @Before
    public void setUp() {
        postsPresenter = createPostDetailsPresenter();
    }

    private PostDetailsPresenter createPostDetailsPresenter() {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetPostUseCase getPostUseCase = new GetPostUseCase(postsRepository);

        return new PostDetailsPresenter(mockView, getPostUseCase, useCaseHandler);
    }

    @Test
    public void createPresenter_shouldSetPresenterOnView() {
        postsPresenter = createPostDetailsPresenter();

        verify(mockView).setPresenter(postsPresenter);
    }

    @Test
    public void loadPost_shouldDisplayPost_whenPostAvailable() {
        post = DomainDataUtil.createSinglePost(1,1,1);
        PostDetailsViewItem postDetailsViewItem = PostDetailsViewItemMapper.getInstance().transform(post);
        ArgumentCaptor<PostDetailsViewItem> detailsViewItemArgumentCaptor = ArgumentCaptor.forClass(PostDetailsViewItem.class);
        when(mockView.isActive()).thenReturn(true);
        postsRepository.savePosts(Collections.singletonList(post));

        postsPresenter.loadPost(POST_ID);

        verify(postsRepository).getPost(captorPostId.capture(), captorLoadPostCallback.capture());
        captorLoadPostCallback.getValue().onPostLoaded(post);

        assertEquals(POST_ID, captorPostId.getValue().intValue());
        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView).showProgress();
        inOrder.verify(mockView).isActive();
        inOrder.verify(mockView).hideProgress();
        inOrder.verify(mockView).showPostDetails(detailsViewItemArgumentCaptor.capture());
        assertThat(detailsViewItemArgumentCaptor.getAllValues().get(0), is(postDetailsViewItem));
    }

    @Test
    public void loadPost_shouldDisplayNoResults_whenPostNotAvailable() {
        when(mockView.isActive()).thenReturn(true);
        postsRepository.savePosts(new ArrayList<Post>());

        postsPresenter.loadPost(POST_ID);

        verify(postsRepository).getPost(captorPostId.capture(), captorLoadPostCallback.capture());
        captorLoadPostCallback.getValue().onPostLoaded(post);

        assertEquals(POST_ID, captorPostId.getValue().intValue());
        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView).showProgress();
        inOrder.verify(mockView).isActive();
        inOrder.verify(mockView).hideProgress();
        inOrder.verify(mockView).showNoResults();
    }

    @Test
    public void loadPosts_shouldNotUpdateView_whenViewDestroyedBeforePostsRetrieved() {
        when(mockView.isActive()).thenReturn(false);
        post = DomainDataUtil.createSinglePost(1, 1, 1);
        postsRepository.savePosts(Collections.singletonList(post));

        postsPresenter.loadPost(POST_ID);

        verify(postsRepository).getPost(captorPostId.capture(), captorLoadPostCallback.capture());
        captorLoadPostCallback.getValue().onPostLoaded(post);

        assertEquals(POST_ID, captorPostId.getValue().intValue());
        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView).showProgress();
        inOrder.verify(mockView).isActive();
        inOrder.verify(mockView, never()).hideProgress();
        inOrder.verify(mockView, never()).showNoResults();
    }

    @Test
    public void loadPosts_shouldDisplayError_whenPostNotRetrieved() {
        when(mockView.isActive()).thenReturn(true);
        postsRepository.savePosts(new ArrayList<Post>());

        postsPresenter.loadPost(POST_ID);

        verify(postsRepository).getPost(captorPostId.capture(), captorLoadPostCallback.capture());
        captorLoadPostCallback.getValue().onError(-1);

        assertEquals(POST_ID, captorPostId.getValue().intValue());
        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView).showProgress();
        inOrder.verify(mockView).isActive();
        inOrder.verify(mockView).hideProgress();
        inOrder.verify(mockView, never()).showNoResults();
        inOrder.verify(mockView).showError(anyString());
    }

    @Test
    public void loadPosts_shouldNotDisplayError_whenViewDestroyedBeforeErrorReturns() {
        when(mockView.isActive()).thenReturn(false);
        postsRepository.savePosts(Collections.singletonList(post));

        postsPresenter.loadPost(POST_ID);

        verify(postsRepository).getPost(captorPostId.capture(), captorLoadPostCallback.capture());
        captorLoadPostCallback.getValue().onError(-1);

        assertEquals(POST_ID, captorPostId.getValue().intValue());
        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView).showProgress();
        inOrder.verify(mockView).isActive();
        inOrder.verify(mockView, never()).hideProgress();
        inOrder.verify(mockView, never()).showNoResults();
        inOrder.verify(mockView, never()).showError(anyString());
    }

    @Test
    public void resume() {
        postsPresenter.loadPost(POST_ID);
        postsPresenter.resume();
        verify(postsRepository, times(2)).getPost(captorPostId.capture(), captorLoadPostCallback.capture());
        assertEquals(POST_ID, captorPostId.getValue().intValue());
    }

}