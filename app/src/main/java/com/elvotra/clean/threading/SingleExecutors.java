package com.elvotra.clean.threading;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

public class SingleExecutors extends AppExecutors {
    private static Executor instant = new Executor() {
        @Override
        public void execute(@NonNull Runnable command) {
            command.run();
        }
    };

    public SingleExecutors() {
        super(instant, instant);
    }
}