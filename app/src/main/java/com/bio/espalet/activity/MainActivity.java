package com.bio.espalet.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bio.espalet.R;
import com.bio.espalet.model.SnapshotUrl;
import com.bio.espalet.model.Snapshot;
import com.bio.espalet.usecase.SnapshotFetchCallback;
import com.bio.espalet.usecase.SnapshotFetchUseCase;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {

    private TextView franceDate;
    private ImageView franceCam;
    private TextView spainDate;
    private ImageView spainCam;

    private Snapshot franceSnapshot, spainSnapshot;
    private SnapshotFetchUseCase snapshotFetchUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.initSnapshots();
        this.initViews();
        this.setListeners();
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
                Toast.makeText(this, R.string.refreshed, Toast.LENGTH_SHORT).show();
                this.fetchSnapshots(this.snapshotFetchUseCase);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initSnapshots() {
        this.franceSnapshot = new Snapshot(HttpUrl.parse(SnapshotUrl.ESPALET_FRANCE));
        this.spainSnapshot = new Snapshot(HttpUrl.parse(SnapshotUrl.ESPALET_SPAIN));
    }

    private void initViews() {
        this.franceDate = (TextView) findViewById(R.id.france_cam_date);
        this.franceCam = (ImageView) findViewById(R.id.france_cam_image);
        this.spainDate = (TextView) findViewById(R.id.spain_cam_date);
        this.spainCam = (ImageView) findViewById(R.id.spain_cam_image);
    }

    private void setListeners() {
        this.franceCam.setOnClickListener(new SnapshotClickListener(this.franceSnapshot));
        this.spainCam.setOnClickListener(new SnapshotClickListener(this.spainSnapshot));
    }

    private void fetchSnapshots(SnapshotFetchUseCase useCase) {
        useCase.execute(
                this.franceSnapshot,
                new SnapshotFetchCallback(this.franceCam, this.franceDate)
        );

        useCase.execute(
                this.spainSnapshot,
                new SnapshotFetchCallback(this.spainCam, this.spainDate)
        );
    }

}
