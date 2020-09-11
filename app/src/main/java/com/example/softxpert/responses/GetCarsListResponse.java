package com.example.softxpert.responses;

import com.example.softxpert.classes.Data;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetCarsListResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private ArrayList<Data> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
