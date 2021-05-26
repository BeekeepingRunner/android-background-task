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

    private static class DownloadInfoTask extends AsyncTask {

        // Connect with server, get file info (name, type), return FileInfo
        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }

        // display fetched info in GUI
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
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