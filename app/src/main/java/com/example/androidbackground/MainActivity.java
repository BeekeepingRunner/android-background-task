package com.example.androidbackground;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText editTextAddress;
    private Button buttonDownloadInfo;
    private TextView textViewFileSizeNumber;
    private TextView textViewFileTypeText;
    private Button buttonDownloadFile;
    private TextView textViewBytes;
    private ProgressBar progressBar;

    private final String FILE_SIZE_NUMBER = "file_size_number";
    private final String FILE_TYPE = "file_type";
    private final String BYTES_FETCHED = "bytes_fetched";

    String webAddress = "";

    private static final int CODE_WRITE_EXTERNAL_STORAGE = 1;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            ProgressInfo progressInfo = bundle.getParcelable(FileDownloadService.INFO);

            switch (progressInfo.getStatus()) {
                case ProgressInfo.IN_PROGRESS:
                    textViewBytes.setText(String.valueOf(progressInfo.getBytesFetched()));
                    progressBar.setProgress(progressInfo.getProgress());
                    break;
                case ProgressInfo.FINISHED:
                    textViewBytes.setText(String.valueOf(progressInfo.getFileSize()));
                    progressBar.setProgress(100);
                    break;
                case ProgressInfo.ERROR:
                    textViewBytes.setText(getString(R.string.notification_error));
            }
        }
    };

    // Register the receiver
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver, new IntentFilter(FileDownloadService.NOTIFICATION)
        );
    }

    // Unregister the receiver
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    // Task to run in a background
    private class DownloadInfoTask extends AsyncTask<String, Void, FileInfo> {

        @Override
        protected FileInfo doInBackground(String... strings) {

            HttpsURLConnection connection = null;
            FileInfo fileInfo = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int contentSize = connection.getContentLength();
                String contentType = connection.getContentType();
                fileInfo = new FileInfo(contentSize, contentType);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null)
                    connection.disconnect();
            }

            return fileInfo;
        }

        @Override
        protected void onPostExecute(FileInfo fileInfo) {

            if (fileInfo != null) {

                textViewFileSizeNumber.setText(String.valueOf(fileInfo.getFileSize()));
                textViewFileTypeText.setText(fileInfo.getFileType());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWidgetReferences();

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }

        setButtonDownloadInfoOnClick();
        setButtonDownloadFileOnClick();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(FILE_SIZE_NUMBER, textViewFileSizeNumber.getText().toString());
        outState.putString(FILE_TYPE, textViewFileTypeText.getText().toString());
        outState.putString(BYTES_FETCHED, textViewBytes.getText().toString());
    }

    private void restoreState(Bundle savedInstanceState) {

        textViewFileSizeNumber.setText(savedInstanceState.getString(FILE_SIZE_NUMBER));
        textViewFileTypeText.setText(savedInstanceState.getString(FILE_TYPE));
        textViewBytes.setText(savedInstanceState.getString(BYTES_FETCHED));
    }

    private void getWidgetReferences() {
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonDownloadInfo = findViewById(R.id.buttonDownloadInfo);
        textViewFileSizeNumber = findViewById(R.id.textViewFileSizeNumber);
        textViewFileTypeText = findViewById(R.id.textViewFileTypeText);
        buttonDownloadFile = findViewById(R.id.buttonDownloadFile);
        textViewBytes = findViewById(R.id.textViewBytesDownloadedNumber);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setButtonDownloadInfoOnClick() {
        buttonDownloadInfo.setOnClickListener((View v) -> {

            String webAddress = editTextAddress.getText().toString().trim();
            if (webAddress.startsWith("https://")) {

                DownloadInfoTask downloadInfoTask = new DownloadInfoTask();
                downloadInfoTask.execute(webAddress);
            } else {
                Toast.makeText(MainActivity.this, R.string.bad_url, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setButtonDownloadFileOnClick() {

        buttonDownloadFile.setOnClickListener((View v) -> {

            webAddress = editTextAddress.getText().toString().trim();
            if (webAddress.startsWith("https://")) {

                if (hasPermissions()) {
                    FileDownloadService.startActionFileDownload(
                            MainActivity.this, webAddress, FileDownloadService.ID_NOTIFICATIONS);
                } else {
                    if (permissionPreviouslyDenied()) {
                        // TODO: explain why we need permissions ...
                    }

                    ActivityCompat.requestPermissions(this,
                            new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },
                            CODE_WRITE_EXTERNAL_STORAGE);
                }

            } else {
                Toast.makeText(MainActivity.this, R.string.bad_url, Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean hasPermissions() {
        return ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean permissionPreviouslyDenied() {
        return ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] decisions) {

        super.onRequestPermissionsResult(requestCode, permissions, decisions);
        switch (requestCode) {
            case CODE_WRITE_EXTERNAL_STORAGE:
                if (permissions.length > 0
                && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && decisions[0] == PackageManager.PERMISSION_GRANTED) {

                    FileDownloadService.startActionFileDownload(
                            MainActivity.this, webAddress, FileDownloadService.ID_NOTIFICATIONS);
                } else {
                    // nothing to do without permission... :(
                }
                break;
            default:
                Log.e("onRequestPermissionsResult", "unknown request code");
                break;
        }
    }
}