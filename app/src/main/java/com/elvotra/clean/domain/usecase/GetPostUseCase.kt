package com.elvotra.clean.domain.usecase

import com.elvotra.clean.domain.model.Post
import com.elvotra.clean.domain.repository.IPostsRepository
import com.elvotra.clean.domain.usecase.base.BaseUseCase

class GetPostUseCase(private val repository: IPostsRepository) : BaseUseCase<GetPostUseCase.RequestValues, GetPostUseCase.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues?) {
        val postId = requestValues!!.postId
        repository.getPost(postId, object : IPostsRepository.LoadPostCallback {
            override fun onPostLoaded(post: Post) {
                val responseValue = GetPostUseCase.ResponseValue(post)
                useCaseCallback!!.onSuccess(responseValue)
            }

            override fun onError(statusCode: Int) {
                useCaseCallback!!.onError(statusCode)
            }
        })
    }

    class RequestValues(val postId: Int) : BaseUseCase.RequestValues

    class ResponseValue(val post: Post) : BaseUseCase.ResponseValue
}
