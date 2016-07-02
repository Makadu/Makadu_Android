package br.com.makadu.makaduevento.DAO.dao.entityDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.makadu.makaduevento.DAO.dao.Dao;
import br.com.makadu.makaduevento.DAO.table.EventTable;
import br.com.makadu.makaduevento.model.Event;

/**
 * Created by lucasschwalbeferreira on 10/1/15.
 */
public class EventDao extends Dao<Event> {

    public EventDao(Context context) {
        super(context);
    }

    public void upsert(Event entity) {
        if(entity != null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            try {

                    ContentValues contentValues = new ContentValues();

                    contentValues.put(EventTable.COLUMN_ID, Long.parseLong(entity.getId()));
                    contentValues.put(EventTable.COLUMN_TITLE, entity.getTitle());
                    contentValues.put(EventTable.COLUMN_DESCRIPTION, entity.getDescription());
                    contentValues.put(EventTable.COLUMN_ADDRESS, entity.getAddress());
                    contentValues.put(EventTable.COLUMN_VANUE, entity.getVenue());
                    contentValues.put(EventTable.COLUMN_CITY, entity.getCity());
                    contentValues.put(EventTable.COLUMN_STRING_LOGO, entity.logo);
                    contentValues.put(EventTable.COLUMN_STATE, entity.getState());
                    contentValues.put(EventTable.COLUMN_START_DATE, entity.getStart_date());
                    contentValues.put(EventTable.COLUMN_END_DATE, entity.getEnd_date());
                    contentValues.put(EventTable.COLUMN_ACTIVE, 1);
                    contentValues.put(EventTable.COLUMN_EVENT_TYPE, entity.event_type);
                    contentValues.put(EventTable.COLUMN_EVENT_PASSWORD, entity.password);
                    contentValues.put(EventTable.COLUMN_HAVE_PAPERS, entity.have_papers == true ? 1 : 0);

                    //contentValues.put(EventTable.COLUMN_UPDATED_AT, String.valueOf(entity.updated_at));

                    if (containEvent(entity)) {
                        db.update(EventTable.TABLE_EVENT, contentValues, "_id = " + Long.parseLong(entity.getId()), null);
                        Log.v("LOG_DAO", "Atualizou:" + entity.getTitle());
                    } else {
                        db.insert(EventTable.TABLE_EVENT, null, contentValues);
                        Log.v("LOG_DAO", "Inseriu:" + entity.getTitle());
                    }


            } catch (Exception e) {
                Log.e("Error EventDao", "upsert: " + e.getMessage());
            } finally {
                if (db != null || db.isOpen()) {
                    db.close();
                }
            }
        }
    }

    public void upsert(List<Event> events) {
        Log.v("LOG_DAO", " INI");

            removeAll();
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            try {
                Log.v("LOG_DAO", " POS TRY");
                for (Event entity: events) {
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(EventTable.COLUMN_ID, Long.parseLong(entity.getId()));
                    contentValues.put(EventTable.COLUMN_TITLE, entity.getTitle());
                    contentValues.put(EventTable.COLUMN_DESCRIPTION, entity.getDescription());
                    contentValues.put(EventTable.COLUMN_ADDRESS, entity.getAddress());
                    contentValues.put(EventTable.COLUMN_VANUE, entity.getVenue());
                    contentValues.put(EventTable.COLUMN_CITY, entity.getCity());
                    contentValues.put(EventTable.COLUMN_STRING_LOGO, entity.logo);
                    contentValues.put(EventTable.COLUMN_STATE, entity.getState());
                    contentValues.put(EventTable.COLUMN_START_DATE, entity.getStart_date());
                    contentValues.put(EventTable.COLUMN_END_DATE, entity.getEnd_date());
                    contentValues.put(EventTable.COLUMN_ACTIVE, entity.active);
                    contentValues.put(EventTable.COLUMN_EVENT_TYPE, entity.event_type);
                    contentValues.put(EventTable.COLUMN_EVENT_PASSWORD, entity.password);
                    contentValues.put(EventTable.COLUMN_HAVE_PAPERS, entity.have_papers == true ? 1 : 0);

                    if (containEvent(entity)) {
                        db.update(EventTable.TABLE_EVENT, contentValues, "_id = " + Long.parseLong(entity.getId()), null);
                        Log.v("LOG_DAO", "Atualizou:" + entity.getTitle());
                    } else {
                        db.insert(EventTable.TABLE_EVENT, null, contentValues);
                        Log.v("LOG_DAO", "Inseriu:" + entity.getTitle());
                    }
                }

            } catch (Exception e) {
                Log.e("Error EventDao", "upsert: " + e.getMessage());
            } finally {
                if (db != null || db.isOpen()) {
                    db.close();
                }
            }
    }

