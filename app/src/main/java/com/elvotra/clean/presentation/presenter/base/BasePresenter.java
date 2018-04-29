package com.elvotra.clean.presentation.presenter.base;

public interface BasePresenter {
    void resume();

    void pause();

    void stop();

    void destroy();

    void onError(String message);
}
