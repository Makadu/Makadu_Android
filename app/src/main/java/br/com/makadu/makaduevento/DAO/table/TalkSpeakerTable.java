package br.com.makadu.makaduevento.DAO.table;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by lucasschwalbeferreira on 11/2/15.
 */
public class TalkSpeakerTable {
    public static final String TABLE_TALK_SPEAKER = "tb_talk_speaker";
    public static final String COLUMN_ID_TALK = "_id_talk";
    public static final String COLUMN_ID_SPEAKER = "_id_speaker";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_TALK_SPEAKER
            + " ( "
            + COLUMN_ID_TALK + " INTEGER, "
            + COLUMN_ID_SPEAKER + " INTEGER "
            + " FOREIGN KEY( " + COLUMN_ID_TALK + " ) REFERENCES " + TalkTable.TABLE_TALK + " (" + TalkTable.COLUMN_ID + " ) "
            + " FOREIGN KEY( " + COLUMN_ID_SPEAKER + " ) REFERENCES " + SpeakerTable.TABLE_SPEAKER + " (" + SpeakerTable.COLUMN_ID + " ) "
            + " );";

    public static void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(DATABASE_CREATE);
        }catch (Exception e){
            Log.e("erro_create", e.getMessage());
        }
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(EventTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TALK_SPEAKER);
        onCreate(database);
    }
}
