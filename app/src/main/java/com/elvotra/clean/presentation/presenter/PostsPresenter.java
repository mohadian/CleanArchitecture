package com.elvotra.clean.presentation.presenter;

import com.elvotra.clean.domain.executor.UseCaseHandler;
import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.usecase.GetPostsUseCase;
import com.elvotra.clean.domain.usecase.base.BaseUseCase;
import com.elvotra.clean.presentation.contract.PostsContract;
import com.elvotra.clean.presentation.model.PostViewItem;
import com.elvotra.clean.presentation.model.mapper.PostViewItemMapper;

import java.util.List;

public class PostsPresenter implements PostsContract.IPostsPresenter {

    private PostsContract.View view;
    private GetPostsUseCase getPostsUseCase;
    private final UseCaseHandler useCaseHandler;

    public PostsPresenter(PostsContract.View view, GetPostsUseCase getPostsUseCase, UseCaseHandler mUseCaseHandler) {
        this.view = view;
        this.getPostsUseCase = getPostsUseCase;
        this.useCaseHandler = mUseCaseHandler;
        this.view.setPresenter(this);
    }

    @Override
    public void openPostDetails(int postId) {
        view.showPostDetails(postId);
    }

    @Override
    public void resume() {
        executeGetPostsUseCase(false);
    }

    @Override
    public void loadPosts(boolean forceUpdate) {
        executeGetPostsUseCase(forceUpdate);
    }

    private void executeGetPostsUseCase(boolean forceUpdate) {
        view.showProgress();

        GetPostsUseCase.RequestValues requestValue = new GetPostsUseCase.RequestValues(forceUpdate);

        useCaseHandler.execute(getPostsUseCase, requestValue,
                new BaseUseCase.UseCaseCallback<GetPostsUseCase.ResponseValue>() {

                    @Override
                    public void onSuccess(GetPostsUseCase.ResponseValue response) {
                        if (!view.isActive()) {
                            return;
                        }

                        List<Post> posts = response.getPosts();
                        processRetrievedData(posts);
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

    private void processRetrievedData(List<Post> posts) {
        view.hideProgress();
        if (posts == null || posts.isEmpty()) {
            view.showNoResults();
        } else {
            List<PostViewItem> postViewItems = PostViewItemMapper.getInstance().transform(posts);
            view.showPostsList(postViewItems);
        }
    }
}
