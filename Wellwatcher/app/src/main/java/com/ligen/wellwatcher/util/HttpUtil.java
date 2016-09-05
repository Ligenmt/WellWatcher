package com.ligen.wellwatcher.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

import okio.BufferedSink;

/**
 * Created by ligen on 2016/4/10.
 */
public class HttpUtil {

    volatile static String responseText;
    /**
     * HTTP POST请求，发送数据到服务端
     * @param urlStr
     * @param body
     * @return
     */
    public static String doPost(final String urlStr, final String body) throws Exception{

        Log.i("upload data", body);
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/html"), body);
        Request request = new Request.Builder().url(urlStr).post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("HttpUtil", request.urlString());
                responseText = "500";
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i("HttpUtil", response.body().string());
                responseText = "200";
            }
        });
        return responseText;
    }

    /**
     * 判断是否存在网络连接
     * @param context
     * @return
     */
    public static boolean isConnect(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean flag = false;
        if (connectivity != null) {
            // 获取网络连接管理的对象
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 判断当前网络是否已经连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    flag = true;
                }

            }
        }
        return flag;
    }

}
