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
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
// Service to download a file to the main catalogue on a memory card. It sends notification too.
public class FileDownloadService extends IntentService {

    NotificationManager notificationManager;

    private static final String ACTION_FILE_DOWNLOAD =
            "com.example.androidbackground.action.FILE_DOWNLOAD";

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
    public static void startActionFileDownload(Context context, int notificationsParam)
    {
        Intent intent = new Intent(context, FileDownloadService.class);
        intent.setAction(ACTION_FILE_DOWNLOAD);
        intent.putExtra(String.valueOf(ID_NOTIFICATIONS), notificationsParam);
        context.startService(intent);
    }

    // Actually executes a task
    @Override
    protected void onHandleIntent(Intent intent) {

        // notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // prepareNotificationChannel();
        // startForeground(ID_NOTIFICATIONS, createNotification());

        if (intent != null)
        {
            final String action = intent.getAction();
            if (ACTION_FILE_DOWNLOAD.equals(action))
            {
                final int param = intent.getIntExtra(String.valueOf(ID_NOTIFICATIONS), 0);
                handleActionFileDownload(param);
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
    private void handleActionFileDownload(int param) {

        // notificationManager.notify(ID_NOTIFICATIONS, createNotification());

        throw new UnsupportedOperationException("Not yet implemented");
        
    }

}