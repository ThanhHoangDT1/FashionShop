package com.androidexam.fashionshop.Api;

import com.androidexam.fashionshop.Model.ApiResponse;
import com.androidexam.fashionshop.Model.ApiResponseDistrict;
import com.androidexam.fashionshop.Model.ApiResponseWard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiAddress {
    String BASE_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/";
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    @GET("district")
    Call<ApiResponseDistrict> getDistricts(@Header("Token") String token,@Query("province_id") int province_id);


    //c61b8d62-a18d-11ee-a6e6-e60958111f48
    @GET("province")
    Call<ApiResponse> getProvinces(@Header("Token") String token);

    @GET("ward")
    Call<ApiResponseWard> getWards(@Header("Token") String token, @Query("district_id") int district_id);

}
