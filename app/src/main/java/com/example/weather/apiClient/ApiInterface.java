package com.example.weather.apiClient;

import com.example.weather.currentModel.WeatherRes;
import com.example.weather.weatherList.WeatherListResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("data/2.5/forecast")
    Call<WeatherListResp> getWeatherList(@Query("q") String q, @Query("appid") String appid, @Query("units") String units);

    @GET("data/2.5/onecall")
    Call<WeatherRes> getWeatherCall(@Query("lat") double lat,
                                    @Query("lon") double lon,
                                    @Query("exclude") String exclude,
                                    @Query("appid") String appid

    );


}
