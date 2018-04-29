package com.elvotra.clean.threading;

import android.os.Handler;
import android.os.Looper;

import com.elvotra.clean.domain.executor.IMainThread;

public class MainThreadImp implements IMainThread {

    private static IMainThread sIMainThread;

    private Handler mHandler;

    private MainThreadImp() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    public static IMainThread getInstance() {
        if (sIMainThread == null) {
            sIMainThread = new MainThreadImp();
        }

        return sIMainThread;
    }
}
