package com.elvotra.clean.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TypicodeService {

    private static Retrofit.Builder builder;

    private static Retrofit retrofit;

    public static <S> S createService(String baseUri, Class<S> serviceClass) {

        builder = new Retrofit.Builder()
                .baseUrl(baseUri)
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();


        return retrofit.create(serviceClass);

    }
}
