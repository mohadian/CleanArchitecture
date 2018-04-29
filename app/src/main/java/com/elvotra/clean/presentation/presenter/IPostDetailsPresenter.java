package com.elvotra.clean.presentation.presenter;

import com.elvotra.clean.presentation.model.PostDetailsViewItem;
import com.elvotra.clean.presentation.presenter.base.BasePresenter;
import com.elvotra.clean.presentation.presenter.base.BaseView;

public interface IPostDetailsPresenter extends BasePresenter {

    interface View extends BaseView {

        void setPresenter(IPostDetailsPresenter presenter);

        void showPostDetails(PostDetailsViewItem postDetailsViewItem);

        void showNoResults();

        void showError(String message);

    }

    void loadPost(int postId);

}
