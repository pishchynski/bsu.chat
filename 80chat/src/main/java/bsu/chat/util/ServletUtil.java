package bsu.chat.util;

import bsu.chat.model.MsgStorage;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

import static bsu.chat.util.MsgUtil.getToken;

/**
 * Created by Pavel Pishchynski on 26.04.2015.
 */
public class ServletUtil {
    public static final String APPLICATION_JSON = "application/json";
    private static final String MESSAGES = "messages";
    public static final String TOKEN = "token";

    public static String getMessageBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public static String getServerResponse(int index) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MESSAGES, MsgStorage.getSubMsgListByIndex(index));
        jsonObject.put(TOKEN, getToken(MsgStorage.getSize()));
        return jsonObject.toJSONString();
    }
}
