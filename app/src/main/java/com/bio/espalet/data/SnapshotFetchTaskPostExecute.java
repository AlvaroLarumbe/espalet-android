package com.bio.espalet.data;

import com.bio.espalet.model.Snapshot;

public interface SnapshotFetchTaskPostExecute {

    void onTaskCompleted(Snapshot snapshot);
    void onTaskFailed();

}
