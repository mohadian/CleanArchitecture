package com.elvotra.clean.presentation.presenter;

import com.elvotra.clean.presentation.model.PostViewItem;
import com.elvotra.clean.presentation.presenter.base.BasePresenter;
import com.elvotra.clean.presentation.presenter.base.BaseView;

import java.util.List;

public interface PostsPresenter extends BasePresenter {

    interface View extends BaseView {

        void setPresenter(PostsPresenter presenter);

        void showPostsList(List<PostViewItem> postViewItems);

        void showPostDetails(long postId);

        void showNoResults();

        void showError(String message);

    }

    void loadPosts();

    void openPostDetails(long postId);

}
