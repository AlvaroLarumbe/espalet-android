package com.bio.espalet.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

import com.bio.espalet.R;
import com.bio.espalet.model.Snapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SnapshotClickListener implements View.OnClickListener {

    private Snapshot snapshot;

    public SnapshotClickListener(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public void onClick(View view) {
        this.saveSnapshotInCache(view.getContext(), this.snapshot);
        Intent intent = new Intent(view.getContext(), SnapshotFullscreenActivity.class);
        view.getContext().startActivity(intent);
    }

    private void saveSnapshotInCache(Context context, Snapshot snapshot) {
        File file = this.createTempFile(context, "snapshot.tmp");
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(file);
            snapshot.getImage().compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            Toast.makeText(context, R.string.error_opening_image, Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                // Empty :)
            }
        }
    }

    private File createTempFile(Context context, String fileName) {
        return new File(context.getCacheDir(), fileName);
    }

}
