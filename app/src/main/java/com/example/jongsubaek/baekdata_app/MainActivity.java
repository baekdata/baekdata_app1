package com.example.jongsubaek.baekdata_app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    //retrofit 호출
    Retrofit retrofit;
    ApiService apiService;
    Call<ResponseBody> comment;

    TextView tv; // ui

    GeoVariable geoVariable = new GeoVariable(); // 위도 경도 가져오


    void callRetrofit(double n, double m){
        comment = apiService.getListRepos(n, m, 500, ApiService.APPKEY);
        comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    ResponseBody responseBody = response.body();

                    if(responseBody != null){
                        tv.setText(responseBody.getData().getCity()+" "+geoVariable.getLongitube());
                    }
                }catch(Exception e){

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.test_id);

        // 권한 물어서 권한안되어있으면 권한 셋팅해주기
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if(permissionCheck1 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET},1);

        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permissionCheck2 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},1);

        int permissionCheck3 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck3 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);


        // LocationManager 객체를 얻어온다
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
        } catch(SecurityException e){
            e.printStackTrace();
        }

        retrofit = new Retrofit.Builder().baseUrl(ApiService.BASEURL).addConverterFactory(GsonConverterFactory.create()).build();
        apiService = retrofit.create(ApiService.class);

        Log.d("test", "onLocationChanged, location:" + geoVariable.getLatitude());


//        comment = apiService.getListRepos(36.3753836, 127.3613915, 500, ApiService.APPKEY);
//        comment.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try{
//                    ResponseBody responseBody = response.body();
//
//                    if(responseBody != null){
//                        tv.setText(responseBody.getData().getCity()+" "+geoVariable.getLongitube());
//                    }
//                }catch(Exception e){
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });


    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
            geoVariable.setLatitude(latitude); // 클래스 변수에 위도 대입
            geoVariable.setLongitube(longitude);  // 클래스 변수에 경도 대입

            callRetrofit(latitude, longitude);
        }

        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };

}