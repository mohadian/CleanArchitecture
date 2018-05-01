package com.elvotra.clean.presentation.presenter;

import com.elvotra.clean.domain.executor.IExecutor;
import com.elvotra.clean.domain.executor.IMainThread;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;
import com.elvotra.clean.domain.usecase.GetPostsUseCase;
import com.elvotra.clean.domain.usecase.IGetPostsUseCase;
import com.elvotra.clean.presentation.contract.AbstractPresenter;
import com.elvotra.clean.presentation.contract.PostsContract;
import com.elvotra.clean.presentation.model.PostViewItem;
import com.elvotra.clean.presentation.model.mapper.PostViewItemMapper;

import java.util.List;

public class PostsPresenter extends AbstractPresenter implements PostsContract.IPostsPresenter, IGetPostsUseCase.Callback {

    private PostsContract.View view;

    private IPostsRepository iPostsRepository;

    public PostsPresenter(IExecutor IExecutor,
                          IMainThread IMainThread,
                          PostsContract.View view,
                          IPostsRepository repository) {
        super(IExecutor, IMainThread);
        this.view = view;
        this.iPostsRepository = repository;

        this.view.setPresenter(this);
    }

    @Override
    public void loadPosts() {
        executeGetPostsUseCase();
    }

    @Override
    public void openPostDetails(int postId) {
        view.showPostDetails(postId);
    }

    @Override
    public void resume() {
        executeGetPostsUseCase();
    }

    private void executeGetPostsUseCase() {
        view.showProgress();
        IGetPostsUseCase getPostsUseCase = new GetPostsUseCase(
                iExecutor,
                iMainThread,
                this,
                iPostsRepository
        );

        getPostsUseCase.execute();
    }

    @Override
    public void destroy() {
        view = null;
        iPostsRepository = null;
    }

    @Override
    public void onError(String message) {
        view.hideProgress();
        view.showError(message);
    }

    @Override
    public void onPostsRetrieved(List<Post> posts) {
        view.hideProgress();
        if (posts.size() == 0) {
            view.showNoResults();
        } else {
            List<PostViewItem> postViewItems = PostViewItemMapper.getInstance().transform(posts);
            view.showPostsList(postViewItems);
        }
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
