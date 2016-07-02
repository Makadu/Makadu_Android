package br.com.makadu.makaduevento.DAO.dao;

import android.content.Context;

import java.util.List;

import br.com.makadu.makaduevento.DAO.database.DbHelper;

/**
 * Created by lucasschwalbeferreira on 9/30/15.
 */

public abstract class Dao<T> {
    protected DbHelper dbHelper;

    public Dao(Context context){
        this.dbHelper = new DbHelper(context);
    }

    //public abstract void upsert(T entity);
/*
    public abstract T save(T entity);

    public abstract int update(T entity);

    public abstract int remove(T entity);

    public abstract T getById(long pk);

    public abstract List<T> getAll();*/
}