package br.com.makadu.makaduevento.DAO.table;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by lucasschwalbeferreira on 4/18/16.
 */

/*
public int id;
public int event_id;
public String title;
public String reference;
public String authors;
public String Abstract;
*/
public class PaperTable {
    public static final String TABLE_PAPER = "tb_paper";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_REFERENCE = "reference";
    public static final String COLUMN_AUTHORS = "authors";
    public static final String COLUMN_ABSTRACT = "abstract";


    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_PAPER
            + " ( "
            + COLUMN_ID + " INTEGER, "
            + COLUMN_EVENT_ID + " INTEGER, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_REFERENCE + " TEXT, "
            + COLUMN_AUTHORS + " TEXT, "
            + COLUMN_ABSTRACT + " TEXT "
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
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PAPER);
        onCreate(database);
    }
}
