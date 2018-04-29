package com.elvotra.clean.presentation.contract;

public interface BaseView <T extends BasePresenter> {
    void setPresenter(T presenter);

    void showProgress();

    void hideProgress();

    void showError(String message);
}