    public Event save(Event entity) {
        return null;
    }

    public int update(Event entity) {
        return 0;
    }

    public int remove(Event entity) {
        return 0;
    }

    public int removeAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int countRows = 0;

        try {
            countRows = db.delete(EventTable.TABLE_EVENT,"", null);
        }catch (Exception e){
            Log.e("Error DAO", e.getMessage());
        }finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return  countRows;
    }

    public boolean eventPrivate(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean privado = false;
        try {

            Cursor cursor = db.query(EventTable.TABLE_EVENT, null,"_id = " + id,null,null,null, EventTable.COLUMN_TITLE + " asc");

            if (cursor.moveToNext()){

                privado = cursor.getString(11).equalsIgnoreCase("Privado");
            }

        }catch (Exception e){
            Log.e("Error CandidateDAO", e.getMessage());
        }finally {
            db.close();
        }

        return privado;
    }

    public String eventPassword(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String ePassword = "";
        try {

            Cursor cursor = db.query(EventTable.TABLE_EVENT, null,"_id = " + id,null,null,null, EventTable.COLUMN_TITLE + " asc");

            if (cursor.moveToNext()){

                ePassword = cursor.getString(12);
            }

        }catch (Exception e){
            Log.e("Error CandidateDAO", e.getMessage());
        }finally {
            db.close();
        }

        return ePassword;
    }

    public Event getById(long pk) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Event event = null;
        try {

            Cursor cursor = db.query(EventTable.TABLE_EVENT, null,"_id = " + pk,null,null,null, EventTable.COLUMN_TITLE + " asc");

            if (cursor.moveToNext()){
                event = new Event();

                event.setId((cursor.getLong(0) + ""));
                event.setTitle(cursor.getString(1));
                event.setDescription(cursor.getString(2));
                event.setAddress(cursor.getString(3));
                event.setVenue(cursor.getString(4));
                event.setCity(cursor.getString(5));
                event.logo = cursor.getString(6);
                event.setState(cursor.getString(7));
                event.setStart_date(cursor.getString(8));
                event.setEnd_date(cursor.getString(9));
                event.event_type = cursor.getString(11);
                event.password = cursor.getString(12);
                event.have_papers = cursor.getInt(13) == 1 ? true : false;
            }

        }catch (Exception e){
            Log.e("Error CandidateDAO", e.getMessage());
        }finally {
            db.close();
        }

        return event;
    }

    public List<Event> getAll() {
        List<Event> list = new ArrayList<Event>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try{

            Cursor cursor = db.query(EventTable.TABLE_EVENT, null, "active = " + 1,null,null,null, EventTable.COLUMN_TITLE + " asc");
            while (cursor.moveToNext()){
                Event event = new Event();

                event.setId((cursor.getLong(0) + ""));
                event.setTitle(cursor.getString(1));
                event.setDescription(cursor.getString(2));
                event.setAddress(cursor.getString(3));
                event.setVenue(cursor.getString(4));
                event.setCity(cursor.getString(5));
                event.logo = cursor.getString(6);
                event.state = cursor.getString(7);
                event.start_date = cursor.getString(8);
                event.end_date = cursor.getString(9);
                event.event_type = cursor.getString(11);
                event.password = cursor.getString(12);
                event.have_papers = cursor.getInt(13) == 1 ? true : false;
                //event.s(cursor.getString(10));

                list.add(event);
            }

        }catch (Exception e){
            Log.e("Error EventDao","getAll: "+ e.getMessage());
        }finally {
            if (db != null || db.isOpen()) {
                db.close();
            }
        }

        return list;
    }

    private boolean containEvent(Event entity) {

        Log.e("LOG_DAO", "entrou containEvent ID: " + entity.getId());

        boolean contain = false;
        Long id = Long.parseLong(entity.getId());

        if(id != null) {

            SQLiteDatabase db = dbHelper.getReadableDatabase();

            try {
                Cursor cursor = db.query(EventTable.TABLE_EVENT, null, EventTable.COLUMN_ID + " = " + id, null, null, null, EventTable.COLUMN_ID);
                Log.e("LOG_DAO", "entrou containEvent getCount: " + cursor.getCount());

                contain = cursor.getCount() != 0;

                }catch (Exception e) {
                Log.e("Error EventDAO", "containEvent:" +e.getMessage());
            } finally {
                /*if (db != null || db.isOpen()) {
                    db.close();
                }*/
            }
        }
        return contain;
    }
}
