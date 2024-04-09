package com.xuanthongn.catholicwallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Start the wallpaper service
        Intent serviceIntent = new Intent(this, WallpaperService.class);
        serviceIntent.putExtra("inputExtra", "Đang chạy dịch vụ cập nhật hình nền.");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        Notify the service still run in background
    }
}