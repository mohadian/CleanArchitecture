package com.elvotra.clean.domain.usecase.base;

import com.elvotra.clean.domain.executor.IExecutor;
import com.elvotra.clean.domain.executor.IMainThread;

public abstract class AbstractUseCase implements IUseCase {

    protected IExecutor threadIExecutor;
    protected IMainThread iMainThread;

    protected volatile boolean isCanceled;
    protected volatile boolean isRunning;

    public AbstractUseCase(IExecutor threadIExecutor, IMainThread IMainThread) {
        this.threadIExecutor = threadIExecutor;
        this.iMainThread = IMainThread;
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
        this.isRunning = true;

        threadIExecutor.execute(this);
    }

}
