package com.elvotra.clean.domain.usecase.base

abstract class BaseUseCase<Q : BaseUseCase.RequestValues, P : BaseUseCase.ResponseValue> {
    var requestValues: Q? = null
    var useCaseCallback: UseCaseCallback<P>? = null

    fun run() {
        executeUseCase(requestValues)
    }

    protected abstract fun executeUseCase(requestValues: Q?)

    interface RequestValues

    interface ResponseValue

    interface UseCaseCallback<R> {
        fun onSuccess(response: R)
        fun onError(statusCode: Int)
    }
}
