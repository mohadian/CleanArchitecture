package com.elvotra.clean.domain.executor;

import com.elvotra.clean.domain.usecase.base.AbstractUseCase;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadExecutor implements IExecutor {

    private static volatile ThreadExecutor INSTANCE;

    private static final int CORE_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 5;
    private static final int KEEP_ALIVE_TIME = 120;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<Runnable>();

    private ThreadPoolExecutor mThreadPoolExecutor;

    private ThreadExecutor() {
        long keepAlive = KEEP_ALIVE_TIME;
        mThreadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                keepAlive,
                TIME_UNIT,
                WORK_QUEUE);
    }

    @Override
    public void execute(final AbstractUseCase useCase) {
        mThreadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                // run the main logic
                useCase.run();

                // mark it as finished
                useCase.onFinished();
            }
        });
    }

    public static IExecutor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThreadExecutor();
        }

        return INSTANCE;
    }
}
