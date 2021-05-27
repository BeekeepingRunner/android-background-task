package com.example.androidbackground;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText editTextAddress;
    private Button buttonDownloadInfo;
    private TextView textViewFileSize;
    private TextView textViewFileType;
    private Button buttonDownloadFile;
    private TextView textViewBytes;

    private static class DownloadInfoTask extends AsyncTask<String, Void, FileInfo> {

        @Override
        protected FileInfo doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(FileInfo fileInfo) {
            super.onPostExecute(fileInfo);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWidgetReferences();

    }

    private void getWidgetReferences() {
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonDownloadInfo = findViewById(R.id.buttonDownloadInfo);
        textViewFileSize = findViewById(R.id.textViewFileSize);
        textViewFileType = findViewById(R.id.textViewFileTypeText);
        buttonDownloadFile = findViewById(R.id.buttonDownloadFile);
        textViewBytes = findViewById(R.id.textViewBytesDownloadedNumber);
    }
}