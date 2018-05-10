package com.elvotra.clean.presentation.contract

interface BaseView<T> {
    var presenter: T

    fun showProgress()

    fun hideProgress()

    fun showError(message: String)
}
