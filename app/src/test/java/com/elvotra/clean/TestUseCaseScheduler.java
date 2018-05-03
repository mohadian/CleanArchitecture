package com.elvotra.clean;

import com.elvotra.clean.domain.executor.UseCaseScheduler;
import com.elvotra.clean.domain.usecase.base.BaseUseCase;

public class TestUseCaseScheduler implements UseCaseScheduler {
    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Override
    public <R extends BaseUseCase.ResponseValue> void notifyResponse(R response,
                                                                     BaseUseCase.UseCaseCallback<R> useCaseCallback) {
        useCaseCallback.onSuccess(response);
    }

    @Override
    public <R extends BaseUseCase.ResponseValue> void onError(int statusCode,
                                                              BaseUseCase.UseCaseCallback<R> useCaseCallback) {
        useCaseCallback.onError(statusCode);
    }
}
