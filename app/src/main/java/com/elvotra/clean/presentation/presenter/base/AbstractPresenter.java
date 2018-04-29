package com.elvotra.clean.presentation.presenter.base;

import com.elvotra.clean.domain.executor.Executor;
import com.elvotra.clean.domain.executor.MainThread;

public abstract class AbstractPresenter {
    protected Executor executor;
    protected MainThread mainThread;

    public AbstractPresenter(Executor executor, MainThread mainThread) {
        this.executor = executor;
        this.mainThread = mainThread;
    }
}
