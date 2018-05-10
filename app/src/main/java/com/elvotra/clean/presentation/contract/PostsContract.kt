package com.elvotra.clean.presentation.contract

import com.elvotra.clean.presentation.model.PostViewItem

interface PostsContract {

    interface View : BaseView<IPostsPresenter> {

        val isActive: Boolean
        fun showPostsList(postViewItems: List<PostViewItem>)

        fun showPostDetails(postId: Int)

        fun showNoResults()
    }

    interface IPostsPresenter : BasePresenter {
        fun loadPosts(forceUpdate: Boolean)

        fun openPostDetails(postId: Int)
    }
}
