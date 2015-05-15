package br.com.makadu.makaduevento.model;

/**
 * Created by lucasschwalbeferreira on 04/04/15.
 */
public class Notice {

    private String name_notice;
    private String detail;

    public Notice(){}

    public Notice(String name_notice, String detail) {
        this.detail = detail;
        this.name_notice = name_notice;
    }

    public String getName_notice() {
        return name_notice;
    }

    public void setName_notice(String name_notice) {
        this.name_notice = name_notice;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
