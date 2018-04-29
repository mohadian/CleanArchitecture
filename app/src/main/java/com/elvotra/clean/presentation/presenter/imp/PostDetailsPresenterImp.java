package com.elvotra.clean.presentation.presenter.imp;

import com.elvotra.clean.domain.executor.Executor;
import com.elvotra.clean.domain.executor.MainThread;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.PostsRepository;
import com.elvotra.clean.domain.usecase.GetPostUseCase;
import com.elvotra.clean.domain.usecase.imp.GetPostUseCaseImp;
import com.elvotra.clean.presentation.model.PostDetailsViewItem;
import com.elvotra.clean.presentation.model.mapper.PostDetailsViewItemMapper;
import com.elvotra.clean.presentation.presenter.PostDetailsPresenter;
import com.elvotra.clean.presentation.presenter.base.AbstractPresenter;

public class PostDetailsPresenterImp extends AbstractPresenter implements PostDetailsPresenter, GetPostUseCase.Callback {

    private View view;

    private PostsRepository postsRepository;

    private int postId;

    public PostDetailsPresenterImp(
            int postId,
            Executor executor,
            MainThread mainThread,
            View view,
            PostsRepository repository) {
        super(executor, mainThread);
        this.postId = postId;
        this.view = view;
        this.postsRepository = repository;

        this.view.setPresenter(this);
    }

    @Override
    public void onPostRetrieved(Post post) {
        view.hideProgress();
        if (post == null) {
            view.showNoResults();
        } else {
            PostDetailsViewItem postDetailsViewItem = PostDetailsViewItemMapper.getInstance().transform(post);
            view.showPostDetails(postDetailsViewItem);
        }
    }

    @Override
    public void loadPost(int postId) {
        this.postId = postId;
        executeGetPostsUseCase(postId);
    }

    @Override
    public void resume() {
        executeGetPostsUseCase(postId);
    }

    private void executeGetPostsUseCase(int postId) {
        view.showProgress();
        GetPostUseCase getPostsUseCase = new GetPostUseCaseImp(
                postId,
                executor,
                mainThread,
                this,
                postsRepository
        );

        getPostsUseCase.execute();
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
        view = null;
        postsRepository = null;
    }

    @Override
    public void onError(String message) {
        view.hideProgress();
        view.showError(message);
    }

    @Override
    public void onRetrievalFailed(int statusCode) {
        view.hideProgress();
        if (statusCode == -1) {
            view.showError("Cannot connect to the server. \nPlease try again later...");
        } else {
            view.showError("Server returned " + statusCode + " error");
        }
    }
}
