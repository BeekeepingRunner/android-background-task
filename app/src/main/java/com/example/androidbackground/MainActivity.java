package com.example.androidbackground;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

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
    }
}