package com.elvotra.clean.presentation.contract;

import com.elvotra.clean.domain.executor.IExecutor;
import com.elvotra.clean.domain.executor.IMainThread;

public abstract class AbstractPresenter {
    protected IExecutor IExecutor;
    protected IMainThread IMainThread;

    public AbstractPresenter(IExecutor IExecutor, IMainThread IMainThread) {
        this.IExecutor = IExecutor;
        this.IMainThread = IMainThread;
    }
}
