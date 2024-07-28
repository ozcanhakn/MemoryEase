package app.forget.forgetfulnessapp.CreateReminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import app.forget.forgetfulnessapp.CreateReminderActivity;
import app.forget.forgetfulnessapp.R;
import io.reactivex.rxjava3.annotations.Nullable;

public class SelectAlarmSoundActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer1, mediaPlayer2, mediaPlayer3, mediaPlayer4, mediaPlayer5, mediaPlayer6,mediaPlayerDevice;
    ImageView playTheSkyWithinRingtone, playDefaultRingtone, playClockAlarmRingtone, playAPlaceInTheRingtone, playMiamiBeachRingtone, playDreamBigRingtone;
    ImageView stopTheSkyWithinRingtone, stopDefaultRingtone, stopClockAlarmRingtone, stopAPlaceInTheRingtone, stopMiamiBeachRingtone, stopDreamBigRingtone;
    ImageView backSelectSound;

    private ProgressBar progressBar1, progressBar2, progressBar3, progressBar4, progressBar5, progressBar6;


    private static final int PICK_AUDIO_REQUEST = 1;
    private Spinner alarmSoundSpinner;
    private Button selectAlarmButton;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_alarm_sound);

        handler = new Handler();


        // Ses seçenekleri için butonları tanımla
        Button btnSound1 = findViewById(R.id.btnSound1);
        Button btnSound2 = findViewById(R.id.btnSound2);
        Button btnSound3 = findViewById(R.id.btnSound3);
        Button btnSound4 = findViewById(R.id.btnSound4);
        Button btnSound5 = findViewById(R.id.btnSound5);
        Button btnSound6 = findViewById(R.id.btnSound6);
        Button btnSoundMyDevice = findViewById(R.id.btnSoundDevice);

        btnSoundMyDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });




        playAPlaceInTheRingtone = findViewById(R.id.playAPlaceInTheRingtone);
        playClockAlarmRingtone = findViewById(R.id.playClockAlarmRingtone);
        playDefaultRingtone = findViewById(R.id.playDefaultRingtone);
        playTheSkyWithinRingtone = findViewById(R.id.playTheSkyRingtone);
        playMiamiBeachRingtone = findViewById(R.id.playMiamiBeachRingtone);
        playDreamBigRingtone = findViewById(R.id.playDreamBigRingtone);


        stopAPlaceInTheRingtone = findViewById(R.id.stopAPlaceInTheRingtone);
        stopClockAlarmRingtone = findViewById(R.id.stopClockAlarmRingtone);
        stopDefaultRingtone = findViewById(R.id.stopDefaultRingtone);
        stopTheSkyWithinRingtone = findViewById(R.id.stopTheSkyRingtone);
        stopMiamiBeachRingtone = findViewById(R.id.stopMiamiBeachRingtone);
        stopDreamBigRingtone = findViewById(R.id.stopDreamBigRingtone);


        mediaPlayer1 = MediaPlayer.create(this, R.raw.theskywithin);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.aplaceintheuniverse);
        mediaPlayer3 = MediaPlayer.create(this, R.raw.ringtonedefault);
        mediaPlayer4 = MediaPlayer.create(this, R.raw.clockalarm);
        mediaPlayer5 = MediaPlayer.create(this, R.raw.miamibeach);
        mediaPlayer6 = MediaPlayer.create(this, R.raw.dreambig);


        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar3 = findViewById(R.id.progressBar3);
        progressBar4 = findViewById(R.id.progressBar4);
        progressBar5 = findViewById(R.id.progressBar5);
        progressBar6 = findViewById(R.id.progressBar6);


        backSelectSound = findViewById(R.id.backSelectSound);




        backSelectSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectAlarmSoundActivity.this,CreateReminderActivity.class);
                startActivity(intent);
            }
        });

        playTheSkyWithinRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound(mediaPlayer1,progressBar1);
            }
        });

        playAPlaceInTheRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound(mediaPlayer2,progressBar2);
            }
        });

        playDefaultRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound(mediaPlayer3,progressBar3);
            }
        });

        playClockAlarmRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound(mediaPlayer4,progressBar4);
            }
        });

        playMiamiBeachRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound(mediaPlayer5,progressBar5);
            }
        });

        playDreamBigRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound(mediaPlayer6,progressBar6);
            }
        });



        stopTheSkyWithinRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSound(mediaPlayer1,progressBar1);
            }
        });

        stopAPlaceInTheRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSound(mediaPlayer2,progressBar2);
            }
        });

        stopDefaultRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSound(mediaPlayer3,progressBar3);
            }
        });

        stopClockAlarmRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSound(mediaPlayer4,progressBar4);
            }
        });

        stopMiamiBeachRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSound(mediaPlayer5,progressBar5);
            }
        });

        stopDreamBigRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSound(mediaPlayer6,progressBar6);
            }
        });



        btnSound5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currentTimeMillis = System.currentTimeMillis();
                saveSelectedSoundUri(R.raw.miamibeach,currentTimeMillis);
                Toast.makeText(SelectAlarmSoundActivity.this, getString(R.string.voiceselected)+"Miami Beach", Toast.LENGTH_SHORT).show();

            }
        });

        btnSound6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currentTimeMillis = System.currentTimeMillis();
                saveSelectedSoundUri(R.raw.dreambig,currentTimeMillis);
                Toast.makeText(SelectAlarmSoundActivity.this, getString(R.string.voiceselected)+"Dream Big", Toast.LENGTH_SHORT).show();


            }
        });



        // Butonlara tıklama olaylarını ekle
        btnSound1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTimeMillis = System.currentTimeMillis();
                saveSelectedSoundUri(R.raw.theskywithin,currentTimeMillis);
                Toast.makeText(SelectAlarmSoundActivity.this, getString(R.string.voiceselected)+"The Sky With In", Toast.LENGTH_SHORT).show();

            }
        });

        btnSound2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTimeMillis = System.currentTimeMillis();
                saveSelectedSoundUri(R.raw.aplaceintheuniverse,currentTimeMillis);
                Toast.makeText(SelectAlarmSoundActivity.this, getString(R.string.voiceselected)+"A Place In The Universe", Toast.LENGTH_SHORT).show();

            }
        });

        btnSound3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTimeMillis = System.currentTimeMillis();
                saveSelectedSoundUri(R.raw.ringtonedefault,currentTimeMillis);
                Toast.makeText(SelectAlarmSoundActivity.this, getString(R.string.voiceselected)+"Default Ringtone", Toast.LENGTH_SHORT).show();

            }
        });

        btnSound4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTimeMillis = System.currentTimeMillis();
                saveSelectedSoundUri(R.raw.clockalarm,currentTimeMillis);
                Toast.makeText(SelectAlarmSoundActivity.this, getString(R.string.voiceselected)+"Clock Alarm", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, PICK_AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Seçilen ses dosyasının URI'sini al ve işlemlere devam et
            Uri selectedSoundUri = data.getData();
            copyFileToAppStorage(selectedSoundUri);
            Log.d("SelectedSoundUri", "Dosya Yolu: " + selectedSoundUri.toString());

            long currentMillis = System.currentTimeMillis();

            // Kullanıcının seçtiği dosya yolunu shared preferences'e kaydetmek
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("selected_sound_uri1", selectedSoundUri.toString());
            editor.putLong("time1",currentMillis);
            editor.apply();
        }
    }

    private void copyFileToAppStorage(Uri selectedSoundUri) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // Kullanıcının seçtiği dosyanın InputStream'ini alın
            inputStream = getContentResolver().openInputStream(selectedSoundUri);

            // Kopyalanacak dosyanın çıktı akışını oluşturun
            File outputDir = getFilesDir(); // Uygulama dosya dizini
            File outputFile = new File(outputDir, "selected_sound_file.mp3");
            outputStream = new FileOutputStream(outputFile);

            // Dosyayı kopyalama işlemi
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Uygulama dosyasına kopyalanan dosyanın URI'sini alın
            Uri copiedFileUri = Uri.fromFile(outputFile);

            //playSound1(copiedFileUri);


            // Kullanıcı seçtiği dosyanın URI'sini SharedPreferences'e kaydet
            //saveSelectedSoundUri(copiedFileUri);
            Integer de = getSoundUriId(copiedFileUri);

            long currentTimeMillis = System.currentTimeMillis();



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // InputStream ve OutputStream'i kapat
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // MediaPlayer ile sesi çalma metodu
    /*private void playSound1(Uri soundUri) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), soundUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    // Seçilen ses dosyasının URI'sini int bir değere dönüştüren metod
    private int getSoundUriId(Uri soundUri) {
        String stringUri = soundUri.toString();
        int uriId = stringUri.hashCode();
        return uriId;
    }


    private void playSound(final MediaPlayer mediaPlayer, final ProgressBar progressBar) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();

            // Progress bar'ı ve süre göstergesini güncelle
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer.isPlaying()) {
                        int progress = (int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100);
                        progressBar.setProgress(progress);



                        handler.postDelayed(this, 100); // Her 100 milisaniyede bir güncelle
                    }
                }
            }, 100);
        }
    }

    private void stopSound(MediaPlayer mediaPlayer, ProgressBar progressBar) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0); // Sesin başına dön
            progressBar.setProgress(0); // Progress bar'ı sıfırla
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // MediaPlayer nesnelerini serbest bırak
        if (mediaPlayer1 != null) {
            mediaPlayer1.release();
        }
        if (mediaPlayer2 != null) {
            mediaPlayer2.release();
        }
        if (mediaPlayer3 != null) {
            mediaPlayer3.release();
        }
        if (mediaPlayer4 != null){
            mediaPlayer4.release();
        }
        if (mediaPlayer5 != null){
            mediaPlayer5.release();
        }
    }




    private void saveSelectedSoundUri(Integer uriString,long millis) {
        SharedPreferences preferences = getSharedPreferences("AlarmSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("selectedAlarmUri", uriString);
        editor.putLong("time", millis);

        editor.apply();
    }


}