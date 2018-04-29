package com.elvotra.clean.domain.usecase.imp;

import com.elvotra.clean.domain.executor.Executor;
import com.elvotra.clean.domain.executor.MainThread;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.PostsRepository;
import com.elvotra.clean.domain.usecase.GetPostsUseCase;
import com.elvotra.clean.domain.usecase.base.AbstractUseCase;

import java.util.List;

public class GetPostsUseCaseImp extends AbstractUseCase implements GetPostsUseCase {
    GetPostsUseCase.Callback callback;
    PostsRepository repository;

    public GetPostsUseCaseImp(Executor threadExecutor,
                              MainThread mainThread,
                              Callback callback, PostsRepository repository) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
    }

    @Override
    public void run() {
        repository.getPosts(new PostsRepository.LoadPostsCallback() {
            @Override
            public void onPostsLoaded(final List<Post> posts) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onPostsRetrieved(posts);
                    }
                });
            }

            @Override
            public void onError(final int statusCode) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onRetrievalFailed(statusCode);
                    }
                });
            }
        });
    }
}
