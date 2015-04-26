package bsu.chat.util;

/**
 * Created by Pavel Pishchynski on 26.04.2015.
 */

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class MsgUtil {
    public static final String APPLICATION_JSON = "application/json";
    public  static final String TOKEN = "token";
    private static final String MESSAGES = "messages";

    private MsgUtil(){
    }

    public static String getMessageBody(HttpServletRequest request) throws IOException{
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
