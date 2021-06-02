package com.example.androidbackground;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        setButtonDownloadInfoOnClick();
    }

    private void getWidgetReferences() {
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonDownloadInfo = findViewById(R.id.buttonDownloadInfo);
        textViewFileSizeNumber = findViewById(R.id.textViewFileSizeNumber);
        textViewFileTypeText = findViewById(R.id.textViewFileTypeText);
        buttonDownloadFile = findViewById(R.id.buttonDownloadFile);
        textViewBytes = findViewById(R.id.textViewBytesDownloadedNumber);
    }

    private void setButtonDownloadInfoOnClick() {
        buttonDownloadInfo.setOnClickListener((View v) -> {

            String webAddress = editTextAddress.getText().toString().trim();
            if (webAddress.startsWith("https://")) {

                DownloadInfoTask downloadInfoTask = new DownloadInfoTask();
                downloadInfoTask.execute(webAddress);
            }
        });
    }
}