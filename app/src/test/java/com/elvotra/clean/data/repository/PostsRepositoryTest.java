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

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PostsRepositoryTest {
    private static final boolean NOT_FORRCE_UPDATE = false;
    private static final boolean FORRCE_UPDATE = true;
    private PostsRepository postsRepository;

    @Captor
    private ArgumentCaptor<IPostsRepository.LoadPostsCallback> captorPostsCallbackArgument;
    @Captor
    private ArgumentCaptor<Boolean> captorForceUpdateArgument;

    @Mock
    private TypicodeLocalDataSource mockLocalDataSource;
    @Mock
    private TypicodeRemoteDataSource mockRemoteDataSource;
    @Mock
    private IPostsRepository.LoadPostsCallback mockLoadPostsCallback;
    @Mock
    private IPostsRepository.LoadPostCallback mockLoadPostCallback;

    @Before
    public void setup() {
        PostsRepository.Companion.destroyInstance();
        postsRepository = PostsRepository.Companion.getInstance(mockRemoteDataSource, mockLocalDataSource);
    }

    @After
    public void cleanUp() {
        PostsRepository.Companion.destroyInstance();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(postsRepository);
    }

    @Test
    public void getPosts_shouldCallLocalDataSource_whenNotForceUpdateAvailableLocally() {
        List<Post> posts = DomainDataUtil.createPostList(1, 1);

        postsRepository.getPosts(NOT_FORRCE_UPDATE, mockLoadPostsCallback);

        verify(mockRemoteDataSource, never()).getPosts(anyBoolean(), any(IPostsRepository.LoadPostsCallback.class));
        verify(mockLocalDataSource).getPosts(captorForceUpdateArgument.capture(), captorPostsCallbackArgument.capture());
        captorPostsCallbackArgument.getValue().onPostsLoaded(posts);

        assertEquals(NOT_FORRCE_UPDATE, captorForceUpdateArgument.getValue());
        verify(mockLoadPostsCallback).onPostsLoaded(posts);
        verify(mockLoadPostsCallback, never()).onError(anyInt());
    }

    @Test
    public void getPosts_shouldCallLocalDataSourceAndRemoteDataSource_whenNotForceUpdateNotAvailableLocally() {
        List<Post> posts = DomainDataUtil.createPostList(1, 1);

        postsRepository.getPosts(NOT_FORRCE_UPDATE, mockLoadPostsCallback);

        InOrder inOrder = inOrder(mockLocalDataSource, mockRemoteDataSource);
        inOrder.verify(mockLocalDataSource).getPosts(captorForceUpdateArgument.capture(), captorPostsCallbackArgument.capture());
        captorPostsCallbackArgument.getValue().onPostsLoaded(Collections.<Post>emptyList());
        inOrder.verify(mockRemoteDataSource).getPosts(captorForceUpdateArgument.capture(), captorPostsCallbackArgument.capture());
        captorPostsCallbackArgument.getValue().onPostsLoaded(posts);

        assertEquals(NOT_FORRCE_UPDATE, captorForceUpdateArgument.getValue());
        verify(mockLoadPostsCallback).onPostsLoaded(posts);
        verify(mockLoadPostsCallback, never()).onError(anyInt());
    }

    @Test
    public void getPosts_shouldCallRemoteDataSource_whenForceUpdate() {
        List<Post> posts = DomainDataUtil.createPostList(1, 1);

        postsRepository.getPosts(FORRCE_UPDATE, mockLoadPostsCallback);

        verify(mockLocalDataSource, never()).getPosts(anyBoolean(), any(IPostsRepository.LoadPostsCallback.class));
        verify(mockRemoteDataSource).getPosts(captorForceUpdateArgument.capture(), captorPostsCallbackArgument.capture());
        captorPostsCallbackArgument.getValue().onPostsLoaded(posts);

        assertEquals(FORRCE_UPDATE, captorForceUpdateArgument.getValue());
        verify(mockLoadPostsCallback).onPostsLoaded(posts);
        verify(mockLoadPostsCallback, never()).onError(anyInt());
    }

    @Test
    public void getPosts_shouldCallRemoteDataSource_whenNotAvailableLocallyAvailableRemotely() {
        List<Post> posts = DomainDataUtil.createPostList(1, 1);
        int statusCode = -1;

        postsRepository.getPosts(NOT_FORRCE_UPDATE, mockLoadPostsCallback);

        verify(mockLocalDataSource).getPosts(captorForceUpdateArgument.capture(), captorPostsCallbackArgument.capture());
        captorPostsCallbackArgument.getValue().onError(statusCode);

        verify(mockRemoteDataSource).getPosts(captorForceUpdateArgument.capture(), captorPostsCallbackArgument.capture());
        captorPostsCallbackArgument.getValue().onPostsLoaded(posts);

        assertEquals(NOT_FORRCE_UPDATE, captorForceUpdateArgument.getValue());
        verify(mockLoadPostsCallback).onPostsLoaded(posts);
        verify(mockLoadPostsCallback, never()).onError(anyInt());

        InOrder inOrderGetPostsOnDataSources = inOrder(mockLocalDataSource, mockRemoteDataSource);
        inOrderGetPostsOnDataSources.verify(mockLocalDataSource).getPosts(anyBoolean(), any(IPostsRepository.LoadPostsCallback.class));
        inOrderGetPostsOnDataSources.verify(mockRemoteDataSource).getPosts(anyBoolean(), any(IPostsRepository.LoadPostsCallback.class));

        InOrder inOrderUpdateLocalDataSource = inOrder(mockLocalDataSource);
        inOrderUpdateLocalDataSource.verify(mockLocalDataSource).deleteAllData();
        inOrderUpdateLocalDataSource.verify(mockLocalDataSource).savePosts(posts);
    }

    @Test
    public void getPosts_shouldCallError_whenNotAvailableLocallyNotAvailableRemotely() {
        int statusCode = -1;

        postsRepository.getPosts(NOT_FORRCE_UPDATE, mockLoadPostsCallback);

        verify(mockLocalDataSource).getPosts(captorForceUpdateArgument.capture(), captorPostsCallbackArgument.capture());
        captorPostsCallbackArgument.getValue().onError(statusCode);
        assertEquals(NOT_FORRCE_UPDATE, captorForceUpdateArgument.getValue());

        verify(mockRemoteDataSource).getPosts(captorForceUpdateArgument.capture(), captorPostsCallbackArgument.capture());
        captorPostsCallbackArgument.getValue().onError(statusCode);
        assertEquals(NOT_FORRCE_UPDATE, captorForceUpdateArgument.getValue());

        verify(mockLoadPostsCallback).onError(statusCode);
        verify(mockLoadPostsCallback, never()).onPostsLoaded(anyList());

        InOrder inOrderGetPostsOnDataSources = inOrder(mockLocalDataSource, mockRemoteDataSource);
        inOrderGetPostsOnDataSources.verify(mockLocalDataSource).getPosts(anyBoolean(), any(IPostsRepository.LoadPostsCallback.class));
        inOrderGetPostsOnDataSources.verify(mockRemoteDataSource).getPosts(anyBoolean(), any(IPostsRepository.LoadPostsCallback.class));
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