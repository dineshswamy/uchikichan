package sleeplessworks.uchikichan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by practo on 11/09/15.
 */
public class TreatmentStatusDBHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "health_records";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "TreatmentStatusDBHelper";
    // Table Names
    private static final String TABLE_TREATMENT_STATUS = "treatment_status";

    // Post Table Columns
    public static final String KEY_ID = "id";
    public static final String KEY_APPOINTMENT_ID= "appointmentId";
    public static final String KEY_STATUS_VALUE = "statusValue";
    public static final String KEY_STATUS_TIME = "statusTime";

    private static TreatmentStatusDBHelper sInstance;

    public TreatmentStatusDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized TreatmentStatusDBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TreatmentStatusDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TREATMENT_STATUS_TABLE = "CREATE TABLE " + TABLE_TREATMENT_STATUS +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_APPOINTMENT_ID + " INTEGER "+ "," +
                KEY_STATUS_VALUE + " INTEGER" + ","+
                KEY_STATUS_TIME + " DATETIME)";

        db.execSQL(CREATE_TREATMENT_STATUS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TREATMENT_STATUS);
            onCreate(db);
        }
    }
    

    public void insertIntoTreatmentStatus(String appointment_id , int statusValue, String statusTime) {
        SQLiteDatabase db = getWritableDatabase();
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_APPOINTMENT_ID, appointment_id);
            values.put(KEY_STATUS_VALUE, statusValue);

            values.put(KEY_STATUS_TIME, statusTime);
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_TREATMENT_STATUS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Return a cursor object with all rows in the table.
     * @return A cursor suitable for use in a SimpleCursorAdapter
     */
    public Cursor getAll() {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        String query = "select * from "+TABLE_TREATMENT_STATUS+" order by "+KEY_STATUS_TIME;
        return db.rawQuery(query, null);
    }


}
