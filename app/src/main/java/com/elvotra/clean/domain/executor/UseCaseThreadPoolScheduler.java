package com.elvotra.clean.domain.executor;

import android.os.Handler;

import com.elvotra.clean.domain.usecase.base.BaseUseCase;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UseCaseThreadPoolScheduler implements UseCaseScheduler {

    private static final int POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 4;
    private static final int TIMEOUT = 30;

    private ThreadPoolExecutor threadPoolExecutor;
    private final Handler handler = new Handler();

    UseCaseThreadPoolScheduler() {
        threadPoolExecutor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(POOL_SIZE));
    }

    @Override
    public void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    @Override
    public <V extends BaseUseCase.ResponseValue> void notifyResponse(final V response,
                                                                     final BaseUseCase.UseCaseCallback<V> useCaseCallback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                useCaseCallback.onSuccess(response);
            }
        });
    }

    @Override
    public <V extends BaseUseCase.ResponseValue> void onError(final int statusCode,
                                                              final BaseUseCase.UseCaseCallback<V> useCaseCallback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                useCaseCallback.onError(statusCode);
            }
        });
    }

}
