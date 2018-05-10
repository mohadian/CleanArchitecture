package com.elvotra.clean.presentation.di

import android.content.Context

import com.elvotra.clean.data.local.TypicodeDatabase
import com.elvotra.clean.data.local.TypicodeLocalDataSource
import com.elvotra.clean.data.remote.TypicodeRemoteDataSource
import com.elvotra.clean.data.repository.PostsRepository
import com.elvotra.clean.domain.executor.UseCaseHandler
import com.elvotra.clean.domain.usecase.GetPostUseCase
import com.elvotra.clean.domain.usecase.GetPostsUseCase
import com.elvotra.clean.threading.AppExecutors

import com.google.common.base.Preconditions.checkNotNull

object Injector {

    fun provideUseCaseHandler(): UseCaseHandler {
        return UseCaseHandler.instance()
    }

    fun provideGetPostsUseCase(context: Context): GetPostsUseCase {
        return GetPostsUseCase(providePostsRepository(context)!!)
    }

    fun provideGetPostUseCase(context: Context): GetPostUseCase {
        return GetPostUseCase(providePostsRepository(context)!!)
    }

    private fun providePostsRepository(context: Context): PostsRepository? {
        checkNotNull(context)
        val database = TypicodeDatabase.getInstance(context)
        return PostsRepository.getInstance(TypicodeRemoteDataSource.instance,
                TypicodeLocalDataSource.getInstance(AppExecutors(), database!!.typicodeDao())!!)
    }
}
