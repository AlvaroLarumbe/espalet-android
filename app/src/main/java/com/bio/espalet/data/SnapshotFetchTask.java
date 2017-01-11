package com.bio.espalet.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.bio.espalet.model.Snapshot;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SnapshotFetchTask extends AsyncTask<HttpUrl, Void, Snapshot> {

    private Snapshot snapshot;
    private SnapshotFetchTaskPostExecute callback;

    public SnapshotFetchTask(Snapshot snapshot, SnapshotFetchTaskPostExecute callback) {
        this.snapshot = snapshot;
        this.callback = callback;
    }

    @Override
    protected Snapshot doInBackground(HttpUrl... params) {
        final OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(params[0])
                .build();

        Response response = null;
        Bitmap image = null;

        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            // TODO: print error
        }

        if(response != null && response.isSuccessful()) {
            image = BitmapFactory.decodeStream(response.body().byteStream());
        }

        SimpleDateFormat format = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss 'GMT'",
                Locale.US
        );
        Date date = new Date();

        try {
            if (response != null) {
                date = format.parse(response.header("Last-Modified"));
            }
        } catch (ParseException e) {
            // Could not parse date, current date is already set
        }

        snapshot.setImage(image);
        snapshot.setDate(date);

        return snapshot;
    }

    @Override
    protected void onPostExecute(Snapshot snapshot) {
        callback.onTaskCompleted(snapshot);
    }

}
