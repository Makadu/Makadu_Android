package br.com.makadu.makaduevento.DAO.dao.entityDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.makadu.makaduevento.DAO.dao.Dao;
import br.com.makadu.makaduevento.DAO.table.PaperTable;
import br.com.makadu.makaduevento.model.Paper;


/**
 * Created by lucasschwalbeferreira on 18/04/16.
 */
public class PaperDao extends Dao<Paper> {

    public PaperDao(Context context) {
        super(context);
    }

    public void upsert(List<Paper> papers, String eventId) {
        removeAll();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            for (Paper entity: papers) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(PaperTable.COLUMN_ID, entity.id);
                contentValues.put(PaperTable.COLUMN_EVENT_ID, eventId);
                contentValues.put(PaperTable.COLUMN_TITLE, entity.title);
                contentValues.put(PaperTable.COLUMN_REFERENCE, entity.reference);
                contentValues.put(PaperTable.COLUMN_AUTHORS, entity.authors);
                contentValues.put(PaperTable.COLUMN_ABSTRACT, entity.Abstract);

                db.insert(PaperTable.TABLE_PAPER, null, contentValues);

            }

        } catch (Exception e) {
            Log.e("Error PaperDao", "upsert: " + e.getMessage());
        } finally {
            if (db != null || db.isOpen()) {
                db.close();
            }
        }
    }

    public List<Paper> getListPaperForEventIdTAB_PAPER(long eventId) {
        List<Paper> list = new ArrayList<Paper>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {

            Cursor cursor = db.query(PaperTable.TABLE_PAPER, null, "event_id = " + eventId, null, null, null, PaperTable.COLUMN_TITLE + " asc");

            while (cursor.moveToNext()){
                Paper paper = new Paper();

                paper.id = (int)cursor.getLong(0);
                paper.event_id = (int)cursor.getLong(1);
                paper.title = cursor.getString(2);
                paper.reference = cursor.getString(3);
                paper.authors = cursor.getString(4);
                paper.Abstract = cursor.getString(5);

                list.add(paper);
            }

        }catch (Exception e){
            Log.e("Error PaperDao","getListPaperForEventIdTAB_PAPER: "+ e.getMessage());
        }finally {
            if (db != null || db.isOpen()) {
                db.close();
            }
        }

        return list;
    }


    public int removeAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int countRows = 0;

        try {
            countRows = db.delete(PaperTable.TABLE_PAPER,"", null);
        }catch (Exception e){
            Log.e("Error DAO", e.getMessage());
        }finally {
            db.close();
        }
        return  countRows;
    }

}
