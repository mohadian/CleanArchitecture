package com.elvotra.clean.presentation.presenter.imp;

import com.elvotra.clean.domain.executor.Executor;
import com.elvotra.clean.domain.executor.MainThread;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.PostsRepository;
import com.elvotra.clean.domain.usecase.GetPostsUseCase;
import com.elvotra.clean.domain.usecase.imp.GetPostsUseCaseImp;
import com.elvotra.clean.presentation.model.PostViewItem;
import com.elvotra.clean.presentation.model.mapper.PostViewItemMapper;
import com.elvotra.clean.presentation.presenter.PostsPresenter;
import com.elvotra.clean.presentation.presenter.base.AbstractPresenter;

import java.util.List;

public class PostsPresenterImp extends AbstractPresenter implements PostsPresenter, GetPostsUseCase.Callback {

    private PostsPresenter.View view;

    private PostsRepository postsRepository;

    public PostsPresenterImp(Executor executor,
                             MainThread mainThread,
                             View view,
                             PostsRepository repository) {
        super(executor, mainThread);
        this.view = view;
        this.postsRepository = repository;

        this.view.setPresenter(this);

    }


    @Override
    public void loadPosts() {
        executeGetPostsUseCase();
    }

    @Override
    public void openPostDetails(long postId) {
        view.showPostDetails(postId);
    }

    @Override
    public void resume() {
        executeGetPostsUseCase();
    }

    private void executeGetPostsUseCase() {
        view.showProgress();
        GetPostsUseCase getPostsUseCase = new GetPostsUseCaseImp(
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
