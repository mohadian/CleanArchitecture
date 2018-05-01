package com.elvotra.clean.data.repository;

import com.elvotra.clean.DomainDataUtil;
import com.elvotra.clean.data.local.TypicodeLocalDataSource;
import com.elvotra.clean.data.remote.TypicodeRemoteDataSource;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PostsRepositoryImpTest {
    private PostsRepositoryImp postsRepository;

    @Mock
    private TypicodeLocalDataSource mockLocalDataSource;
    @Mock
    private TypicodeRemoteDataSource mockRemoteDataSource;

    @Mock
    private IPostsRepository.LoadPostsCallback mockLoadPostsCallback;
    @Mock
    private IPostsRepository.LoadPostCallback mockLoadPostCallback;

    @Captor
    private ArgumentCaptor<IPostsRepository.LoadPostsCallback> captorPostsCallbackArgument;

    @Before
    public void setup() {
        PostsRepositoryImp.destroyInstance();
        postsRepository = PostsRepositoryImp.getInstance(mockRemoteDataSource, mockLocalDataSource);
    }

    @After
    public void cleanUp() {
        PostsRepositoryImp.destroyInstance();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(postsRepository);
    }

    @Test
    public void getPosts_shouldCallLocalDataSource_whenAvailableLocally() {
        List<Post> posts = DomainDataUtil.createPostList(1, 1);

        postsRepository.getPosts(mockLoadPostsCallback);

        verify(mockLocalDataSource).getPosts(captorPostsCallbackArgument.capture());
        captorPostsCallbackArgument.getValue().onPostsLoaded(posts);

        verify(mockLoadPostsCallback).onPostsLoaded(posts);
        verify(mockLoadPostsCallback, never()).onError(anyInt());
    }

    @Test
    public void getPosts_shouldCallRemoteDataSource_whenNotAvailableLocallyAvailableRemotely() {
        List<Post> posts = DomainDataUtil.createPostList(1, 1);
        int statusCode = -1;

        postsRepository.getPosts(mockLoadPostsCallback);

        verify(mockLocalDataSource).getPosts(captorPostsCallbackArgument.capture());
        captorPostsCallbackArgument.getValue().onError(statusCode);

        verify(mockRemoteDataSource).getPosts(captorPostsCallbackArgument.capture());
        captorPostsCallbackArgument.getValue().onPostsLoaded(posts);

        verify(mockLoadPostsCallback).onPostsLoaded(posts);
        verify(mockLoadPostsCallback, never()).onError(anyInt());

        InOrder inOrderGetPostsOnDataSources = inOrder(mockLocalDataSource, mockRemoteDataSource);
        inOrderGetPostsOnDataSources.verify(mockLocalDataSource).getPosts(any(IPostsRepository.LoadPostsCallback.class));
        inOrderGetPostsOnDataSources.verify(mockRemoteDataSource).getPosts(any(IPostsRepository.LoadPostsCallback.class));

        InOrder inOrderUpdateLocalDataSource = inOrder(mockLocalDataSource);
        inOrderUpdateLocalDataSource.verify(mockLocalDataSource).deleteAllData();
        inOrderUpdateLocalDataSource.verify(mockLocalDataSource).savePosts(posts);
    }

    @Test
    public void getPosts_shouldCallError_whenNotAvailableLocallyNotAvailableRemotely() {
        int statusCode = -1;

        postsRepository.getPosts(mockLoadPostsCallback);

        verify(mockLocalDataSource).getPosts(captorPostsCallbackArgument.capture());
        captorPostsCallbackArgument.getValue().onError(statusCode);

        verify(mockRemoteDataSource).getPosts(captorPostsCallbackArgument.capture());
        captorPostsCallbackArgument.getValue().onError(statusCode);

        verify(mockLoadPostsCallback).onError(statusCode);
        verify(mockLoadPostsCallback, never()).onPostsLoaded(anyList());

        InOrder inOrderGetPostsOnDataSources = inOrder(mockLocalDataSource, mockRemoteDataSource);
        inOrderGetPostsOnDataSources.verify(mockLocalDataSource).getPosts(any(IPostsRepository.LoadPostsCallback.class));
        inOrderGetPostsOnDataSources.verify(mockRemoteDataSource).getPosts(any(IPostsRepository.LoadPostsCallback.class));
    }


    @Test
    public void getPost_shouldCallLocalDataSource() {
        postsRepository.getPost(-1, mockLoadPostCallback);

        verify(mockLocalDataSource).getPost(-1, mockLoadPostCallback);
    }

    @Test
    public void deleteAllData_shouldCallLocalDataSource() {
        postsRepository.deleteAllData();

        verify(mockLocalDataSource).deleteAllData();
    }

    @Test
    public void savePosts_shouldCallLocalDataSource() {
        List<Post> posts = DomainDataUtil.createPostList(1, 1);
        postsRepository.savePosts(posts);

        verify(mockLocalDataSource).savePosts(posts);
    }
}