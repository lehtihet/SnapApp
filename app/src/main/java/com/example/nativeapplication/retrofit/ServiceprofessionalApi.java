package com.example.nativeapplication.retrofit;

import com.example.nativeapplication.model.ServiceProfessional;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceprofessionalApi {

    @GET("allserviceprofessionals")
    Call<List<ServiceProfessional>> getServiceProfessionals();

    @POST("saveserviceProfessional")
    Call<Object> save(@Body ServiceProfessional serviceprofessional);

}
