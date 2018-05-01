package com.elvotra.clean.presentation.contract;

import com.elvotra.clean.domain.executor.IExecutor;
import com.elvotra.clean.domain.executor.IMainThread;

public abstract class AbstractPresenter {
    protected IExecutor iExecutor;
    protected IMainThread iMainThread;

    public AbstractPresenter(IExecutor IExecutor, IMainThread IMainThread) {
        this.iExecutor = IExecutor;
        this.iMainThread = IMainThread;
    }
}
