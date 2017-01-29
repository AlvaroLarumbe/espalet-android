package com.bio.espalet.presentation.view.activity;

import android.content.Intent;
import android.view.View;

import com.bio.espalet.model.Snapshot;
import com.bio.espalet.usecase.SnapshotSaveFileUseCase;

public class SnapshotClickListener implements View.OnClickListener {

    private SnapshotSaveFileUseCase snapshotSaveFileUseCase;

    private Snapshot snapshot;

    public SnapshotClickListener(
            SnapshotSaveFileUseCase snapshotSaveFileUseCase,
            Snapshot snapshot
    ) {
        this.snapshotSaveFileUseCase = snapshotSaveFileUseCase;
        this.snapshot = snapshot;
    }

    @Override
    public void onClick(View view) {
        snapshotSaveFileUseCase.execute(this.snapshot);
        Intent intent = new Intent(view.getContext(), SnapshotFullscreenActivity.class);
        view.getContext().startActivity(intent);
    }

}
