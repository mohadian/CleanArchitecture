package com.elvotra.clean.presentation.presenter

import com.elvotra.clean.domain.executor.UseCaseHandler
import com.elvotra.clean.domain.model.Post
import com.elvotra.clean.domain.usecase.GetPostUseCase
import com.elvotra.clean.domain.usecase.base.BaseUseCase
import com.elvotra.clean.presentation.contract.PostDetailsContract
import com.elvotra.clean.presentation.model.mapper.PostDetailsViewItemMapper
import timber.log.Timber

class PostDetailsPresenter(private val view: PostDetailsContract.View, private val getPostUseCase: GetPostUseCase, private val useCaseHandler: UseCaseHandler) : PostDetailsContract.IPostDetailsPresenter {
    private var postId: Int = 0

    init {
        this.view.presenter = this
    }

    override fun loadPost(postId: Int) {
        this.postId = postId
        executeGetPostsUseCase(postId)
    }

    override fun resume() {
        executeGetPostsUseCase(postId)
    }

    private fun executeGetPostsUseCase(postId: Int) {
        view.showProgress()

        val requestValue = GetPostUseCase.RequestValues(postId)

        useCaseHandler.execute(getPostUseCase, requestValue,
                object : BaseUseCase.UseCaseCallback<GetPostUseCase.ResponseValue> {

                    override fun onSuccess(response: GetPostUseCase.ResponseValue) {
                        if (!view.isActive) {
                            Timber.d("View is not active")

                            return
                        }

                        val post = response.post
                        processRetrievedData(post)
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

    private fun processRetrievedData(post: Post?) {
        view.hideProgress()
        if (post == null) {
            view.showNoResults()
        } else {
            val postDetailsViewItem = PostDetailsViewItemMapper.instance().transform(post)
            view.showPostDetails(postDetailsViewItem)
        }
    }
}
