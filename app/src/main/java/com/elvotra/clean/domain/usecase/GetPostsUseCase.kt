package com.elvotra.clean.domain.usecase

import com.elvotra.clean.domain.model.Post
import com.elvotra.clean.domain.repository.IPostsRepository
import com.elvotra.clean.domain.usecase.base.BaseUseCase

import java.util.ArrayList

class GetPostsUseCase(private val repository: IPostsRepository) : BaseUseCase<GetPostsUseCase.RequestValues, GetPostsUseCase.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues?) {
        repository.getPosts(requestValues!!.isForceUpdate, object : IPostsRepository.LoadPostsCallback {
            override fun onPostsLoaded(posts: ArrayList<Post>) {
                val responseValue = ResponseValue(posts)
                useCaseCallback!!.onSuccess(responseValue)
            }

            override fun onError(statusCode: Int) {
                useCaseCallback!!.onError(statusCode)
            }
        })
    }

    class RequestValues(val isForceUpdate: Boolean) : BaseUseCase.RequestValues

    class ResponseValue(val posts: List<Post>) : BaseUseCase.ResponseValue
}
