package com.elvotra.clean.presentation.contract;

import com.elvotra.clean.presentation.model.PostDetailsViewItem;

public interface PostDetailsContract {

    interface View extends BaseView<IPostDetailsPresenter> {

        void showPostDetails(PostDetailsViewItem postDetailsViewItem);

        void showNoResults();
    }

    interface IPostDetailsPresenter extends BasePresenter {

        void loadPost(int postId);
    }

}
