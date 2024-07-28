package app.forget.forgetfulnessapp.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.forget.forgetfulnessapp.Model.Reminder;
import app.forget.forgetfulnessapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class LastReminderWidget extends AppWidgetProvider {

    String reminderKeyy;

    private boolean isUserPremium() {
        // Kullanıcının abonelik durumunu kontrol etmek için gerekli kodu ekleyin
        // Örneğin, Firebase veya başka bir sunucu üzerinde kullanıcının abonelik durumunu kontrol edebilirsiniz.
        // Bu örnek sadece bir kılavuzdur, gerçek doğrulama sunucunuzda yapılmalıdır.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Premium").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String premium = snapshot.child("premium").getValue(String.class);
                if (snapshot.exists()){
                    if (premium.equals("yes")){

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        return true; // Geçici olarak her zaman true dönüyoruz.
    }




    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.last_reminder_widget);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        Log.d("WIDGET", "updateAppWidget: Widget güncelleme başladı 01");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Reminder").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Reminder nearestReminder = null;
                long currentTimeInMillis = System.currentTimeMillis();
                long nearestTimeDifference = Long.MAX_VALUE;

                for (DataSnapshot reminderSnapshot : dataSnapshot.getChildren()) {
                    Reminder reminder = reminderSnapshot.getValue(Reminder.class);
                    String reminderDateTime = reminder.getCreationDate();

                    // Hatırlatma tarihini ve zamanını milisaniyeye çevir
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date reminderDateTimeObj = sdf.parse(reminderDateTime);
                        long reminderTimeInMillis = reminderDateTimeObj.getTime();

                        // Şu anki zaman ile hatırlatma zamanı arasındaki farkı hesapla
                        long timeDifference = Math.abs(reminderTimeInMillis - currentTimeInMillis);

                        // En yakın zamanı kontrol et
                        if (timeDifference < nearestTimeDifference) {
                            nearestTimeDifference = timeDifference;
                            nearestReminder = reminder;
                            Log.d("LOG", "onDataChange:Mainnn " + nearestReminder);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (nearestReminder != null) {
                    String title = nearestReminder.getTitle();
                    String description = nearestReminder.getDescription();
                    String url = nearestReminder.getPhoto();
                    String status = nearestReminder.getStatus();

                    String key = nearestReminder.getId();
                    Log.d("WIDGET", "onDataChange: " + key);

                    views.setTextViewText(R.id.textTitleWidget, title);
                    views.setTextViewText(R.id.descWidget, description);
                    views.setTextViewText(R.id.textCompleteStatusWidget, status);

                    Picasso.get().load(url).into(views, R.id.imageViewWidget, new int[]{appWidgetId});

                    reminderKeyy = key;
                    Log.d("WIDGET", "onDataChange:003 " + reminderKeyy);

                    // CompleteButton'a tıklandığında yapılacak işlemleri ayarla
                    Intent completeButtonIntent = new Intent(context, LastReminderWidget.class);
                    completeButtonIntent.setAction("COMPLETE_BUTTON_CLICKED");
                    completeButtonIntent.putExtra("reminderKey", reminderKeyy);

                    PendingIntent pendingCompleteButtonIntent = PendingIntent.getBroadcast(context, 0, completeButtonIntent, 0);

                    views.setOnClickPendingIntent(R.id.completeButton, pendingCompleteButtonIntent);

                    appWidgetManager.updateAppWidget(appWidgetId, views);
                    Log.d("WIDGET", "updateAppWidget: Widget güncelleme başladı 02");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action, String reminderKeyy) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        intent.putExtra("reminderKey", reminderKeyy); // Reminder key'i intent'e ekleyin
        Log.d("WIDGET", "getPendingSelfIntent: 003" + reminderKeyy);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        if ("COMPLETE_BUTTON_CLICKED".equals(intent.getAction())) {
            String reminderKey = intent.getStringExtra("reminderKey");
            Log.d("WIDGET", "onReceive: " + reminderKey);
            if (reminderKey != null) {
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Reminder").child(userId).child(reminderKey);

                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String status = snapshot.child("status").getValue(String.class);
                        Log.d("WIDGET", "onDataChange: " + status);

                        if ("not completed".equals(status)) {
                            reference1.child("status").setValue("Complete");
                            Log.d("WIDGET", "onDataChange: TIKLANILDI");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Hata durumunu yönet
                    }
                });
            }
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

