/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elvotra.clean;

import com.elvotra.clean.domain.executor.UseCaseScheduler;
import com.elvotra.clean.domain.usecase.base.BaseUseCase;

public class TestUseCaseScheduler implements UseCaseScheduler {
    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Override
    public <R extends BaseUseCase.ResponseValue> void notifyResponse(R response,
                                                                     BaseUseCase.UseCaseCallback<R> useCaseCallback) {
        useCaseCallback.onSuccess(response);
    }

    @Override
    public <R extends BaseUseCase.ResponseValue> void onError(int statusCode,
                                                              BaseUseCase.UseCaseCallback<R> useCaseCallback) {
        useCaseCallback.onError(statusCode);
    }
}
