package bsu.chat.util;

/**
 * Created by Pavel Pishchynski on 26.04.2015.
 */

import bsu.chat.model.Msg;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MsgUtil {
    public static final String TOKEN = "token";
    public static final String MESSAGES = "messages";
    private static final String TN = "TN";
    private static final String EN = "EN";
    private static final String ID = "id";
    private static final String USER = "user";
    private static final String TEXT = "text";

    private MsgUtil(){
    }

    public static String getToken(int index) {
        Integer number = index * 8 + 11;
        return TN + number + EN;
    }

    public static int getIndex(String token) {
        return (Integer.valueOf(token.substring(2, token.length() - 2)) - 11) / 8;
    }

    public static JSONObject stringToJson(String data) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(data.trim());
    }

    public static Msg jsonToMsg(JSONObject json) {
        //Object id = json.get(ID);
        Object user = json.get(USER);
        Object text = json.get(TEXT);

        if (/*id != null && */user != null && text != null) {
            return new Msg(/*(String) id,*/ (String) user, (String) text);
        }
        return null;
    }

}
