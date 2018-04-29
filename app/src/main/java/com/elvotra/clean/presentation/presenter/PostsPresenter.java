package com.elvotra.clean.presentation.presenter;

import com.elvotra.clean.presentation.model.PostViewItem;
import com.elvotra.clean.presentation.presenter.base.BasePresenter;
import com.elvotra.clean.presentation.presenter.base.BaseView;

import java.util.List;

public interface PostsPresenter extends BasePresenter {

    interface View extends BaseView {

        void setPresenter(PostsPresenter presenter);

        void showPostsList(List<PostViewItem> postViewItems);

        void showPostDetails(int postId);

        void showNoResults();

        void showError(String message);

    }

    void loadPosts();

    void openPostDetails(int postId);

}
