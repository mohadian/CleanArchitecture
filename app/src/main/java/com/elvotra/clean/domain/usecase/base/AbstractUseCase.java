package com.elvotra.clean.domain.usecase.base;

import com.elvotra.clean.domain.executor.IExecutor;
import com.elvotra.clean.domain.executor.IMainThread;

public abstract class AbstractUseCase implements IUseCase {

    protected IExecutor threadIExecutor;
    protected IMainThread IMainThread;

    protected volatile boolean isCanceled;
    protected volatile boolean isRunning;

    public AbstractUseCase(IExecutor threadIExecutor, IMainThread IMainThread) {
        this.threadIExecutor = threadIExecutor;
        this.IMainThread = IMainThread;
    }

    public abstract void run();

    public void cancel() {
        isCanceled = true;
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void onFinished() {
        isRunning = false;
        isCanceled = false;
    }

    public void execute() {

        // mark this interactor as running
        this.isRunning = true;

        // start running this interactor in a background thread
        threadIExecutor.execute(this);
    }

}
