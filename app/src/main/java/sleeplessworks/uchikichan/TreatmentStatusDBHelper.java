package sleeplessworks.uchikichan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by practo on 11/09/15.
 */
public class TreatmentStatusDBHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "health_records";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TREATMENT_STATUS = "treatment_status";

    // Post Table Columns
    private static final String KEY_ID = "id";
    private static final String KEY_APPOINTMENT_ID= "appointmentId";
    private static final String KEY_STATUS_VALUE = "status_value";
    private static final String KEY_STATUS_TIME = "status_time";

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
    

    public void insertIntoTreatmentStatus(String appointment_id , String statusValue, Date statusTime) {

    }
}
