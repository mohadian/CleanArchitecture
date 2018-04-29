package com.elvotra.clean.domain.executor;

public interface IMainThread {

    void post(final Runnable runnable);
}
