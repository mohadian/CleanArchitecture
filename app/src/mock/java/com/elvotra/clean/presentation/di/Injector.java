package com.elvotra.clean.presentation.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.elvotra.clean.data.local.TypicodeDatabase;
import com.elvotra.clean.data.local.TypicodeLocalDataSource;
import com.elvotra.clean.data.remote.FakePostsRemoteDataSource;
import com.elvotra.clean.data.repository.PostsRepository;
import com.elvotra.clean.domain.executor.UseCaseHandler;
import com.elvotra.clean.domain.usecase.GetPostUseCase;
import com.elvotra.clean.domain.usecase.GetPostsUseCase;
import com.elvotra.clean.threading.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injector {

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

    public static GetPostsUseCase provideGetPostsUseCase(@NonNull Context context) {
        return new GetPostsUseCase(providePostsRepository(context));
    }

    public static GetPostUseCase provideGetPostUseCase(@NonNull Context context) {
        return new GetPostUseCase(providePostsRepository(context));
    }

    private static PostsRepository providePostsRepository(@NonNull Context context) {
        checkNotNull(context);
        TypicodeDatabase database = TypicodeDatabase.getInstance(context);
        return PostsRepository.getInstance(FakePostsRemoteDataSource.getInstance(),
                TypicodeLocalDataSource.getInstance(new AppExecutors(), database.typicodeDao()));
    }
}
