package com.elvotra.clean.presentation.contract

import com.elvotra.clean.presentation.model.PostDetailsViewItem

interface PostDetailsContract {

    interface View : BaseView<IPostDetailsPresenter> {

        val isActive: Boolean
        fun showPostDetails(postDetailsViewItem: PostDetailsViewItem)

        fun showNoResults()
    }

    interface IPostDetailsPresenter : BasePresenter {
        fun loadPost(postId: Int)
    }

}
