package br.com.makadu.makaduevento.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import br.com.makadu.makaduevento.Util.Util;

/**
 * Created by lucasschwalbeferreira on 16/02/15.
 */

/*    "id": 1526,
    "title": "Mais uma palestras para teste",
    "start_time": "2016-03-18T08:08:00.000-03:00",
    "end_time": "2016-03-18T20:07:00.000-03:00",
    "room": "Testando oskdjaskjdlaksjd",
    "allow_question": true,
    "allow_download": true,
    "speakers": "Felipe Diligenti e João Borges Fortes",
    "active": true,
    "allow_favorite": true,
    "interactive": false,
    "description": "alksj fjhsdkjfh sdkjhf kjsdh kjshd kjhsdkj hsdkjh kjsdh kjshd kjhd",
    "updated_at": "2016-03-24T15:50:45.952-03:00"*/

public class Talk implements Serializable {
    public String id;
    private String title;
    private String descricao;
    public String description;
    private Date data;
    public Date start_time;
    private Date end_time;
    private String room;
    public boolean active;
    private byte[] foto;
    public String speakers;
    private String url;
    private ArrayList<Question> questions1;
    private boolean downloads;
    private boolean questions;
    public boolean interactive;
    public boolean allow_question;
    public boolean allow_download;
    public int bd_downloads;
    public int bd_questions;
    public String bd_start_time;
    public String bd_end_time;
    public String updated_at;
    public String event_id;

    public Talk() {
    }

    public Talk(String id, String title, String descricao, Date data, Date start_time, Date end_time, String room, byte[] foto, String speakers, ArrayList<Speaker> speakers2, String url, ArrayList<Question> questions1, boolean downloads, boolean questions, String updated_at) {
        this.id = id;
        this.title = title;
        this.descricao = descricao;
        this.data = data;
        this.start_time = start_time;
        this.end_time = end_time;
        this.room = room;
        this.foto = foto;
        this.speakers = speakers;
        this.url = url;
        this.questions1 = questions1;
        this.downloads = downloads;
        this.questions = questions;
        this.updated_at = updated_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setSpeakers(String speakers) {
        this.speakers = speakers;
    }


    public ArrayList<Question> getQuestions1() {
        return questions1;
    }

    public void setQuestions1(ArrayList<Question> questions1) {
        this.questions1 = questions1;
    }

    public boolean isDownloads() {
        return allow_download;
    }

    public void setDownloads(boolean downloads) {
        this.downloads = downloads;
    }

    public boolean isQuestions() {
        return allow_question;
    }

    public void setQuestions(boolean questions) {
        this.questions = questions;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return title;
    }

    public void setTitulo(String titulo) {
        this.title = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return this.start_time;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHoraInicio() {
        if(this.start_time != null) {
            this.bd_start_time = new Util().getHour(this.start_time);
            Log.v("LOG_DATA_c","LOG_DATA_start: " + this.start_time + "");
            Log.v("LOG_DATA_c","LOG_DATA_bd_start: "+ this.bd_start_time + "");

        }
        Log.v("LOG_DATA","LOG_DATA_startbd : " + bd_start_time);
        return bd_start_time;
    }

    public String getHoraFim() {
        if(this.end_time != null)
            this.bd_end_time = new Util().getHour(this.end_time);
        return bd_end_time;
    }

    public String getLocal() {
        return room;
    }

    public void setLocal(String local) {
        this.room = local;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isAllow_file() {
        return this.allow_download;
    }

    public void setAllow_file(boolean allow_file) {
        this.downloads = allow_file;
    }

    public boolean isAllow_question() {
        return this.allow_question;
    }

    public void setAllow_question(boolean allow_question) {
        this.questions = allow_question;
    }

    public ArrayList<Question> getQuestions() {
        return questions1;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions1 = questions;
    }

    public String retornaPalestrantesList() {

        return speakers;
    }

}