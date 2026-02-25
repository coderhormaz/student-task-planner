package com.hormaz.studentplanner.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.hormaz.studentplanner.App;
import com.hormaz.studentplanner.ui.task.TaskDetailActivity;

public class ReminderReceiver extends BroadcastReceiver {

    public static final String EXTRA_TASK_TITLE = "task_title";
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        String taskTitle = intent.getStringExtra(EXTRA_TASK_TITLE);
        String taskId = intent.getStringExtra(Constants.EXTRA_TASK_ID);
        int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID,
                Constants.NOTIFICATION_ID_REMINDER);

        Intent openIntent = new Intent(context, TaskDetailActivity.class);
        openIntent.putExtra(Constants.EXTRA_TASK_ID, taskId);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, notificationId, openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context, App.CHANNEL_REMINDERS)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Task Reminder")
                .setContentText(taskTitle != null ? taskTitle : "You have a task due soon!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        try {
            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            manager.notify(notificationId, builder.build());
        } catch (SecurityException ignored) {
            // Notification permission not granted
        }
    }

    public static void scheduleReminder(Context context, String taskId,
                                         String taskTitle, long triggerAtMillis) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(Constants.EXTRA_TASK_ID, taskId);
        intent.putExtra(EXTRA_TASK_TITLE, taskTitle);
        intent.putExtra(EXTRA_NOTIFICATION_ID, taskId.hashCode());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, taskId.hashCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        }
    }

    public static void cancelReminder(Context context, String taskId) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, taskId.hashCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
