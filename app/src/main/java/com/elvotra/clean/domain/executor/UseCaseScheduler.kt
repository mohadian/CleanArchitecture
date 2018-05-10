package com.elvotra.clean.domain.executor

import com.elvotra.clean.domain.usecase.base.BaseUseCase

interface UseCaseScheduler {

    fun execute(runnable: Runnable)

    fun <V : BaseUseCase.ResponseValue> notifyResponse(response: V,
                                                       useCaseCallback: BaseUseCase.UseCaseCallback<V>)

    fun <V : BaseUseCase.ResponseValue> onError(statusCode: Int,
                                                useCaseCallback: BaseUseCase.UseCaseCallback<V>)
}
