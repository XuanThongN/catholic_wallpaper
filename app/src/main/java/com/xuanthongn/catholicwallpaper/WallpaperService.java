package com.xuanthongn.catholicwallpaper;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xuanthongn.catholicwallpaper.models.Wallpaper;
import com.xuanthongn.catholicwallpaper.tasks.GetWallpapersTask;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class WallpaperService extends Service {

    private Target target;
    private final String apiUrl = "https://api.unsplash.com/search/photos";

    private final Handler handler = new Handler();

    public WallpaperService() {
    }

    private final Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Call the method to update the wallpaper
            updateWallpaper();
            // Repeat this runnable code block again every minute
            handler.postDelayed(this, 60 * 1000);
        }
    };

    private void updateWallpaper() {
        GetWallpapersTask getWallpapersTask = new GetWallpapersTask(this, apiUrl);
        getWallpapersTask.execute();
        List<Wallpaper> wallpapers = getWallpapersTask.getWallpapers();

        if (wallpapers != null && !wallpapers.isEmpty()) {
            // Select a random wallpaper from the list
            int index = new Random().nextInt(wallpapers.size());
            Wallpaper wallpaper = wallpapers.get(index);

            // Set the wallpaper
            setWallpaperFromUrl(this, wallpaper.getUrl());

        }
    }

    public void setWallpaperFromUrl(Context context, String imageUrl) {
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                try {
                    // Set the wallpaper for the home screen only
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                    Log.d("WallpaperService", "Wallpaper updated successfully");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                // Handle the error
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // You can display a placeholder while the image is loading
            }
        };
        Picasso.get().load(imageUrl).into(target);
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, "WallpaperServiceChannel")
                .setContentTitle("Wallpaper Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
        handler.post(runnableCode);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}