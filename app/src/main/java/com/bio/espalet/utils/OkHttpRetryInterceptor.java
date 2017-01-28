package com.bio.espalet.utils;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpRetryInterceptor implements Interceptor {

    private final static int MAX_REQUEST_RETRIES = 3;

    /**
     * Time in miliseconds multiplied by the current retry
     *
     * Retry 1: RETRY_BACKOFF_DELAY * 1
     * Retry 2: RETRY_BACKOFF_DELAY * 2
     * Retry 3: RETRY_BACKOFF_DELAY * 3
     */
    private final static int RETRY_BACKOFF_DELAY = 2000;

    private NetworkUtils networkUtils;

    public OkHttpRetryInterceptor(Context context) {
        this.networkUtils = new NetworkUtils(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        int retries = 1;

        while(retries <= MAX_REQUEST_RETRIES) {
            try {
                response = chain.proceed(request);
                break;
            }
            catch (Exception e) {
                if(!this.networkUtils.isInternetAvailable()) {
                    throw e;
                }

                if("Cancelled".equalsIgnoreCase(e.getMessage())) {
                    throw e;
                }

                if(retries > MAX_REQUEST_RETRIES) {
                    throw e;
                }

                try {
                    Thread.sleep(RETRY_BACKOFF_DELAY * retries);
                } catch (InterruptedException e1) {
                    throw new RuntimeException(e1);
                }

                retries++;
            }
        }

        return response;
    }

}
