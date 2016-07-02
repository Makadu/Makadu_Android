package br.com.makadu.makaduevento.model;

/**
 * Created by lucasschwalbeferreira on 11/3/15.
 */
public class User {

    public String user_id;
    public String full_name;
    public String username;
    public String email;
    public String password;
    public boolean data;
    public String result;

    public User(){}

    public User(String full_name, String username,String email, String password) {
        this.full_name = full_name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
