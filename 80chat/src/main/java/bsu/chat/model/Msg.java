package bsu.chat.model;

import java.util.UUID;

/**
 * Created by Pavel Pishchynski on 26.04.2015.
 */

public class Msg {
    private String id;
    private String user;
    private String text;

    public Msg(){
        id = "";
        user = "";
        text = "";
    }

    public Msg(String id, String user, String text){
        this.id = id;
        this.user = user;
        this.text = text;
    }

    public Msg(String user, String text){
        this.id = this.uniqueID();
        this.user = user;
        this.text = text;
    }

    private String uniqueID(){
        return UUID.randomUUID().toString();
    }

    public String getID(){
        return this.id;
    }

    public boolean setID(String id){
        this.id = id;
        return true;
    }

    public String getUsername(){
        return this.user;
    }

    public boolean setUsername(String user){
        this.user = user;
        return true;
    }

    public String getText(){
        return this.text;
    }

    public String toString() {
        return "{\"id\":\"" + this.id + "\",\"user\":\"" + this.user + "\",\"text\":\"" + this.text + "\" }";
    }

    public boolean setText(String text){
        this.text = text;
        return true;
    }
}
