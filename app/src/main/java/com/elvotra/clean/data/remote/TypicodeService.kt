package com.elvotra.clean.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TypicodeService {

    fun <S> createService(baseUri: String, serviceClass: Class<S>): S {
        val builder = Retrofit.Builder()
                .baseUrl(baseUri)
                .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()

        return retrofit.create(serviceClass)

    }
}
