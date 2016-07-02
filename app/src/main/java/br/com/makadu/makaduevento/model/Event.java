package br.com.makadu.makaduevento.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import br.com.makadu.makaduevento.Util.Util;

/**
 * Created by lucasschwalbeferreira on 05/02/15.
 */

public class Event implements Serializable {

    // JSON RETROFIT
    public String id;
    public String title;
    public String description;
    public String address;
    public String venue;
    public String city;
    public String logo;
    public String logo_medium;
    public String state;
    public String start_date;
    public String end_date;
    public boolean active;
    public String event_type;
    public String password;
    public boolean have_papers;

    public String resposta;
    public String erro;

    //private  boolean active;
    public Date updated_at;


    private byte[] img_logo;


    public Event(String id, String title, String description, String venue, String address, String city, String state, String start_date, String end_date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.address = address;
        this.city = city;
        this.state = state;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public Event(){};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDescription() {
        if(this.description == null)
            this.description = "";

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        if(this.venue == null)
            this.venue = "";
        return venue;
    }

    public void setVenue(String local) {
        this.venue = local;
    }

    public String getAddress() {
        if(this.address == null)
            this.description = "";
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        if(this.state == null)
            this.state = "";

        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return this.end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public byte[] getFile_img_event(Context ctx) {

        byte[] array_byte_img = null;

        if(this.img_logo == null){
            try {
                Bitmap bmp = Picasso.with(ctx).load(this.logo).get();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                array_byte_img = stream.toByteArray();
            } catch (IOException e) {
                Log.e("ERRO_IMG",e.getMessage());
            }
        }
        else
        {
            array_byte_img = this.img_logo;
        }

        return array_byte_img;
    }

    public byte[] get_logo_sql() {
        return this.img_logo;
    }

    public void setFile_img_event(byte[] file_img_event) {
        this.img_logo = file_img_event;
    }


    @Override
    public String toString() {
        return this.getTitle();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }
}
