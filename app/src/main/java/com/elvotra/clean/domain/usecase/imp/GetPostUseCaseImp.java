package com.elvotra.clean.domain.usecase.imp;

import com.elvotra.clean.domain.executor.Executor;
import com.elvotra.clean.domain.executor.MainThread;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.PostsRepository;
import com.elvotra.clean.domain.usecase.GetPostUseCase;
import com.elvotra.clean.domain.usecase.base.AbstractUseCase;

public class GetPostUseCaseImp extends AbstractUseCase implements GetPostUseCase {
    GetPostUseCase.Callback callback;
    PostsRepository repository;
    int postId;

    public GetPostUseCaseImp(
            int postId,
            Executor threadExecutor,
            MainThread mainThread,
            GetPostUseCase.Callback callback,
            PostsRepository repository) {
        super(threadExecutor, mainThread);
        this.postId = postId;
        this.callback = callback;
        this.repository = repository;
    }

    @Override
    public void run() {
        repository.getPost(postId, new PostsRepository.LoadPostCallback() {
            @Override
            public void onPostLoaded(final Post post) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onPostRetrieved(post);
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
