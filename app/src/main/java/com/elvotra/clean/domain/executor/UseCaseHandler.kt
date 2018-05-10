package com.elvotra.clean.domain.executor

import com.elvotra.clean.domain.usecase.base.BaseUseCase

class UseCaseHandler
constructor(private val useCaseScheduler: UseCaseScheduler) {

    fun <T : BaseUseCase.RequestValues, R : BaseUseCase.ResponseValue> execute(
            useCase: BaseUseCase<T, R>, values: T, callback: BaseUseCase.UseCaseCallback<R>) {
        useCase.requestValues = values
        useCase.useCaseCallback = UiCallbackWrapper(callback, this)

        useCaseScheduler.execute(runnable = Runnable { useCase.run() })
    }

    fun <V : BaseUseCase.ResponseValue> notifyResponse(response: V,
                                                       useCaseCallback: BaseUseCase.UseCaseCallback<V>) {
        useCaseScheduler.notifyResponse(response, useCaseCallback)
    }

    private fun <V : BaseUseCase.ResponseValue> notifyError(statusCode: Int,
                                                            useCaseCallback: BaseUseCase.UseCaseCallback<V>) {
        useCaseScheduler.onError(statusCode, useCaseCallback)
    }

    private class UiCallbackWrapper<V : BaseUseCase.ResponseValue>(private val useCaseCallback: BaseUseCase.UseCaseCallback<V>,
                                                                   private val useCaseHandler: UseCaseHandler) : BaseUseCase.UseCaseCallback<V> {

        override fun onSuccess(response: V) {
            useCaseHandler.notifyResponse(response, useCaseCallback)
        }

        override fun onError(statusCode: Int) {
            useCaseHandler.notifyError(statusCode, useCaseCallback)
        }
    }

    companion object {

        private var INSTANCE: UseCaseHandler? = null

        fun instance(): UseCaseHandler {
            return INSTANCE ?: UseCaseHandler(UseCaseThreadPoolScheduler())
                    .apply { INSTANCE = this }
        }
    }
}
