package br.com.makadu.makaduevento.DAO.table;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by lucasschwalbeferreira on 9/30/15.
 */

public class EventTable {
    public static final String TABLE_EVENT = "tb_event";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_SPONSOR = "sponsor"; // não usada
    public static final String COLUMN_ADVERTISEMENTS = "advertisement"; // não usada
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_VANUE = "venue";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_STRING_LOGO = "logo";
    public static final String COLUMN_IMG_LOGO = "img_logo";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_ACTIVE = "active";
    public static final String COLUMN_EVENT_TYPE = "type";
    public static final String COLUMN_EVENT_PASSWORD = "password";
    public static final String COLUMN_HAVE_PAPERS = "have_papers";
    //public static final String COLUMN_UPDATED_AT = "updated_at";



    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_EVENT
            + " ( "
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_ADDRESS + " TEXT, "
            + COLUMN_VANUE + " TEXT, "
            + COLUMN_CITY + " TEXT, "
            + COLUMN_STRING_LOGO + " TEXT, "
            + COLUMN_STATE + " TEXT, "
            + COLUMN_START_DATE + " TEXT, "
            + COLUMN_END_DATE + " TEXT, "
            + COLUMN_ACTIVE + " INTEGER, "
            + COLUMN_EVENT_TYPE + " TEXT,  "
            + COLUMN_EVENT_PASSWORD + " TEXT,  "
            + COLUMN_HAVE_PAPERS + " INTEGER  "
            //+ COLUMN_UPDATED_AT + "DATETIME "
            + " );";

    public static void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(DATABASE_CREATE);
        }catch (Exception e){
            Log.e("erro_create",e.getMessage());
        }
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(EventTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        onCreate(database);
    }

}
