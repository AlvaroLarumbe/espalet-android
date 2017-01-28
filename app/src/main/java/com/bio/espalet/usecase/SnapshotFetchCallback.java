package com.bio.espalet.usecase;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bio.espalet.data.SnapshotFetchTaskPostExecute;
import com.bio.espalet.model.Snapshot;

public class SnapshotFetchCallback implements SnapshotFetchTaskPostExecute {

    private ImageView image;
    private TextView date;
    private ProgressBar progressBar;

    public SnapshotFetchCallback(ImageView image, TextView date, ProgressBar progressBar) {
        this.image = image;
        this.date = date;
        this.progressBar = progressBar;
    }

    @Override
    public void onTaskCompleted(Snapshot snapshot) {
        this.progressBar.setVisibility(View.GONE);
        this.image.setVisibility(View.VISIBLE);
        this.image.setImageBitmap(snapshot.getImage());
        this.date.setText(
                android.text.format.DateFormat.format("HH:mm - dd/MM/yyyy", snapshot.getDate())
        );
        this.date.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskFailed() {
        // TODO: display error
    }

}
