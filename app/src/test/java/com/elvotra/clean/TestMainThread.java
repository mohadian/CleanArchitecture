package com.elvotra.clean;

import com.elvotra.clean.domain.executor.IMainThread;

public class TestMainThread implements IMainThread {
    @Override
    public void post(Runnable runnable) {
        runnable.run();
    }
}
