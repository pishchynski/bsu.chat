package bsu.chat.model;

import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Date;

import static bsu.chat.util.MsgUtil.DATE_FORMAT;


/**
 * Created by Pavel Pishchynski on 26.04.2015.
 */

public class Msg {
    private String id;
    private String user;
    private String text;
    private String date;

    public Msg(){
        id = "";
        user = "";
        text = "";
    }

    public Msg(String id, String user, String text, String date){
        this.id = id;
        this.user = user;
        this.text = text;
        this.date = date;
    }

    public Msg(String id, String user, String text){
        this.id = id;
        this.user = user;
        this.text = text;
        this.date = setDate();
    }

    public Msg(String user, String text){
        this.id = this.uniqueID();
        this.user = user;
        this.text = text;
        this.date = setDate();
    }

    public Msg(Msg msg){
        this.id = msg.getID();
        this.user = msg.getUsername();
        this.text = msg.getText();
        this.date = msg.getDate();
    }

    private String uniqueID(){
        return UUID.randomUUID().toString();
    }

    private String setDate(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(date);
    }

    public String getDate(){
        return this.date;
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
