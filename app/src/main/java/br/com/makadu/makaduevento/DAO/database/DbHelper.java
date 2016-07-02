package br.com.makadu.makaduevento.DAO.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.makadu.makaduevento.DAO.table.EventTable;
import br.com.makadu.makaduevento.DAO.table.PaperTable;
import br.com.makadu.makaduevento.DAO.table.TalkTable;

/**
 * Created by lucasschwalbeferreira on 9/30/15.
 */
public class DbHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "makadu.db";
    static final int DB_VERSION = 32;

    public DbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        EventTable.onCreate(database);
        TalkTable.onCreate(database);
        PaperTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        EventTable.onUpgrade(database, oldVersion, newVersion);
        TalkTable.onUpgrade(database, oldVersion, newVersion);
        PaperTable.onUpgrade(database, oldVersion, newVersion);
    }
}
