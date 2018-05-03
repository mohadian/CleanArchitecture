package com.elvotra.clean.domain.usecase.base;

public abstract class BaseUseCase<Q extends BaseUseCase.RequestValues, P extends BaseUseCase.ResponseValue>{
    private Q requestValues;
    private UseCaseCallback<P> useCaseCallback;

    public void setRequestValues(Q requestValues) {
        this.requestValues = requestValues;
    }

    public Q getRequestValues() {
        return requestValues;
    }

    public UseCaseCallback<P> getUseCaseCallback() {
        return useCaseCallback;
    }

    public void setUseCaseCallback(UseCaseCallback<P> useCaseCallback) {
        this.useCaseCallback = useCaseCallback;
    }

    public void run() {
        executeUseCase(requestValues);
    }

    protected abstract void executeUseCase(Q requestValues);

    public interface RequestValues {
    }

    public interface ResponseValue {
    }

    public interface UseCaseCallback<R> {
        void onSuccess(R response);
        void onError(int statusCode);
    }
}
