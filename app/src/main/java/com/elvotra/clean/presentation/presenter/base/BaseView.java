package com.elvotra.clean.presentation.presenter.base;

public interface BaseView {

    void showProgress();

    void hideProgress();

    void showError(String message);
}