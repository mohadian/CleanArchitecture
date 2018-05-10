package com.elvotra.clean.domain.executor

import android.os.Handler

import com.elvotra.clean.domain.usecase.base.BaseUseCase

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class UseCaseThreadPoolScheduler internal constructor() : UseCaseScheduler {

    private val threadPoolExecutor: ThreadPoolExecutor
    private val handler = Handler()

    init {
        threadPoolExecutor = ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT.toLong(),
                TimeUnit.SECONDS, ArrayBlockingQueue(POOL_SIZE))
    }

    override fun execute(runnable: Runnable) {
        threadPoolExecutor.execute(runnable)
    }

    override fun <V : BaseUseCase.ResponseValue> notifyResponse(response: V,
                                                                useCaseCallback: BaseUseCase.UseCaseCallback<V>) {
        handler.post { useCaseCallback.onSuccess(response) }
    }

    override fun <V : BaseUseCase.ResponseValue> onError(statusCode: Int,
                                                         useCaseCallback: BaseUseCase.UseCaseCallback<V>) {
        handler.post { useCaseCallback.onError(statusCode) }
    }

    companion object {
        private val POOL_SIZE = 2
        private val MAX_POOL_SIZE = 4
        private val TIMEOUT = 30
    }

}
