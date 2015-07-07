package br.com.makadu.makaduevento.model;

import java.io.Serializable;

/**
 * Created by lucasschwalbeferreira on 05/02/15.
 */

public class Event implements Serializable {

    private String id_parse;
    private String name;
    private String description;
    private String local;
    private String address;
    private String city;
    private String state;
    private String start_date;
    private String end_date;
    private byte[] file_img_event;
    private byte[] file_img_patronage;


    public Event(String id, String name, String description, String local, String address, String city, String state, String start_date, String end_date, byte[] file_img_event) {
        this.id_parse = id;
        this.name = name;
        this.description = description;
        this.local = local;
        this.address = address;
        this.city = city;
        this.state = state;
        this.start_date = start_date;
        this.end_date = end_date;
        this.file_img_event = file_img_event;
    }

    public Event(){};

    public String getId_Parse() {
        return id_parse;
    }

    public void setId(String id) {
        this.id_parse = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getAddress() {
        return address;
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
        return state;
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
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public byte[] getFile_img_event() {
        return file_img_event;
    }

    public void setFile_img_event(byte[] file_img_event) {
        this.file_img_event = file_img_event;
    }

    public byte[] getFile_img_patronage() {
        return file_img_patronage;
    }

    public void setFile_img_patronage(byte[] file_img_patronage) {
        this.file_img_patronage = file_img_patronage;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
