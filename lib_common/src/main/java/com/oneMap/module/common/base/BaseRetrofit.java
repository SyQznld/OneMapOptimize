package com.oneMap.module.common.base;


import com.oneMap.module.common.global.ApiService;
import com.oneMap.module.common.global.Global;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class BaseRetrofit {

    private static ApiService apiService;


    public static Retrofit getRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(Global.BASE_URL_IP)

                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder.build();
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getRetrofit().create(ApiService.class);
        }
        return apiService;
    }


}
