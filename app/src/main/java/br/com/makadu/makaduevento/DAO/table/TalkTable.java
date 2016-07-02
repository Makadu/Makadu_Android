package br.com.makadu.makaduevento.DAO.table;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by lucasschwalbeferreira on 11/1/15.
 */

public class TalkTable {
    public static final String TABLE_TALK = "tb_talk";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";
    public static final String COLUMN_ROOM = "room";
    public static final String COLUMN_QUESTIONS = "questions";
    public static final String COLUMN_DOWNLOADS = "downloads";
    public static final String COLUMN_SPEAKERS_STRING = "speakers";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ACTIVE = "active";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_ID_EVENT = "id_event";
    public static final String COLUMN_IS_INTERACTIVE = "interactive";

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_TALK
            + " ( "
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_START_TIME + " TEXT, "
            + COLUMN_END_TIME + " TEXT, "
            + COLUMN_ROOM + " TEXT, "
            + COLUMN_QUESTIONS + " INTEGER, "
            + COLUMN_DOWNLOADS + " INTEGER, "
            + COLUMN_SPEAKERS_STRING + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_ACTIVE + " INTEGER, "
            + COLUMN_UPDATED_AT + " TEXT, "
            + COLUMN_ID_EVENT + " INTEGER, "
            + COLUMN_IS_INTERACTIVE + " INTEGER "
            + " );";

    public static void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(DATABASE_CREATE);
        }catch (Exception e){
            Log.e("erro_create", e.getMessage());
        }
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(TalkTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TALK);
        onCreate(database);
    }
}
