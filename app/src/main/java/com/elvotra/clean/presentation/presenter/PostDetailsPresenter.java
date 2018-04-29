package com.elvotra.clean.presentation.presenter;

import com.elvotra.clean.domain.executor.IExecutor;
import com.elvotra.clean.domain.executor.IMainThread;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;
import com.elvotra.clean.domain.usecase.IGetPostUseCase;
import com.elvotra.clean.domain.usecase.GetPostUseCase;
import com.elvotra.clean.presentation.model.PostDetailsViewItem;
import com.elvotra.clean.presentation.model.mapper.PostDetailsViewItemMapper;
import com.elvotra.clean.presentation.presenter.IPostDetailsPresenter;
import com.elvotra.clean.presentation.presenter.base.AbstractPresenter;

public class PostDetailsPresenter extends AbstractPresenter implements IPostDetailsPresenter, IGetPostUseCase.Callback {

    private View view;

    private IPostsRepository IPostsRepository;

    private int postId;

    public PostDetailsPresenter(
            int postId,
            IExecutor IExecutor,
            IMainThread IMainThread,
            View view,
            IPostsRepository repository) {
        super(IExecutor, IMainThread);
        this.postId = postId;
        this.view = view;
        this.IPostsRepository = repository;

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
        IGetPostUseCase getPostsUseCase = new GetPostUseCase(
                postId,
                IExecutor,
                IMainThread,
                this,
                IPostsRepository
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
        IPostsRepository = null;
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
