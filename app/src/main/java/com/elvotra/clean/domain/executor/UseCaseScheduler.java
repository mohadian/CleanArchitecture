package com.elvotra.clean.domain.executor;

import com.elvotra.clean.domain.usecase.base.BaseUseCase;

public interface UseCaseScheduler {

    void execute(Runnable runnable);

    <V extends BaseUseCase.ResponseValue> void notifyResponse(final V response,
                                                              final BaseUseCase.UseCaseCallback<V> useCaseCallback);

    <V extends BaseUseCase.ResponseValue> void onError(int statusCode,
                                                       final BaseUseCase.UseCaseCallback<V> useCaseCallback);
}
