package com.example.androidbackground;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
// Service to download a file to the main catalogue on a memory card. It sends notification too.
public class FileDownloadService extends IntentService {

    NotificationManager notificationManager;

    private static final String ACTION_FILE_DOWNLOAD =
            "com.example.androidbackground.action.FILE_DOWNLOAD";

    private static int DATABLOCK_SIZE = 1024;
    private int bytesFetched = 0;

    // parameters
    private static final String FILE_URL = "file_url";
    public static final int ID_NOTIFICATIONS = 1;
    private static final String ID_CHANNEL = "notification_channel";

    public FileDownloadService() {
        super("FileDownloadService");
    }

    /**
     * Starts this service to perform action FileDownload with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFileDownload(Context context, String fileURL, int notificationsParam)
    {
        Intent intent = new Intent(context, FileDownloadService.class);
        intent.setAction(ACTION_FILE_DOWNLOAD);
        intent.putExtra(FILE_URL, fileURL);
        intent.putExtra(String.valueOf(ID_NOTIFICATIONS), notificationsParam);
        context.startService(intent);
    }

    // Actually executes a task
    @Override
    protected void onHandleIntent(Intent intent) {

        // notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // prepareNotificationChannel();
        // startForeground(ID_NOTIFICATIONS, createNotification());

        Log.d("FileDownloadService", "Service has started downloading a file...");

        if (intent != null)
        {
            final String action = intent.getAction();
            if (ACTION_FILE_DOWNLOAD.equals(action))
            {
                final int param = intent.getIntExtra(String.valueOf(ID_NOTIFICATIONS), 0);
                String fileURL = intent.getStringExtra(FILE_URL);
                handleActionFileDownload(fileURL, param);
            }
            else {
                Log.e("FileDownloadService", "Unknown action");
            }
        }
        Log.d("FileDownloadService", "Service has finished the task");
    }

    private void prepareNotificationChannel() {

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android 8/Oreo requires notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.app_name);
            NotificationChannel channel = new NotificationChannel(
                    ID_CHANNEL, name, NotificationManager.IMPORTANCE_LOW);

            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification() {

        Intent notificationIntent = new Intent(this, FileDownloadService.class);
        // Data for display, when user comes back to the application
        // notificationIntent.putExtra();

        // We build a stack of activities, that the user is waiting for after comeback.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(FileDownloadService.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // We build a notification
        Notification.Builder notificationBuilder = new Notification.Builder(this);
        notificationBuilder.setContentTitle(getString(R.string.notification_title))
                //.setProgress(100, progressValue(), false)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH);

        /* if downloading is still running...
        if (...)
        {
            notificationBuilder.setOngoing(false);
        } else {
            notificationBuilder.setOngoing(true);
        }
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(ID_CHANNEL);
        }

        return notificationBuilder.build();
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFileDownload(String fileURL, int notificationParam) {

        // notificationManager.notify(notificationParam, createNotification());

        FileOutputStream fileOutput = null;
        try {
            // preparing a file to save to
            URL url = new URL(fileURL);
            File tempFile = new File(url.getFile());
            File outFile = new File(
                    Environment.getExternalStorageDirectory()
                    + File.separator
                    + tempFile.getName()
            );
            if (outFile.exists())
                outFile.delete();

            // file downloading...
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream fromWebStream = connection.getInputStream();
            DataInputStream reader = new DataInputStream(fromWebStream);
            FileOutputStream fileOutputStream = new FileOutputStream(outFile.getPath());

            byte buffer[] = new byte[DATABLOCK_SIZE];
            int downloaded = reader.read(buffer, 0, DATABLOCK_SIZE);
            while (downloaded != -1) {
                fileOutputStream.write(buffer, 0, downloaded);
                bytesFetched += downloaded;
                downloaded = reader.read(buffer, 0, DATABLOCK_SIZE);
            }

            if (fromWebStream != null) {
                fromWebStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}