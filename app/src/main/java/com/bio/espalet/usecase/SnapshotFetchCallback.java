package com.bio.espalet.usecase;

import android.widget.ImageView;
import android.widget.TextView;

import com.bio.espalet.data.SnapshotFetchTaskPostExecute;
import com.bio.espalet.model.Snapshot;

public class SnapshotFetchCallback implements SnapshotFetchTaskPostExecute {

    private ImageView image;
    private TextView date;

    public SnapshotFetchCallback(ImageView image, TextView date) {
        this.image = image;
        this.date = date;
    }

    @Override
    public void onTaskCompleted(Snapshot snapshot) {
        this.image.setImageBitmap(snapshot.getImage());
        this.date.setText(
                android.text.format.DateFormat.format("HH:mm - dd/MM/yyyy", snapshot.getDate())
        );
    }

}
