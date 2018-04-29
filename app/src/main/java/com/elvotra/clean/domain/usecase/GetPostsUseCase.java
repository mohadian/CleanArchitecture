package com.elvotra.clean.domain.usecase;

import com.elvotra.clean.domain.executor.IExecutor;
import com.elvotra.clean.domain.executor.IMainThread;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;
import com.elvotra.clean.domain.usecase.IGetPostsUseCase;
import com.elvotra.clean.domain.usecase.base.AbstractUseCase;

import java.util.List;

public class GetPostsUseCase extends AbstractUseCase implements IGetPostsUseCase {
    IGetPostsUseCase.Callback callback;
    IPostsRepository repository;

    public GetPostsUseCase(IExecutor threadIExecutor,
                           IMainThread IMainThread,
                           Callback callback, IPostsRepository repository) {
        super(threadIExecutor, IMainThread);
        this.callback = callback;
        this.repository = repository;
    }

    @Override
    public void run() {
        repository.getPosts(new IPostsRepository.LoadPostsCallback() {
            @Override
            public void onPostsLoaded(final List<Post> posts) {
                IMainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onPostsRetrieved(posts);
                    }
                });
            }

            @Override
            public void onError(final int statusCode) {
                IMainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onRetrievalFailed(statusCode);
                    }
                });
            }
        });
    }
}
