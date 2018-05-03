package com.elvotra.clean.presentation.presenter;

import com.elvotra.clean.domain.executor.UseCaseHandler;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.usecase.GetPostUseCase;
import com.elvotra.clean.domain.usecase.base.BaseUseCase;
import com.elvotra.clean.presentation.contract.PostDetailsContract;
import com.elvotra.clean.presentation.model.PostDetailsViewItem;
import com.elvotra.clean.presentation.model.mapper.PostDetailsViewItemMapper;

public class PostDetailsPresenter implements PostDetailsContract.IPostDetailsPresenter {

    private PostDetailsContract.View view;
    private GetPostUseCase getPostUseCase;
    private final UseCaseHandler useCaseHandler;
    private int postId;

    public PostDetailsPresenter(PostDetailsContract.View view, GetPostUseCase getPostUseCase, UseCaseHandler mUseCaseHandler) {
        this.view = view;
        this.getPostUseCase = getPostUseCase;
        this.useCaseHandler = mUseCaseHandler;
        this.view.setPresenter(this);
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

        GetPostUseCase.RequestValues requestValue = new GetPostUseCase.RequestValues(postId);

        useCaseHandler.execute(getPostUseCase, requestValue,
                new BaseUseCase.UseCaseCallback<GetPostUseCase.ResponseValue>() {

                    @Override
                    public void onSuccess(GetPostUseCase.ResponseValue response) {
                        if (!view.isActive()) {
                            return;
                        }

                        Post post = response.getPost();
                        processRetrievedData(post);
                    }

                    @Override
                    public void onError(int statusCode) {
                        if (!view.isActive()) {
                            return;
                        }

                        view.hideProgress();
                        view.showError("Server returned " + statusCode + " error");
                    }
                });
    }

    private void processRetrievedData(Post post) {
        view.hideProgress();
        if (post == null) {
            view.showNoResults();
        } else {
            PostDetailsViewItem postDetailsViewItem = PostDetailsViewItemMapper.getInstance().transform(post);
            view.showPostDetails(postDetailsViewItem);
        }
    }
}
