package br.com.makadu.makaduevento.DAO.dao.entityDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.makadu.makaduevento.DAO.dao.Dao;
import br.com.makadu.makaduevento.DAO.table.TalkTable;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.model.Favorites;
import br.com.makadu.makaduevento.model.Talk;

/**
 * Created by lucasschwalbeferreira on 10/1/15.
 */
public class TalkDao extends Dao<Talk> {

    public TalkDao(Context context) {
        super(context);
    }

    public void upsert(List<Talk> talks, String eventId) {
        Log.v("LOG_DAO", " INI");
        removeAll();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            for (Talk entity: talks) {
                ContentValues contentValues = new ContentValues();

                Log.v("LOG_DAO", " TESTE DATA : " + entity.getData().toString());

                contentValues.put(TalkTable.COLUMN_ID, Long.parseLong(entity.id));
                contentValues.put(TalkTable.COLUMN_TITLE, entity.getTitle());
                contentValues.put(TalkTable.COLUMN_START_TIME, new Util().getDateHour(entity.getData()));
                contentValues.put(TalkTable.COLUMN_END_TIME, new Util().getDateHour(entity.getEnd_time()));
                contentValues.put(TalkTable.COLUMN_ROOM, entity.getRoom());
                contentValues.put(TalkTable.COLUMN_QUESTIONS, entity.allow_question == true ? 1 : 0);
                contentValues.put(TalkTable.COLUMN_DOWNLOADS, entity.allow_download == true ? 1 : 0);
                contentValues.put(TalkTable.COLUMN_SPEAKERS_STRING, entity.speakers);
                contentValues.put(TalkTable.COLUMN_DESCRIPTION, entity.description);
                contentValues.put(TalkTable.COLUMN_ACTIVE, Long.parseLong("1"));
                contentValues.put(TalkTable.COLUMN_UPDATED_AT, entity.updated_at);
                contentValues.put(TalkTable.COLUMN_ID_EVENT, Long.parseLong(eventId));
                contentValues.put(TalkTable.COLUMN_IS_INTERACTIVE, entity.interactive == true ? 1 : 0);

               // if (containTalk(entity.id,db)) {
               //     db.update(TalkTable.TABLE_TALK, contentValues, "_id = " + Long.parseLong(entity.getId()), null);
               //     Log.v("LOG_DAO", "Atualizou:" + entity.getTitle());
               // } else {
                    db.insert(TalkTable.TABLE_TALK, null, contentValues);
                    Log.v("LOG_DAO", "Inseriu:" + entity.getTitle());
                //}
            }

        } catch (Exception e) {
            Log.e("Error TalkDao", "upsert: " + e.getMessage());
        } finally {
            if (db != null || db.isOpen()) {
                db.close();
            }
        }
    }

    public List<Talk> getListTalkForEventIdTAB_TALK(long eventId) {
        List<Talk> list = new ArrayList<Talk>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {

            Cursor cursor = db.query(TalkTable.TABLE_TALK, null, "id_event = " + eventId, null, null, null, TalkTable.COLUMN_TITLE + " asc");

            while (cursor.moveToNext()){
                Talk talk = new Talk();

                Log.v("Error EventDao", " DENTRO WHILE getListTalkForEventIdTAB_TALK: ");
                Log.v("LOG_DATA","OG_DATA1: " + cursor.getString(2));
                Log.v("LOG_DATA","LOG_DATA2: " + (new Util().convertStringtoDate(cursor.getString(2))).toString());

                talk.setId(cursor.getLong(0) + "");
                talk.setTitle(cursor.getString(1));
                talk.start_time = new Util().convertStringtoDate(cursor.getString(2));
                talk.setEnd_time(new Util().convertStringtoDate(cursor.getString(3)));
                talk.setRoom(cursor.getString(4));
                talk.allow_question = cursor.getInt(5) == 1 ? true : false;
                talk.allow_download = cursor.getInt(6) == 1 ? true : false;
                talk.speakers = cursor.getString(7);
                talk.description = cursor.getString(8);
                talk.active = cursor.getInt(9) == 1 ? true : false;
                talk.updated_at = cursor.getString(1);
                talk.event_id = cursor.getString(11);
                talk.interactive = cursor.getInt(12) == 1 ? true : false;

                list.add(talk);
            }

        }catch (Exception e){
            Log.e("Error EventDao","getListTalkForEventIdTAB_TALK: "+ e.getMessage());
        }finally {
            if (db != null || db.isOpen()) {
                db.close();
            }
        }

        return list;
    }

    public String returnEventId(long talkId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String event_id = "";
        try {
            Cursor cursor = db.query(TalkTable.TABLE_TALK, null, "_id = " + talkId, null, null, null, TalkTable.COLUMN_TITLE + " asc");
            while (cursor.moveToNext()){
                Talk talk = new Talk();
                event_id = cursor.getString(11);
            }

        }catch (Exception e){
            Log.e("Error EventDao","getListTalkForEventIdTAB_TALK: "+ e.getMessage());
        }finally {
            if (db != null || db.isOpen()) {
                db.close();
            }
        }

        return event_id;
    }

    public int removeAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int countRows = 0;

        try {
            countRows = db.delete(TalkTable.TABLE_TALK,"", null);
        }catch (Exception e){
            Log.e("Error DAO", e.getMessage());
        }finally {
            db.close();
        }
        return  countRows;
    }

    private boolean containTalk(String talkid,SQLiteDatabase db) {

        boolean contain = false;
        //Long id = Long.parseLong(talkid);

        Log.v("LOG_DAO", "Long.parseLong(entity.id); " + talkid);

        Log.v("LOG_DAO", "!entity.id.isEmpty(); " + !talkid.isEmpty());

        if(!talkid.isEmpty()) {

            //SQLiteDatabase db = dbHelper.getReadableDatabase();

            try {
                Cursor cursor = db.query(TalkTable.TABLE_TALK, null, TalkTable.COLUMN_ID + " = " + talkid, null, null, null, TalkTable.COLUMN_ID);
                Log.v("LOG_DAO", "entrou containTalk getCount: " + cursor.getCount());

                contain = cursor.getCount() != 0;

            }catch (Exception e) {
                Log.e("Error TalkDAO", "containTalk:" + e.getMessage());
            }finally {
                /*if (db != null || db.isOpen()) {
                    db.close();
                }*/
            }
        }
        return contain;
    }

}
