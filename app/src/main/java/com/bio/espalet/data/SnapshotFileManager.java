package com.bio.espalet.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.bio.espalet.R;
import com.bio.espalet.model.Snapshot;
import com.bio.espalet.model.SnapshotConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SnapshotFileManager {

    private Context context;

    private File snapshotsFolder;

    public SnapshotFileManager(Context context) {
        this.context = context;
        this.snapshotsFolder = new File(
                this.context.getCacheDir() + File.separator + SnapshotConstants.SNAPSHOT_FOLDER
        );
    }

    public void saveSnapshot(Snapshot snapshot) {
        if(!this.snapshotsFolder.exists())
            if(!this.snapshotsFolder.mkdir())
                return;

        File file = new File(
                this.snapshotsFolder,
                SnapshotConstants.SNAPSHOT_FILENAME
        );
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(file);
            snapshot.getImage().compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            Toast.makeText(this.context, R.string.image_error, Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignored) {}
        }
    }

    public Uri getSnapshotUri() {
        File file = new File(
                this.snapshotsFolder,
                SnapshotConstants.SNAPSHOT_FILENAME
        );

        return FileProvider.getUriForFile(context, "com.bio.espalet.fileprovider", file);
    }

}
