package com.example.fadhilikhsann.mysubmission3.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.fadhilikhsann.mysubmission3.MainActivity;
import com.example.fadhilikhsann.mysubmission3.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class ReminderReceiver extends BroadcastReceiver {

    public static final String TYPE_RELEASE = "ReleaseAlarm";
    public static final String TYPE_DAILY = "DailyAlarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    private static final String API_KEY = "0a5f7dacaa1737c927feb35a47fdf1dd";
    // Siapkan 2 id untuk 2 macam alarm, onetime dna repeating

    private final int ID_DAILY = 200;
    private final int ID_RELEASE = 201;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String title = context.getResources().getString(R.string.app_name);
        int notifId = type.equalsIgnoreCase(TYPE_DAILY) ? ID_DAILY : ID_RELEASE;

        if (notifId == ID_DAILY) {
            showAlarmNotification(context, title, message, notifId);
        } else if (notifId == ID_RELEASE) {
            checkForNotification(context, strDate, message, notifId);
        }
        showToast(context, title, EXTRA_MESSAGE);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    private void checkForNotification(final Context context, String strDate, final String extraMessage, final int notifId) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte=" + strDate + "&primary_release_date.lte=" + strDate;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    int ID_NOTIF = notifId;
                    String title = "";
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    if (list.length() > 0) {
                        for (int i = 0; i < list.length(); i++) {
                            title = list.getJSONObject(i).getString("title");
                            showAlarmNotification(context, title, title + " " + extraMessage, ID_NOTIF);
                            ID_NOTIF++;
                        }
                    } else {
                        showAlarmNotification(context, "Jinx", "Kagak ada anjenk", ID_NOTIF);
                        ID_NOTIF++;
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    private void showToast(Context context, String title, String message) {
        Toast.makeText(context, title + " : " + message, Toast.LENGTH_LONG).show();
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_DAILY) ? ID_DAILY : ID_RELEASE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        //Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show();
    }

    public void setRepeatingAlarm(Context context, String type, String time, String message) {
        final PendingIntent pendingIntent;
        String TIME_FORMAT = "HH:mm";
        if (isDateInvalid(time, TIME_FORMAT)) return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
        String[] timeArray = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        int requestCode = type.equalsIgnoreCase(TYPE_DAILY) ? ID_DAILY : ID_RELEASE;
        pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            //Toast.makeText(context, "Repeating alarm set up ", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(context, "Repeating alarm set up "+ requestCode, Toast.LENGTH_SHORT).show();
    }

    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }
}
