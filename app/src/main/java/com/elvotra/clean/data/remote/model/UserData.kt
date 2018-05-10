package com.elvotra.clean.data.remote.model

data class UserData(val id: Int = 0,
                    val name: String? = null,
                    val username: String? = null,
                    val email: String? = null,
                    val address: UserAddress? = null,
                    val phone: String? = null,
                    val website: String? = null,
                    val company: UserCompany? = null)