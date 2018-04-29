package com.elvotra.clean.presentation.contract;

interface BasePresenter {
    void resume();

    void destroy();

    void onError(String message);
}
