package br.com.makadu.makaduevento.DAO.table;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by lucasschwalbeferreira on 11/2/15.
 */

public class SpeakerTable {
        public static final String TABLE_SPEAKER = "tb_speaker";
        public static final String COLUMN_ID = "_id_speaker";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ABOUT = "about";
        public static final String COLUMN_UPDATED_AT = "updated_at";

        private static final String DATABASE_CREATE = "create table "
                + TABLE_SPEAKER
                + " ( "
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_ABOUT + " TEXT, "
                + COLUMN_UPDATED_AT + " TEXT "
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
                database.execSQL("DROP TABLE IF EXISTS " + TABLE_SPEAKER);
                onCreate(database);
        }

}
