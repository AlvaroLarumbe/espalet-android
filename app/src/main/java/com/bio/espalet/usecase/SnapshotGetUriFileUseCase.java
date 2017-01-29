package com.bio.espalet.usecase;

import android.net.Uri;

import com.bio.espalet.data.SnapshotFileManager;

public class SnapshotGetUriFileUseCase {

    private SnapshotFileManager snapshotFileManager;

    public SnapshotGetUriFileUseCase(SnapshotFileManager snapshotFileManager) {
        this.snapshotFileManager = snapshotFileManager;
    }

    public Uri execute() {
        return this.snapshotFileManager.getSnapshotUri();
    }

}
