package com.bio.espalet.usecase;

import com.bio.espalet.data.SnapshotFetchTask;
import com.bio.espalet.model.Snapshot;

public class SnapshotFetchUseCase {

    public void execute(Snapshot snapshot, SnapshotFetchCallback callback) {
        new SnapshotFetchTask(snapshot, callback).execute(snapshot.getUrl());
    }

}
