package com.elvotra.clean.presentation.presenter

import com.elvotra.clean.domain.executor.UseCaseHandler
import com.elvotra.clean.domain.model.Post
import com.elvotra.clean.domain.usecase.GetPostsUseCase
import com.elvotra.clean.domain.usecase.base.BaseUseCase
import com.elvotra.clean.presentation.contract.PostsContract
import com.elvotra.clean.presentation.model.PostViewItem
import com.elvotra.clean.presentation.model.mapper.PostViewItemMapper

import timber.log.Timber

class PostsPresenter(private val view: PostsContract.View, private val getPostsUseCase: GetPostsUseCase, private val useCaseHandler: UseCaseHandler) : PostsContract.IPostsPresenter {

    init {
        this.view.presenter = this
    }

    override fun openPostDetails(postId: Int) {
        view.showPostDetails(postId)
    }

    override fun resume() {
        executeGetPostsUseCase(false)
    }

    override fun loadPosts(forceUpdate: Boolean) {
        executeGetPostsUseCase(forceUpdate)
    }

    private fun executeGetPostsUseCase(forceUpdate: Boolean) {
        view.showProgress()

        val requestValue = GetPostsUseCase.RequestValues(forceUpdate)

        useCaseHandler.execute(getPostsUseCase, requestValue,
                object : BaseUseCase.UseCaseCallback<GetPostsUseCase.ResponseValue> {

                    override fun onSuccess(response: GetPostsUseCase.ResponseValue) {
                        if (!view.isActive) {
                            Timber.d("View is not active")

                            return
                        }

                        val posts = response.posts
                        processRetrievedData(posts)
                    }

                    override fun onError(statusCode: Int) {
                        if (!view.isActive) {
                            Timber.d("View is not active")

                            return
                        }

                        view.hideProgress()
                        view.showError("Server returned $statusCode error")
                    }
                })
    }

    private fun processRetrievedData(posts: List<Post>?) {
        view.hideProgress()
        if (posts == null || posts.isEmpty()) {
            view.showNoResults()
        } else {
            val postViewItems = PostViewItemMapper.instance().transform(posts)
            view.showPostsList(postViewItems)
        }
    }
}
