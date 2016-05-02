package com.gw.api;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gw.MainApplication;
import com.gw.util.NetUtil;
import com.gw.util.ToastUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;


public final class ResponseInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastUtil.showShortToast(MainApplication.getApplication().getApplicationContext(), msg.obj.toString());

        }
    };

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        if (!NetUtil.isNetworkAvailable()) {
            request = request
                    .newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        //这里可以根据响应体做出不同操作
       /* ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                Logger.i("Couldn't decode the response body; charset is likely malformed.");
                return getWrapResponse(response);
            }
        }
        String result = buffer.clone().readString(charset);
        try {
            JSONObject jsonObject = new JSONObject(result);
            Message msg = Message.obtain();
            if (jsonObject.getBoolean("error")) {
                msg.obj = "true";
            } else {
                msg.obj = "false";
            }
            handler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        // Logger.i("Test:--->"+buffer.clone().readString(charset));


        return getWrapResponse(response);
    }

    //缓存
    private Response getWrapResponse(Response response) {
        return response
                .newBuilder()
                //在这里生成新的响应并修改它的响应头
                .header("Cache-Control", "public,max-age=3600")
                .removeHeader("Pragma").build();
    }
}