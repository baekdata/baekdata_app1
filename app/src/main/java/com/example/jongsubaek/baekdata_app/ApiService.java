package com.example.jongsubaek.baekdata_app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    //베이스 url
    static final String BASEURL = "http://api.airvisual.com/";
    static final String APPKEY = "BGscknSy6Mqstnnr4";

//    http://api.airvisual.com/v2/nearest_city?lat=36.3753836&lon=127.3613915&rad=500&key=BGscknSy6Mqstnnr4

    @GET("v2/nearest_city")
    Call<ResponseBody> getListRepos(@Query("lat") double lat, @Query("lon") double lon, @Query("rad") int rad,
                                        @Query("key") String key);
}
