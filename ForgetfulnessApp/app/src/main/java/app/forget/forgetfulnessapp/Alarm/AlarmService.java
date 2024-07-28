package app.forget.forgetfulnessapp.Alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;

import app.forget.forgetfulnessapp.R;

public class AlarmService extends Service {
    private boolean alarmStarted = false;
    String sabitAlarmChannelID = "sabit_alarm_channel";
    public MediaPlayer notificationSound;

    String alarmId;
    String title;
    String description;
    PendingIntent okContentIntent;
    PendingIntent okPendingIntent;

    private static final int NOTIFICATION_ID = 1;

    PendingIntent pendingIntent;

    String recc;




    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("LOG", "onCreate: "+ "onCreate Çalıştı");


        Intent intent = new Intent();
        if (intent != null){
            alarmId = intent.getStringExtra("alarmId");
            description = intent.getStringExtra("description");
            title = intent.getStringExtra("title");
            recc = intent.getStringExtra("alarm10");
        }



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null){
            alarmId = intent.getStringExtra("alarmId");
            description = intent.getStringExtra("description");
            title = intent.getStringExtra("title");
            recc = intent.getStringExtra("alarm10");


        }

        if (intent != null){
            if ("STOP_ALARM_SOUND".equals(intent.getAction())){
                stopAlarmSound();
            }else if ("STOP_BILL".equals(intent.getAction())){
                Log.d("BILL", "onStartCommand:denemebillstopbillreceiver ");
            }else {
                startForegroundService();
                Log.d("LOG", "onStartCommand: "+ "startForegroundService çalıştı");

            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startForegroundService() {

        Notification notification = createNotification("Sabit Alarm",sabitAlarmChannelID);
        startForeground(NOTIFICATION_ID, notification);
        Log.d("SERVICE", "startForegroundService: çalıştı foreground");

        Intent intent = new Intent();
        if (alarmId!=null && recc.equals("alarm")){
            startAlarm(getApplicationContext());
        }else if (alarmId!=null && recc.equals("bill")){
            startAlarm1(getApplicationContext());
        }
    }



    public Notification createNotification(String message, String channelId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(R.drawable.logo10)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                //.setPriority(NotificationCompat.FLAG_NO_CLEAR)
                .setOngoing(false)
                //.setVibrate(new long[]{ 1000, 1000, 1000, 1000, 1000 })
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Alarm Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            //channel.enableVibration(true);
            channel.setLightColor(Color.RED);
            //channel.setVibrationPattern(new long[]{1000, 500});
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        return builder.build();

    }


    private void startAlarm1(Context context){
            // Alarmı başlatmak için gerekli işlemleri burada gerçekleştirin
            // Örneğin, bir bildirim göndermek veya sesli bir alarm çalmak için AlarmManager kullanabilirsiniz.
            // AlarmManager ve PendingIntent kullanarak bir alarm başlatmak için aşağıdaki kodu kullanabilirsiniz:
            Log.d("LOG", "startAlarm: startAlarm Methodu çalıştı");

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);

            if (Build.VERSION.SDK_INT >= 31) {
                pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_MUTABLE);
            }else {
                pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            // Alarmı başlatmak için bir zaman belirleyin (örneğin, 5 saniye sonra)
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            //calendar.add(Calendar.SECOND, 5); // 5 saniye sonra alarm başlayacak
            Log.d("alarm", "startAlarm: Alarm Çalıyor");

            // AlarmManager'ı kullanarak PendingIntent'i belirlediğiniz zamanla başlatın
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


            sendBillNotification(context);



    }



    private void startAlarm(Context context){
        if (!alarmStarted){
            // Alarmı başlatmak için gerekli işlemleri burada gerçekleştirin
            // Örneğin, bir bildirim göndermek veya sesli bir alarm çalmak için AlarmManager kullanabilirsiniz.
            // AlarmManager ve PendingIntent kullanarak bir alarm başlatmak için aşağıdaki kodu kullanabilirsiniz:
            Log.d("LOG", "startAlarm: startAlarm Methodu çalıştı");

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);

            if (Build.VERSION.SDK_INT >= 31) {
                 pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_MUTABLE);
            }else {
                pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            // Alarmı başlatmak için bir zaman belirleyin (örneğin, 5 saniye sonra)
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            //calendar.add(Calendar.SECOND, 5); // 5 saniye sonra alarm başlayacak
            Log.d("alarm", "startAlarm: Alarm Çalıyor");

            // AlarmManager'ı kullanarak PendingIntent'i belirlediğiniz zamanla başlatın
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


            sendNotification(context,"Dinamik Alarm");

            alarmStarted = true;
        }


    }

    public void sendNotification(Context context, String message) {



        // UYGULAMADAN SEÇİLDİ

        SharedPreferences preferences = context.getSharedPreferences("AlarmSettings", Context.MODE_PRIVATE);
        int selectedAlarmUri = preferences.getInt("selectedAlarmUri", 0); // 0 yerine varsayılan bir değer belirleyebilirsiniz
        long millis = preferences.getLong("time",0);


        //CİHAZDAN SEÇİLDİ

        SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedSoundUriString = preferences2.getString("selected_sound_uri1", null);
        long millis1 = preferences2.getLong("time1",0);




        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews customNavigationView = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout);

        // OK_ACTION için PendingIntent oluşturun
        Intent okIntent = new Intent(context, AlarmReceiver.class);
        okIntent.setAction("OK_ACTION");
        PendingIntent okPendingIntent;
        if (Build.VERSION.SDK_INT >= 31) {
            okPendingIntent = PendingIntent.getBroadcast(context, 0, okIntent, PendingIntent.FLAG_MUTABLE);
        } else {
            okPendingIntent = PendingIntent.getBroadcast(context, 0, okIntent, 0);
        }
        customNavigationView.setOnClickPendingIntent(R.id.btnOk, okPendingIntent);

        // DELETE_FIREBASE_DATA için PendingIntent oluşturun
        Intent deleteIntent = new Intent(context, AlarmReceiver.class);
        deleteIntent.setAction("DELETE_FIREBASE_DATA");
        deleteIntent.putExtra("alarmId", alarmId);
        PendingIntent okContentIntent;
        if (Build.VERSION.SDK_INT >= 31) {
            okContentIntent = PendingIntent.getBroadcast(context, 0, deleteIntent, PendingIntent.FLAG_MUTABLE);
        } else {
            okContentIntent = PendingIntent.getBroadcast(context, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        // Burada özel TextView'a metni ayarlayın
        customNavigationView.setTextViewText(R.id.btnOk, "Complete");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "alarm_channel";
            CharSequence channelName = "Alarm Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarm_channel")
                .setSmallIcon(R.drawable.logo10)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{1000, 1000, 1000})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .addAction(R.mipmap.ic_launcher, "Complete", okContentIntent)
                .setColor(Color.BLACK)
                .setContent(customNavigationView);

        Log.d("AlarmService", "Selected alarm URI: " + selectedAlarmUri);


        if (millis>millis1){
            notificationSound = MediaPlayer.create(getApplicationContext(),selectedAlarmUri);
            notificationSound.start();


        }else if (millis<millis1){
            if (selectedSoundUriString != null) {
                Uri selectedSoundUri = Uri.parse(selectedSoundUriString);

                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());

                try {
                    mediaPlayer.setDataSource(getApplicationContext(), selectedSoundUri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }




        }




        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
    }


    public void sendBillNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent deleteIntent = new Intent(context, AlarmReceiver.class);
        deleteIntent.setAction("DELETE_BILL");
        deleteIntent.putExtra("alarmId", alarmId);

        PendingIntent okContentIntent;
        // PendingIntent'i Broadcast olarak oluşturun
        okContentIntent = PendingIntent.getBroadcast(context, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Alarm Channel";
            CharSequence channelName = "Alarm Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Alarm Channel")
                .setSmallIcon(R.drawable.logo10)
                .setContentTitle(title)
                .setContentText(description)
                .setContentInfo("Fatura Hatırlatıcı")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{1000,1000, 1000}) // Sadece titreşim, 5 saniye titreşim
                .setSound(Uri.EMPTY) // Bildirim sesini null olarak ayarlayın
                .setColor(Color.BLACK);

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 100, okContentIntent);

        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
    }






    public void stopAlarmSound() {
        try {
            if (notificationSound != null && notificationSound.isPlaying()) {
                notificationSound.stop();
                notificationSound.release();
                alarmStarted = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
