package com.bio.espalet.usecase;

import com.bio.espalet.data.SnapshotFileManager;
import com.bio.espalet.model.Snapshot;

public class SnapshotSaveFileUseCase {

    private SnapshotFileManager snapshotFileManager;

    public SnapshotSaveFileUseCase(SnapshotFileManager snapshotFileManager) {
        this.snapshotFileManager = snapshotFileManager;
    }

    public void execute(Snapshot snapshot) {
        this.snapshotFileManager.saveSnapshot(snapshot);
    }

}
