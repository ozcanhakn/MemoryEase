package app.forget.forgetfulnessapp.Voice;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// DBHelper.java
// DBHelper.java
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Recordings.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS recordings (" +
                            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "file_path TEXT)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("TAG", "onCreate: "+e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recordings");
        onCreate(db);
    }
}
