package com.example.jongsubaek.baekdata_app.network;

import com.example.jongsubaek.baekdata_app.data.AirPollution;

public class ResponseBody {
    private String status;
    private AirPollution data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AirPollution getData() {
        return data;
    }

    public void setData(AirPollution data) {
        this.data = data;
    }
}