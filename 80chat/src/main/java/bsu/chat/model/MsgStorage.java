package bsu.chat.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Pavel Pishchynski on 26.04.2015.
 */
public class MsgStorage {
    private static List<Msg> storage = new ArrayList<Msg>();

    private MsgStorage(){
    }

    public static List<Msg> getStorage() {
        return storage;
    }

    public static void addMsg(Msg msg){
        storage.add(msg);
    }

    public static void addAll(List<Msg> messages){
        storage.addAll(messages);
    }

    public static int getSize(){
        return storage.size();
    }

    public static Msg getMsgByIndex(int index){
        return storage.get(index);
    }

    public static List<Msg> getSubMsgListByIndex(int index){
        return storage.subList(index, storage.size());
    }

    public static void setMsgById(Msg msg) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getID().equals(msg.getID())) {
                storage.set(i, msg);
            }
        }
    }

    public static Msg getMsgById(String id){
        for(Msg msg: storage){
            if(msg.getID().equals(id)){
                return msg;
            }
        }
        return null;
    }
}
