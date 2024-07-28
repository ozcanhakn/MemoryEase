package app.forget.forgetfulnessapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.Voice;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import app.forget.forgetfulnessapp.Voice.RecordingDataSource;

public class VoiceRecordActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String LOG_TAG = "AudioRecordTest";


    LottieAnimationView lottieAnimationView;

    private MediaRecorder recorder;
    private MediaPlayer player;
    private String recordingFilePath;
    private ArrayList<String> recordedFiles;
    private RecordingDataSource recordingDataSource;
    private LinearLayout fileListLayout;
    private Button recordButton;

    private String currentRecordingFilePath;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_voice_record);



        // UI elements
        ImageView backButton = findViewById(R.id.backButtonVoice);
        recordButton = findViewById(R.id.recordButton);
        Button playButton = findViewById(R.id.playButton);
        fileListLayout = findViewById(R.id.fileListLayout);
        lottieAnimationView = findViewById(R.id.lottieViewSound);

        // Request audio recording permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);

        String uniqueID = UUID.randomUUID().toString(); // Rastgele bir dosya adı oluştur
        recordingFilePath = getRecordingFilePath(uniqueID);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VoiceRecordActivity.this,HomeScreen.class);
                startActivity(intent);
            }
        });
        // Set up the directory for saving recordings
        File recordingsDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Recordings");
        if (!recordingsDirectory.exists()) {
            recordingsDirectory.mkdirs();
        }

        // Set initial recording file path
        recordingFilePath = recordingsDirectory.getAbsolutePath() + "/audio_record.3gp";

        // Initialize recordingDataSource
        recordingDataSource = new RecordingDataSource(this);

        // Initialize recordedFiles list
        recordedFiles = new ArrayList<>();

        // Set up UI click listeners
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecord(recordButton);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlay(playButton);
            }
        });

        // Load existing recorded files
        loadRecordedFiles();
        updateFileList(fileListLayout, recordedFiles);
    }


    private void startRecording(String recordingName) {
        recorder = new MediaRecorder();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        currentRecordingFilePath = getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + recordingName + "_" + timeStamp + ".3gp";
        recordedFiles.add(currentRecordingFilePath);



        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(currentRecordingFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        recordingDataSource.addRecording(currentRecordingFilePath);


        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
        Log.d(LOG_TAG, "Recording started. File path: " + currentRecordingFilePath);
    }

    private String getRecordingFilePath(String uniqueID) {
        File recordingsDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Recordings");
        if (!recordingsDirectory.exists()) {
            recordingsDirectory.mkdirs();
        }
        return recordingsDirectory.getAbsolutePath() + "/audio_" + uniqueID + ".3gp";
    }

    private void onRecord(Button button) {
        if (button.getText().equals(getString(R.string.start_recording))) {
            showNameInputDialog();  // İsim girişi için dialog göster
        } else {
            stopRecording();
            button.setText(R.string.start_recording);
            updateFileList(findViewById(R.id.fileListLayout), recordedFiles);

        }
    }

    private void onPlay(Button button) {
        if (button.getText().equals(getString(R.string.start_playing))) {
            startPlaying();
            button.setText(R.string.stop_playing);
        } else {
            stopPlaying();
            button.setText(R.string.start_playing);
        }
    }


    private void updateFileList(LinearLayout fileListLayout, ArrayList<String> recordings) {
        fileListLayout.removeAllViews();

        // Yeni eklenen kod: Boşluk boyutu
        int spacingInDp = 30;
        int spacingInPx = (int) (spacingInDp * getResources().getDisplayMetrics().density);

        for (String filePath : recordings) {
            View recordingItemView = getLayoutInflater().inflate(R.layout.recording_item, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            // Yeni eklenen kod: Boşluk ayarı
            params.setMargins(0, 0, 0, spacingInPx);
            recordingItemView.setLayoutParams(params);

            TextView textView = recordingItemView.findViewById(R.id.recordingTextView);
            Button playButton = recordingItemView.findViewById(R.id.playButton);
            Button deleteButton = recordingItemView.findViewById(R.id.deleteButton);

            String fileName = getFileNameFromPath(filePath);
            String deneme = fileName.substring(0,20);
            textView.setText(deneme);

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playRecordedFile(filePath);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteDialog(filePath);
                }
            });

            fileListLayout.addView(recordingItemView);
        }
    }


    private void showDeleteDialog(final String filePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Recording");
        builder.setMessage("Are you sure you want to delete this recording?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteRecording(filePath);
                loadRecordedFiles();
                updateFileList(fileListLayout, recordedFiles);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deleteRecording(String filePath) {
        File fileToDelete = new File(filePath);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
            recordingDataSource.deleteRecording(filePath);
        }
    }

    // Yeni metod: Kayıt başlatılacağı zaman isim girişi için dialog gösteren fonksiyon
    // Yeni metod: Kayıt başlatılacağı zaman isim girişi için dialog gösteren fonksiyon
    private void showNameInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.enterrecordname));

        // Layout dosyasını tanımla
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);

        // EditText'i layout dosyasından al
        final EditText input = viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String recordingName = input.getText().toString().trim();
                if (!TextUtils.isEmpty(recordingName)) {
                    // LottieAnimationView'i görünür yap
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    // Ses kaydını başlat
                    startRecording(recordingName);
                    // Kayıt butonunun metnini güncelle
                    recordButton.setText(R.string.stop_recording);
                } else {
                    // Kullanıcı isim girmemişse uyarı verebilirsiniz
                    Toast.makeText(getApplicationContext(), getString(R.string.pleaseentervalidname), Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;

            lottieAnimationView.setVisibility(View.GONE);
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(recordingFilePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void loadRecordedFiles() {
        recordingDataSource.open();
        recordedFiles.clear();
        ArrayList<String> dbRecordings = recordingDataSource.getAllRecordings();
        recordedFiles.addAll(dbRecordings);
        Log.d("VoiceRecordActivity", "loadRecordedFiles: Loaded files count - " + recordedFiles.size());
        recordingDataSource.close();

    }

    private void playRecordedFile(String filePath) {
        stopPlaying();

        player = new MediaPlayer();
        try {
            player.setDataSource(filePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getFileNameFromPath(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    @Override
    public void onStop() {
        super.onStop();
        lottieAnimationView.setVisibility(View.GONE);

        if (recorder != null) {
            stopRecording();
        }

        if (player != null) {
            stopPlaying();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Load existing recorded files
        loadRecordedFiles();
        updateFileList(fileListLayout, recordedFiles);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        if (player != null) {
            player.release();
            player = null;
        }
        if (recordingDataSource != null) {
            recordingDataSource.close();
        }
    }
}