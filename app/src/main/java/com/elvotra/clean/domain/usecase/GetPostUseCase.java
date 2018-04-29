package com.elvotra.clean.domain.usecase;

import com.elvotra.clean.domain.executor.IExecutor;
import com.elvotra.clean.domain.executor.IMainThread;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;
import com.elvotra.clean.domain.usecase.IGetPostUseCase;
import com.elvotra.clean.domain.usecase.base.AbstractUseCase;

public class GetPostUseCase extends AbstractUseCase implements IGetPostUseCase {
    IGetPostUseCase.Callback callback;
    IPostsRepository repository;
    int postId;

    public GetPostUseCase(
            int postId,
            IExecutor threadIExecutor,
            IMainThread IMainThread,
            IGetPostUseCase.Callback callback,
            IPostsRepository repository) {
        super(threadIExecutor, IMainThread);
        this.postId = postId;
        this.callback = callback;
        this.repository = repository;
    }

    @Override
    public void run() {
        repository.getPost(postId, new IPostsRepository.LoadPostCallback() {
            @Override
            public void onPostLoaded(final Post post) {
                IMainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onPostRetrieved(post);
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
