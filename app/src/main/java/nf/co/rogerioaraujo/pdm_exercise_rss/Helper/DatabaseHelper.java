package nf.co.rogerioaraujo.pdm_exercise_rss.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // database info
    private static final String TAG = "feed_database";
    private static final String DATABASE_NAME = "feed_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "FEED_UPDATED";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "LAST_UPDATE";

    // sql queries
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT)";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String UPDATE_TABLE = "UPDATE " + TABLE_NAME + " SET " + COL_2 + " = ?";
    private static final String SELECT_TABLE = "SELECT * FROM " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public boolean insertData(String last_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, last_date);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(SELECT_TABLE, null);
        return res;
    }

    public boolean updateData(String id, String last_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, last_date);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{ id });
        return true;
    }

    /*
    old methods
    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, item);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        // if database inserted incorrectly will return -1
        if (result == -1) {
            return false;
        } else return true;

    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(SELECT_TABLE, null);
        return data;
    }

    public void updateData(String newValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = UPDATE_TABLE + " = " + newValue;
        db.execSQL(query);
    }
    */
}
