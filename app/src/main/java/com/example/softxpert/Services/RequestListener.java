package com.example.softxpert.Services;

public interface RequestListener {
    void onSuccess(Object object);
    void  onFailure(int errorCode);


}
