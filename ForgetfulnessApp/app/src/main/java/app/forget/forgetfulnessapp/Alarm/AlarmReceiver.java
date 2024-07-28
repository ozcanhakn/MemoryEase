package app.forget.forgetfulnessapp.Alarm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import app.forget.forgetfulnessapp.R;
import io.reactivex.rxjava3.annotations.NonNull;

public class AlarmReceiver extends BroadcastReceiver {
    private List<String> secilenGunlerListesi = new ArrayList<>();

    private DatabaseReference referenceTime;
    private DatabaseReference referenceDate;
    private DatabaseReference referenceLatitude;
    private DatabaseReference referenceType;
    private DatabaseReference referenceBill;

    String userLocationReminderUID;
    String alarmId;
    String description;
    String title;

    String title1;
    String description1;

    String recc;


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("DÜZENLİRECEİVER", "onReceive: çalıştı onReceive");


        if (intent.getAction() !=null && intent.getAction().equals("DELETE_FIREBASE_DATA")){
            Log.d("LOG", "deleteFirebaseData: " + "deleteFirebaseData metodu çalıştı");
            alarmId = intent.getStringExtra("alarmId");

            stopAlarmSoundInService(context);


            if (alarmId !=null){

                deleteFirebaseData(alarmId);



            }else {
                Log.d("ALARMID", "ALARM ID NULL DEĞERDE: ");
            }



        }else if (intent.getAction() !=null && intent.getAction().equals("DELETE_BILL")){
            Log.d("BILL", "deleteFirebaseData: " + "YÖNLENDİRDİ");
            alarmId = intent.getStringExtra("alarmId");

            stopBillInService(context);


            if (alarmId !=null){

                deleteBill(alarmId);
                Log.d("BILL", "onReceive: "+alarmId);



            }else {
                Log.d("ALARMID", "ALARM ID NULL DEĞERDE: ");
            }
        }

        //Firebase Realtime Database'e erişim sağlama
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        Log.d("DENEME100", "onReceive: "+ userId);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        referenceDate =  database.getReference("Reminder").child(userId);
        referenceTime =  database.getReference("Reminder").child(userId);
        referenceLatitude =  database.getReference("Reminder").child(userId);
        referenceType = database.getReference("Reminder").child(userId);
        referenceBill = database.getReference("Bill").child(userId);


