package com.elvotra.clean.domain.executor;

public interface MainThread {

    void post(final Runnable runnable);
}
