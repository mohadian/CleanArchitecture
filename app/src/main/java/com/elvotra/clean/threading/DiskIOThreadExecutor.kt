package com.elvotra.clean.threading

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DiskIOThreadExecutor : Executor {

    private val executor: Executor

    init {
        executor = Executors.newSingleThreadExecutor()
    }

    override fun execute(command: Runnable) {
        executor.execute(command)
    }
}