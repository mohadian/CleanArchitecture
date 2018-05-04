package com.elvotra.clean.presentation.contract;

import com.elvotra.clean.presentation.model.PostViewItem;

import java.util.List;

public interface PostsContract {

    interface View extends BaseView<IPostsPresenter> {
        void showPostsList(List<PostViewItem> postViewItems);

        void showPostDetails(int postId);

        void showNoResults();

        boolean isActive();
    }

    interface IPostsPresenter extends BasePresenter {
        void loadPosts(boolean forceUpdate);

        void openPostDetails(int postId);
    }
}
