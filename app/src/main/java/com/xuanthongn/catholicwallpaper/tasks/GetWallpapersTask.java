package com.xuanthongn.catholicwallpaper.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
//import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xuanthongn.catholicwallpaper.models.SearchResponse;
import com.xuanthongn.catholicwallpaper.models.Wallpaper;
import com.xuanthongn.catholicwallpaper.models.WallpaperResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.HttpUrl;

public class GetWallpapersTask {

    private final String apiUrl;
    private final Context context;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    String token = "HYqZ7uLxzGoJNJilNYSKpx3aulgn7I47-oEuJNcNu_c"; // replace with your actual token
    List<String> queryList = new ArrayList<>(Arrays.asList("catholic", "catholic church", "jesus", "mary", "catholic saint", "catholic cross", "catholic bible", "catholic prayer", "catholic faith", "catholic god"));
    private List<Wallpaper> wallpapers;

    public GetWallpapersTask(Context context, String apiUrl) {
        this.context = context;
        this.apiUrl = apiUrl;
    }

    public void execute() {
        Callable<List<Wallpaper>> callable = new Callable<List<Wallpaper>>() {
            @Override
            public List<Wallpaper> call() throws Exception {
                try {
                    String query = queryList.get((int) (Math.random() * queryList.size()));
                    OkHttpClient client = new OkHttpClient();
                    // Append the client_id to the URL
                    HttpUrl url = HttpUrl.parse(apiUrl)
                            .newBuilder()
                            .addQueryParameter("client_id", token)
                            .addQueryParameter("query", query)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();
                    // Chuyển đổi JSON thành danh sách các đối tượng Wallpaper
                    List<Wallpaper> wallpapers = new ArrayList<>();
                    Gson gson = new Gson();
                    Type wallpapersListType = new TypeToken<SearchResponse>() {
                    }.getType();
                    SearchResponse searchResponse = gson.fromJson(jsonData, wallpapersListType);
                    List<WallpaperResponse> wallpaperResponseList = searchResponse.getResults();
                    wallpapers = wallpaperResponseList.stream().map(wallpaperResponse -> new Wallpaper(wallpaperResponse.getUrl().getFull(), wallpaperResponse.getDescription())).collect(Collectors.toList());
                    return wallpapers;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        Future<List<Wallpaper>> future = executorService.submit(callable);

        try {
            List<Wallpaper> wallpapers = future.get();
            onPostExecute(wallpapers);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Wallpaper> getWallpapers() {
        return wallpapers;
    }

    protected void onPostExecute(List<Wallpaper> wallpapers) {
        this.wallpapers = wallpapers;
        if (wallpapers != null) {
            // Lưu trữ các hình nền vào bộ nhớ cache
            for (Wallpaper wallpaper : wallpapers) {
                Picasso.get().load(wallpaper.getUrl()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        // Lưu trữ bitmap vào bộ nhớ cache
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        // Xử lý lỗi tải hình nền
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        // Hiển thị hình nền mặc định
                    }
                });
            }

            // Cài đặt hình nền đầu tiên
//            if (!wallpapers.isEmpty())
//                setWallpaper(wallpapers.get(0));
        }
    }

//    private void setWallpaper(Wallpaper wallpaper) {
//        Picasso.get().load(wallpaper.getUrl()).into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//
//            }
//
//            @Override
//            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//
//        });
//    }
}