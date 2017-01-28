package com.bio.espalet.data;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.bio.espalet.model.Snapshot;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SnapshotFetchTask extends AsyncTask<Snapshot, Void, Snapshot> {

    private OkHttpClient okHttpClient;

    private Snapshot snapshot;
    private SnapshotFetchTaskPostExecute callback;

    public SnapshotFetchTask(
            OkHttpClient okHttpClient,
            Snapshot snapshot,
            SnapshotFetchTaskPostExecute callback
    ) {
        this.okHttpClient = okHttpClient;
        this.snapshot = snapshot;
        this.callback = callback;
    }

    @Override
    protected Snapshot doInBackground(Snapshot... params) {
        Request request = new Request.Builder()
                .url(params[0].getUrl())
                .build();

        Response response = null;

        try {
            response = this.okHttpClient.newCall(request).execute();
        } catch (IOException ignored) {}

        if(this.isValidResponse(response)) {
            snapshot.setImage(BitmapFactory.decodeStream(response.body().byteStream()));
            snapshot.setDate(this.getServerDate(response));

            return snapshot;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Snapshot snapshot) {
        if(snapshot != null) {
            callback.onTaskCompleted(snapshot);
        }
        else {
            callback.onTaskFailed();
        }
    }

    private boolean isValidResponse(Response response) {
        return response != null && response.isSuccessful();
    }

    private Date getServerDate(Response response) {
        SimpleDateFormat format = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss 'GMT'",
                Locale.US
        );
        Date date = new Date();

        try {
            if (response != null) {
                date = format.parse(response.header("Last-Modified"));
            }
        } catch (ParseException ignored) {}

        return date;
    }

}
