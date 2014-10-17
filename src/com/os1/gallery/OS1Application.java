package com.os1.gallery;

import android.content.Intent;
import android.util.Log;
import com.os1.gallery.service.UploadService;

/**
 * Created by hanbowen on 2014/10/9.
 */
public class OS1Application extends android.app.Application {
    public static final String TAG = "OS1Application";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        startService();
    }
    private void startService(){
        Intent serviceIntent;
        serviceIntent = new Intent(this, UploadService.class);
        startService(serviceIntent);
    }
}
