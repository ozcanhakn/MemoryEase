package app.forget.forgetfulnessapp.Voice;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

// RecordingDataSource.java
public class RecordingDataSource {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public RecordingDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addRecording(String filePath) {
        open();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("file_path", filePath);
            database.insert("recordings", null, values);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            close();
        }
    }

    public ArrayList<String> getAllRecordings() {
        open();
        ArrayList<String> recordings = new ArrayList<>();
        Cursor cursor = database.query("recordings", new String[]{"file_path"}, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String filePath = cursor.getString(cursor.getColumnIndex("file_path"));
                recordings.add(filePath);
            }
            cursor.close();
        }
        close(); // Veritabanını kapat
        Log.d("LOG_TAG", "getAllRecordings: Loaded files count - " + recordings.size());
        return recordings;
    }


    public void deleteRecording(String filePath) {
        open();
        database.beginTransaction();
        try {
            database.delete("recordings", "file_path = ?", new String[]{filePath});
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            close();
        }
    }





}
