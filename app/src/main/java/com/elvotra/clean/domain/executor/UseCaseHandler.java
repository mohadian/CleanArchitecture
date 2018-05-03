package com.elvotra.clean.domain.executor;

import android.support.annotation.VisibleForTesting;

import com.elvotra.clean.domain.usecase.base.BaseUseCase;

public class UseCaseHandler {

    private static UseCaseHandler INSTANCE;
    private final UseCaseScheduler mUseCaseScheduler;

    public static UseCaseHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UseCaseHandler(new UseCaseThreadPoolScheduler());
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public UseCaseHandler(UseCaseScheduler useCaseScheduler) {
        mUseCaseScheduler = useCaseScheduler;
    }

    public <T extends BaseUseCase.RequestValues, R extends BaseUseCase.ResponseValue> void execute(
            final BaseUseCase<T, R> useCase, T values, BaseUseCase.UseCaseCallback<R> callback) {
        useCase.setRequestValues(values);
        useCase.setUseCaseCallback(new UiCallbackWrapper(callback, this));

        mUseCaseScheduler.execute(new Runnable() {
            @Override
            public void run() {
                useCase.run();
            }
        });
    }

    public <V extends BaseUseCase.ResponseValue> void notifyResponse(final V response,
                                                                     final BaseUseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.notifyResponse(response, useCaseCallback);
    }

    private <V extends BaseUseCase.ResponseValue> void notifyError(int statusCode,
                                                                   final BaseUseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.onError(statusCode, useCaseCallback);
    }

    private static final class UiCallbackWrapper<V extends BaseUseCase.ResponseValue> implements
            BaseUseCase.UseCaseCallback<V> {
        private final BaseUseCase.UseCaseCallback<V> useCaseCallback;
        private final UseCaseHandler useCaseHandler;

        public UiCallbackWrapper(BaseUseCase.UseCaseCallback<V> callback,
                                 UseCaseHandler useCaseHandler) {
            this.useCaseCallback = callback;
            this.useCaseHandler = useCaseHandler;
        }

        @Override
        public void onSuccess(V response) {
            useCaseHandler.notifyResponse(response, useCaseCallback);
        }

        @Override
        public void onError(int statusCode) {
            useCaseHandler.notifyError(statusCode, useCaseCallback);
        }
    }
}
