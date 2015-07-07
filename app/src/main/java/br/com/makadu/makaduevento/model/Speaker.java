package br.com.makadu.makaduevento.model;

import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by lucasschwalbeferreira on 24/03/15.
 */
public class Speaker implements Serializable {

    private String id;
    private String nome;
    private String descricao_palestrante;

    public Speaker(){}

    public Speaker(String id, String nome, String descricao_palestrante) {
        this.id = id;
        this.nome = nome;
        this.descricao_palestrante = descricao_palestrante;
    }

    public Speaker(String nome, String descricao_palestrante) {
        this.nome = nome;
        this.descricao_palestrante = descricao_palestrante;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao_palestrante() {
        return descricao_palestrante;
    }

    public void setDescricao_palestrante(String descricao_palestrante) {
        this.descricao_palestrante = descricao_palestrante;
    }
}