        referenceBill.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot billSnapshot : snapshot.getChildren()){
                    String alarmDate = billSnapshot.child("date").getValue(String.class);
                    String status = billSnapshot.child("status").getValue(String.class);
                    if (status.equals("not completed")){
                        Log.d("BILL", "onDataChange: " + alarmDate + " BILL");
                        if (alarmDate != null) {
                            checkBillTime(context, alarmDate);
                        }
                        alarmId = billSnapshot.child("id").getValue(String.class);
                    }else {
                        Log.d("BILL", "onDataChange: bill zaten completed değerde 02");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(context, "Hata Durumu(Bill)", Toast.LENGTH_SHORT).show();

            }
        });

        referenceLatitude.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot locationSnapshot : snapshot.getChildren()){
                    Double alarmLongitude = locationSnapshot.child("latitude").getValue(Double.class);
                    Double alarmLatitude = locationSnapshot.child("longitude").getValue(Double.class);
                    String status = locationSnapshot.child("status").getValue(String.class);

                    Log.d("LOG", "onDataChange: " + alarmLongitude + " Receiver'da alındı");
                    Log.d("LOG", "onDataChange: " + alarmLatitude + " Receiver'da alındı");
                    Log.d("LOG", "onDataChange: "+ status);


                    if (alarmLongitude != 0 && alarmLatitude != 0) {


                        if (status.equals("not completed")){
                            //alarmId = locationSnapshot.child("id").getValue(String.class);
                            checkAlarmLocation(context);

                        }

                    } else {
                        Log.d("LOG", "onDataChange: Konum bilgileri bulunamadı!");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("LOG", "onCancelled: Konum bilgileri bulunamadı!");
            }
        });

        referenceDate.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                        String alarmDate = dateSnapshot.child("date").getValue(String.class);
                        String status = dateSnapshot.child("status").getValue(String.class);
                        String type = dateSnapshot.child("type").getValue(String.class);

                        assert type != null;
                        if (status.equals("not completed") && type.equals("alarm")){
                            Log.d("LOG", "onDataChange: " + alarmDate + " RECEIVER");
                            if (alarmDate != null) {
                                checkAlarmTime(context, alarmDate);
                            }
                            alarmId = dateSnapshot.child("id").getValue(String.class);

                        }else {
                            Log.d("LOG", "onDataChange: referenceDate zaten completed değerde 02");
                        }
                        }
                    }


                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(context, "Hata Durumu(Date)", Toast.LENGTH_SHORT).show();
                }
            });

        referenceType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dayOfWeekSnapshot : snapshot.getChildren()){
                    String type = dayOfWeekSnapshot.child("type").getValue(String.class);
                    String time = dayOfWeekSnapshot.child("time").getValue(String.class);
                    String friday = dayOfWeekSnapshot.child("Friday").getValue(String.class);
                    String monday = dayOfWeekSnapshot.child("Monday").getValue(String.class);
                    String saturday = dayOfWeekSnapshot.child("Saturday").getValue(String.class);
                    String wednesday = dayOfWeekSnapshot.child("Wednesday").getValue(String.class);
                    String tuesday = dayOfWeekSnapshot.child("Tuesday").getValue(String.class);
                    String thursday = dayOfWeekSnapshot.child("Thursday").getValue(String.class);
                    String sunday = dayOfWeekSnapshot.child("Sunday").getValue(String.class);




                    if (type.equals("regular")){
                        if (sunday.equals("yes")){
                            secilenGunlerListesi.add("SUNDAY");
                        }else {
                            Log.d("LOG", "onDataChange: empty(Thursday)");
                        }
                        if (monday.equals("yes")){
                            secilenGunlerListesi.add("MONDAY");
                        }else {
                            Log.d("LOG", "onDataChange: empty(Sunday)");
                        }
                        if (wednesday.equals("yes")){
                            secilenGunlerListesi.add("WEDNESDAY");
                        }else {
                            Log.d("LOG", "onDataChange: empty(Wednesday)");
                        }
                        if (friday.equals("yes")){
                            secilenGunlerListesi.add("FRIDAY");
                        }else {
                            Log.d("LOG", "onDataChange: empty(Friday)");
                        }
                        if (thursday.equals("yes")){
                            secilenGunlerListesi.add("THURSDAY");
                        }else {
                            Log.d("LOG", "onDataChange: empty(Thursday)");
                        }
                        if (tuesday.equals("yes")){
                            secilenGunlerListesi.add("TUESDAY");
                        }else {
                            Log.d("LOG", "onDataChange: empty(Tuesday)");
                        }
                        if (saturday.equals("yes")){
                            secilenGunlerListesi.add("SATURDAY");
                        }else {
                            Log.d("LOG", "onDataChange: empty(Monday)");
                        }


                        Calendar calendar = Calendar.getInstance();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                        for (String secilenGun : secilenGunlerListesi){
                            LocalDate bulunanTarih = gunuBul(secilenGunlerListesi);


                            try {
                                Log.d("DÜZENLİRECEIVER", "onDataChange: RECEİVER'A GELDİK TRY");
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                String formattedDate = bulunanTarih.format(formatter);
                                Date date = sdf.parse(formattedDate + " " + time);
                                Log.d("DÜZENLİRECEIVER", "onDataChange: "+date.toString());

                                if (date != null) {
                                    calendar.setTime(date);
                                    // Alarm zamanı geçtiyse hemen çağırın
                                    if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                                        checkAlarmDate(context, formattedDate);
                                        Log.d("DÜZENLİRECEIVER", "onDataChange: contexte gitti");
                                    }
                                    alarmId = dayOfWeekSnapshot.child("id").getValue(String.class);

                                } else {
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();

                            }

                        }


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void stopAlarmSoundInService(Context context) {
        Intent stopSoundIntent = new Intent(context, AlarmService.class);
        stopSoundIntent.setAction("STOP_ALARM_SOUND");
        context.startService(stopSoundIntent);
    }

    private void stopBillInService(Context context) {
        Intent stopSoundIntent = new Intent(context, AlarmService.class);
        stopSoundIntent.setAction("STOP_BILL");
        context.startService(stopSoundIntent);
    }


    private void checkAlarmTime(final Context context,final String alarmDate){
        referenceTime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot timeSnapshot : snapshot.getChildren()){
                String alarmTime = timeSnapshot.child("time").getValue(String.class);
                //alarmId = timeSnapshot.child("id").getValue(String.class);
                String status = timeSnapshot.child("status").getValue(String.class);
                String type = timeSnapshot.child("type").getValue(String.class);

                if (status.equals("not completed")){

                    //Log.d("LOG", "onDataChange: "+"ID"+alarmId);
                    Log.d("LOG", "onDataChange: "+alarmTime+" checkAlarmTime çalıştı");
                    if (alarmTime != null && type.equals("alarm")) {

                        String alarmDateTime = alarmDate + " " + alarmTime;
                        Log.d("LOG", "onDataChange: " + alarmDateTime + "   alarmDateTime Okundu");

                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date alarmDate1 = sdf.parse(alarmDateTime);
                            Log.d("LOG", "onDataChange: " + alarmDate1);
                            long alarmTimeMillis = alarmDate1.getTime();
                            Log.d("LOG", "onDataChange: " + alarmTimeMillis + "  Alarm zamanı CurrentTimeMillis'e çevrildi");
                            long currentTimeMillis = System.currentTimeMillis();

                            Log.d("LOG", "onDataChange: " + currentTimeMillis + "  anın currentTimeMillis değeri oluşturuldu");
                            Log.d("LOG", "onDataChange: " + alarmTimeMillis + "   alarmTimeMillis değeri alındı");
                            if (alarmTimeMillis <= currentTimeMillis) {
                                //alarm zamanı geldi, alarmı başlat
                                alarmId = timeSnapshot.child("id").getValue(String.class);
                                description = timeSnapshot.child("description").getValue(String.class);
                                title = timeSnapshot.child("title").getValue(String.class);
                                recc = "alarm";

                                startAlarmService(context);
                                Log.d("LOG", "onDataChange: " + " startAlarmService'ye yönlendirildi");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }else {
                    Log.d("LOG", "onDataChange: CheckTime alarm zaten Completed değerde 02");
                }






                }
                }


            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(context, "Alarm saati bulunamadı", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void checkBillTime(final Context context, final String alarmDate){
        referenceBill.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot billSnapshot : snapshot.getChildren()){
                    String alarmTime = billSnapshot.child("time").getValue(String.class);
                    String alarmTime1 = billSnapshot.child("time1").getValue(String.class);
                    String status = billSnapshot.child("status").getValue(String.class);
                    String status1= billSnapshot.child("status").getValue(String.class);
                    Log.d("BILLRECEIVERID", "onDataChange: "+alarmDate);

                    if (status.equals("not completed") && status1.equals("not completed")){

                        if (alarmTime!=null){
                            String alarmDateTime = alarmDate + " " + alarmTime;

                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                                Date alarmDate1 = sdf.parse(alarmDateTime);

                                long alarmTimeMillis = alarmDate1.getTime();

                                long currentTimeMillis = System.currentTimeMillis();


                                if (alarmTimeMillis <= currentTimeMillis) {
                                    //alarm zamanı geldi, alarmı başlat
                                    alarmId = billSnapshot.child("id").getValue(String.class);
                                    description = billSnapshot.child("desc").getValue(String.class);
                                    title = billSnapshot.child("title").getValue(String.class);
                                    recc = "bill";

                                    startAlarmService(context);
                                    Log.d("DÜZENLİRECEİVERD", "onDataChange: alarma gitti");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }

                    }
                    else if (status.equals("completed") && status1.equals("not completed")){
                        if (alarmTime1!=null) {
                            String alarmDateTime = alarmDate + " " + alarmTime1;


                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                                Date alarmDate1 = sdf.parse(alarmDateTime);

                                long alarmTimeMillis = alarmDate1.getTime();

                                long currentTimeMillis = System.currentTimeMillis();


                                if (alarmTimeMillis <= currentTimeMillis) {
                                    //alarm zamanı geldi, alarmı başlat
                                    alarmId = billSnapshot.child("id").getValue(String.class);
                                    description = billSnapshot.child("desc").getValue(String.class);
                                    title = billSnapshot.child("title").getValue(String.class);

                                    startAlarmService(context);
                                    Log.d("BILL", "onDataChange: alarma gitti");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void checkAlarmDate(final Context context,final String alarmDate){
        referenceTime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot timeSnapshot : snapshot.getChildren()){
                    String alarmTime = timeSnapshot.child("time").getValue(String.class);
                    //alarmId = timeSnapshot.child("id").getValue(String.class);
                    String status = timeSnapshot.child("status").getValue(String.class);
                    String type = timeSnapshot.child("type").getValue(String.class);


                    Log.d("DÜZENLİRECEİVERD", "onDataChange: "+alarmDate);

                    if (status.equals("not completed") && type.equals("regular")){

                        if (alarmTime != null) {

                            String alarmDateTime = alarmDate + " " + alarmTime;

                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Date alarmDate1 = sdf.parse(alarmDateTime);

                                long alarmTimeMillis = alarmDate1.getTime();

                                long currentTimeMillis = System.currentTimeMillis();


                                if (alarmTimeMillis <= currentTimeMillis) {
                                    //alarm zamanı geldi, alarmı başlat
                                    alarmId = timeSnapshot.child("id").getValue(String.class);
                                    description = timeSnapshot.child("description").getValue(String.class);
                                    title = timeSnapshot.child("title").getValue(String.class);
                                    recc = "alarm";


                                    startAlarmService(context);
                                    Log.d("DÜZENLİRECEİVERD", "onDataChange: alarma gitti");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }else {
                    }






                }
            }


            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }





    private void startAlarmService(Context context) {
        Log.d("LOG", "startAlarmService: "+" startAlarmService çalıştı");
        Intent serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("alarmId",alarmId);
        serviceIntent.putExtra("description",description);
        serviceIntent.putExtra("title",title);
        serviceIntent.putExtra("alarm10",recc);
        context.startForegroundService(serviceIntent);

    }



    private void checkAlarmLocation(Context context){
        Log.d("LOG", "checkAlarmLocation: checkAlarmLocation Çalıştı");
        referenceLatitude.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot locationSnapshot : snapshot.getChildren()){
                    Double alarmLocationLatitude = locationSnapshot.child("latitude").getValue(Double.class);
                    Double alarmLocationLongitude = locationSnapshot.child("longitude").getValue(Double.class);
                    String status = locationSnapshot.child("status").getValue(String.class);
                    Long distance = locationSnapshot.child("distance").getValue(Long.class);

                    if (status.equals("not completed")){
                        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                                if (location != null) {
                                    double userLatitude = location.getLatitude();
                                    double userLongitude = location.getLongitude();

                                    double targetLatitude = alarmLocationLatitude/* Hedef konumun enlemi */;
                                    double targetLongitude = alarmLocationLongitude/* Hedef konumun boylamı */;

                                    float[] results = new float[1];
                                    Location.distanceBetween(userLatitude, userLongitude, targetLatitude, targetLongitude, results);
                                    float distanceInMeters = results[0];


                                    //Location değerlerini tek bir noktada birleştirme
                    /*Location targetLocation = new Location("");
                    targetLocation.setLatitude(targetLatitude);
                    targetLocation.setLongitude(targetLongitude);


                    Location instantLocation = new Location("");
                    instantLocation.setLatitude(userLatitude);
                    instantLocation.setLongitude(userLongitude);*/

                                    userLocationReminderUID = locationSnapshot.getKey();

                                    if (distanceInMeters <= 1000) {
                                        Log.d("LOG", "onDataChange: belirtilen locasyona yakın" + "mainFragment alarm zamanı geldi" + "alarmReceiver");
                                        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);

                                        Log.d("LOG", "onDataChange: startAlarmService'e yönlendirildi");

                                        //Değerleri ALARM Serviceye burada gönderiyorum karşımaması için.
                                        alarmId = locationSnapshot.child("id").getValue(String.class);
                                        description = locationSnapshot.child("description").getValue(String.class);
                                        title = locationSnapshot.child("title").getValue(String.class);
                                        recc = "alarm";



                                        startAlarmService(context);
                                        // Kullanıcı hedef konumdan en fazla 1 km uzaklıkta
                                        // Alarmı burada başlatabilirsiniz
                                        Log.d("LOG", "onDataChange: " + userLocationReminderUID);
                                    } else {
                                        Log.d("LOG", "getLocation: " + "Kullanıcı hedeften en az 1KM uzaklıkta bulunmakta."+distanceInMeters);
                                        // Kullanıcı hedef konumdan daha uzakta
                                    }
                                }
                            });

                        }
                    }else {
                        Log.d("DEGER", "onDataChange: Location zaten completed oldugundan dolayı alarm çalmıyor 02");
                    }


                    }

                }


            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }


    //Bir üst düğümü sil
    private void deleteFirebaseData(String alarmId) {


        Log.d("Tag", "deleteFirebaseData: metot çalışıyor" );


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Log.d("LO1", "deleteFirebaseData: "+ alarmId);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();



                // Kullanıcının UID'sini alın
            Log.d("UID", "deleteFirebaseData: "+userId);

            // Reminder düğümünden kullanıcının UID'sini silecek referansı oluşturun
            DatabaseReference reminderRef = database.getReference("Reminder").child(userId).child(alarmId);

            LocalDate dateNow = LocalDate.now();
            String nowDate = dateNow.toString();


            // Düğümü silin
            //reminderRef.removeValue();
            reminderRef.child("reminderDeadline").setValue(nowDate);
            reminderRef.child("status").setValue("Completed");


        }


    private void deleteBill(String alarmId) {
        Log.d("BILL", "DELETEBILL: metot çalışıyor" );
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        Log.d("LO1", "deleteFirebaseData: "+ alarmId);

        // Kullanıcının UID'sini alın
        Log.d("UID", "deleteFirebaseData: "+userId);

        // Reminder düğümünden kullanıcının UID'sini silecek referansı oluşturun
        DatabaseReference reminderRef = database.getReference("Bill").child(userId);

        reminderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("status").getValue(String.class).equals("not completed")
                        && snapshot.child("status1").getValue(String.class).equals("not completed")) {
                            reminderRef.child(alarmId).child("status").setValue("completed");
                            Log.d("BILL", "deleteBill: status updated to completed");
                        } else if (snapshot.child("status").getValue(String.class).equals("completed")
                                && snapshot.child("status1").getValue(String.class).equals("not completed")) {
                            reminderRef.child(alarmId).child("status1").setValue("completed");
                            Log.d("BILL", "deleteBill: status1 updated to completed");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("BILL", "onCancelled: " + databaseError.getMessage());
            }
        });
    }


    private static LocalDate gunuBul(List<String> secilenGunlerListesi) {
        Log.d("201", "gunuBul: çalıştı");

        LocalDate simdikiTarih = LocalDate.now();
        LocalDate enYakinTarih = null;
        long enKucukFark = Long.MAX_VALUE;

        for (String secilenGun : secilenGunlerListesi) {
            LocalDate bulunanTarih = simdikiTarih.with(DayOfWeek.valueOf(secilenGun));

            if (bulunanTarih.isBefore(simdikiTarih)) {
                bulunanTarih = bulunanTarih.plusWeeks(1);
            }

            long fark = ChronoUnit.DAYS.between(simdikiTarih, bulunanTarih);
            if (fark < enKucukFark) {
                enKucukFark = fark;
                enYakinTarih = bulunanTarih;
            }
        }

        return enYakinTarih;
    }

}


