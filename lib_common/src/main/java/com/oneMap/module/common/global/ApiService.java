package com.oneMap.module.common.global;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiService {
    /**
     * 用户登录
     */
    @FormUrlEncoded
    @POST(Global.URL_HOUZHUI)
    Observable<ResponseBody> getLogin(
            @Field("flag") String flag,
            @Field("username") String username,
            @Field("userpass") String userpass);


    /**
     * GET
     */

    @GET(Global.URL_HOUZHUI)
    Observable<ResponseBody> testFrame(@Query("RegistrationID") String registrationID);


}
