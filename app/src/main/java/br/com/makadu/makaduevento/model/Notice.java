package br.com.makadu.makaduevento.model;

/**
 * Created by lucasschwalbeferreira on 04/04/15.
 */
public class Notice {

    private String title;
    private String description;

    public Notice(){}

    public Notice(String name_notice, String detail) {
        this.description = detail;
        this.title = name_notice;
    }

    public String getName_notice() {
        return title;
    }

    public void setName_notice(String name_notice) {
        this.title = name_notice;
    }

    public String getDetail() {
        return description;
    }

    public void setDetail(String detail) {
        this.description = detail;
    }
}
