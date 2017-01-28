package com.bio.espalet.usecase;

import com.bio.espalet.data.SnapshotFetchTask;
import com.bio.espalet.model.Snapshot;

import okhttp3.OkHttpClient;

public class SnapshotFetchUseCase {

    public void execute(
            OkHttpClient okHttpClient,
            Snapshot snapshot,
            SnapshotFetchCallback callback
    ) {
        new SnapshotFetchTask(okHttpClient, snapshot, callback).execute(snapshot);
    }

}
