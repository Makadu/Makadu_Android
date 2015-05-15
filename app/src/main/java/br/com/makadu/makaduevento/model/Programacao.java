package br.com.makadu.makaduevento.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lucasschwalbeferreira on 16/02/15.
 */
public class Programacao implements Serializable{

    private String id;
    private String titulo;
    private String descricao;
    private Date data;
    private String horaInicio;
    private String horaFim;
    private String local;
    private byte[] foto;
    private ArrayList<Palestrante> palestrantes;
    private String url;
    private ArrayList<Question> questions;
    private boolean allow_file;
    private boolean allow_question;

    public Programacao(String id, String titulo, String descricao, Date data, String horaInicio, String horaFim, String local, byte[] foto) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.local = local;
        this.foto = foto;
    }

    public Programacao(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() { return descricao; };

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getLocal() { return local; }

    public void setLocal(String local) { this.local = local; }

    public byte[] getFoto() { return foto; }

    public void setFoto(byte[] foto) { this.foto = foto; }

    public ArrayList<Palestrante> getPalestrantes() {
        return palestrantes;
    }

    public void setPalestrantes(ArrayList<Palestrante> palestrantes) {
        this.palestrantes = palestrantes;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public boolean isAllow_file() {
        return allow_file;
    }

    public void setAllow_file(boolean allow_file) {
        this.allow_file = allow_file;
    }

    public boolean isAllow_question() {
        return allow_question;
    }

    public void setAllow_question(boolean allow_question) {
        this.allow_question = allow_question;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public String retornaPalestrantesList(){
        String string_palestrantes = "";
        for(Palestrante pa : this.palestrantes) {
            string_palestrantes += " - " + pa.getNome();
        }

        return string_palestrantes;
    }

}
