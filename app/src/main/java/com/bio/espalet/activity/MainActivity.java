package com.bio.espalet.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bio.espalet.R;
import com.bio.espalet.model.SnapshotUrl;
import com.bio.espalet.model.Snapshot;
import com.bio.espalet.usecase.SnapshotFetchCallback;
import com.bio.espalet.usecase.SnapshotFetchUseCase;
import com.bio.espalet.utils.OkHttpRetryInterceptor;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;

    private TextView franceDate;
    private ImageView franceCam;
    private ProgressBar franceProgressBar;
    private TextView spainDate;
    private ImageView spainCam;
    private ProgressBar spainProgressBar;

    private Snapshot franceSnapshot, spainSnapshot;
    private SnapshotFetchUseCase snapshotFetchUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.okHttpClient = this.createOkHttpClient();
        this.configureSnapshots();
        this.configureViews();
        this.configureListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.snapshotFetchUseCase = new SnapshotFetchUseCase();
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
                this.franceCam.setVisibility(View.GONE);
                this.spainCam.setVisibility(View.GONE);
                this.franceProgressBar.setVisibility(View.VISIBLE);
                this.spainProgressBar.setVisibility(View.VISIBLE);
                this.fetchSnapshots(this.snapshotFetchUseCase);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void configureSnapshots() {
        this.franceSnapshot = new Snapshot(HttpUrl.parse(SnapshotUrl.ESPALET_FRANCE));
        this.spainSnapshot = new Snapshot(HttpUrl.parse(SnapshotUrl.ESPALET_SPAIN));
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
        this.franceCam.setOnClickListener(new SnapshotClickListener(this.franceSnapshot));
        this.spainCam.setOnClickListener(new SnapshotClickListener(this.spainSnapshot));
    }

    private OkHttpClient createOkHttpClient() {
        return new OkHttpClient()
                .newBuilder()
                .addInterceptor(new OkHttpRetryInterceptor(this))
                .build();
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

}
