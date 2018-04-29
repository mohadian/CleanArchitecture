package com.elvotra.clean.domain.executor;

import com.elvotra.clean.domain.usecase.base.AbstractUseCase;

public interface IExecutor {

    void execute(final AbstractUseCase useCase);
}
