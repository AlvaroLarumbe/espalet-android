package com.bio.espalet.presentation.view.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bio.espalet.R;
import com.bio.espalet.data.SnapshotFileManager;
import com.bio.espalet.model.SnapshotConstants;
import com.bio.espalet.model.Snapshot;
import com.bio.espalet.presentation.view.dialog.ShareSnapshotDialogFragment;
import com.bio.espalet.usecase.SnapshotFetchCallback;
import com.bio.espalet.usecase.SnapshotFetchUseCase;
import com.bio.espalet.usecase.SnapshotGetUriFileUseCase;
import com.bio.espalet.usecase.SnapshotSaveFileUseCase;
import com.bio.espalet.utils.OkHttpRetryInterceptor;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements ShareSnapshotDialogFragment.ShareableSnapshot {

    private OkHttpClient okHttpClient;
    private SnapshotFetchUseCase snapshotFetchUseCase;
    private SnapshotSaveFileUseCase snapshotSaveFileUseCase;
    private SnapshotGetUriFileUseCase snapshotGetUriFileUseCase;

    private TextView franceDate, spainDate;
    private ImageView franceCam, spainCam;
    private ProgressBar franceProgressBar, spainProgressBar;
    private Snapshot franceSnapshot, spainSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.init();
        this.configureSnapshots();
        this.configureViews();
        this.configureListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.fetchSnapshots(this.snapshotFetchUseCase);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_refresh:
                this.refreshSnapshots();
                return true;
            case R.id.action_share:
                this.showShareSnapshotDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        SnapshotFileManager snapshotFileManager = new SnapshotFileManager(this);
        this.okHttpClient = this.createOkHttpClient();
        this.snapshotFetchUseCase = new SnapshotFetchUseCase();
        this.snapshotSaveFileUseCase = new SnapshotSaveFileUseCase(snapshotFileManager);
        this.snapshotGetUriFileUseCase = new SnapshotGetUriFileUseCase(snapshotFileManager);
    }

    private void configureSnapshots() {
        this.franceSnapshot = new Snapshot(HttpUrl.parse(SnapshotConstants.ESPALET_FRANCE));
        this.spainSnapshot = new Snapshot(HttpUrl.parse(SnapshotConstants.ESPALET_SPAIN));
    }

    private void configureViews() {
        this.franceDate = (TextView) findViewById(R.id.france_cam_date);
        this.franceCam = (ImageView) findViewById(R.id.france_cam_image);
        this.franceProgressBar = (ProgressBar) findViewById(R.id.france_progress_bar);
        this.spainDate = (TextView) findViewById(R.id.spain_cam_date);
        this.spainCam = (ImageView) findViewById(R.id.spain_cam_image);
        this.spainProgressBar = (ProgressBar) findViewById(R.id.spain_progress_bar);
    }

    private void configureListeners() {
        this.franceCam.setOnClickListener(
                new SnapshotClickListener(this.snapshotSaveFileUseCase, this.franceSnapshot)
        );
        this.spainCam.setOnClickListener(
                new SnapshotClickListener(this.snapshotSaveFileUseCase, this.spainSnapshot)
        );
    }

    private void fetchSnapshots(SnapshotFetchUseCase useCase) {
        useCase.execute(
                this.okHttpClient,
                this.franceSnapshot,
                new SnapshotFetchCallback(this.franceCam, this.franceDate, this.franceProgressBar)
        );

        useCase.execute(
                this.okHttpClient,
                this.spainSnapshot,
                new SnapshotFetchCallback(this.spainCam, this.spainDate, this.spainProgressBar)
        );
    }

    private void refreshSnapshots() {
        this.franceCam.setVisibility(View.GONE);
        this.spainCam.setVisibility(View.GONE);
        this.franceProgressBar.setVisibility(View.VISIBLE);
        this.spainProgressBar.setVisibility(View.VISIBLE);
        this.fetchSnapshots(this.snapshotFetchUseCase);
    }

    private void showShareSnapshotDialog() {
        if(!this.isVisible(this.franceCam) && !this.isVisible(this.spainCam)) {
            Toast.makeText(this, R.string.share_wait, Toast.LENGTH_SHORT).show();
        }
        else {
            DialogFragment shareSnapshotDialog = new ShareSnapshotDialogFragment();
            Bundle args = new Bundle();
            args.putBoolean("showFrance", this.isVisible(this.franceCam));
            args.putBoolean("showSpain", this.isVisible(this.spainCam));
            shareSnapshotDialog.setArguments(args);
            shareSnapshotDialog.show(getFragmentManager(), "share");
        }
    }

    @Override
    public void onShareSnapshotSelected(short option) {
        switch (option) {
            case SnapshotConstants.FRANCE:
                this.shareSnapshot(this.franceSnapshot);
                break;
            case SnapshotConstants.SPAIN:
                this.shareSnapshot(this.spainSnapshot);
                break;
            default:
        }
    }

    private void shareSnapshot(Snapshot snapshot) {
        this.snapshotSaveFileUseCase.execute(snapshot);
        Intent intent = new Intent();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        else
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, this.snapshotGetUriFileUseCase.execute());
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Share"));
    }

    private boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    private OkHttpClient createOkHttpClient() {
        return new OkHttpClient()
                .newBuilder()
                .addInterceptor(new OkHttpRetryInterceptor(this))
                .build();
    }
}
