package br.com.makadu.makaduevento.model;

import java.io.Serializable;

/**
 * Created by lucasschwalbeferreira on 24/03/15.
 */
public class Speaker implements Serializable {

    /*
            exemplo retorno retrofit
            "id": 3,
            "name": "Guilherme Viegas",
            "about": "",
            "created_at": "2016-03-17T08:03:15.752-03:00",
            "updated_at": "2016-03-17T08:03:15.752-03:00"
    */

    private String id;
    private String name;
    private String about;

    public Speaker(){}

    public Speaker(String id, String nome, String descricao_palestrante) {
        this.id = id;
        this.name = nome;
        this.about = descricao_palestrante;
    }

    public Speaker(String nome, String descricao_palestrante) {
        this.name = nome;
        this.about = descricao_palestrante;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return name;
    }

    public void setNome(String nome) {
        this.name = nome;
    }

    public String getDescricao_palestrante() {
        return about;
    }

    public void setDescricao_palestrante(String descricao_palestrante) {
        this.about = descricao_palestrante;
    }
}
