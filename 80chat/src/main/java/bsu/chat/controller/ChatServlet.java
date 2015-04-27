package bsu.chat.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import bsu.chat.model.Msg;
import bsu.chat.model.MsgStorage;
import bsu.chat.util.ServletUtil;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import static bsu.chat.util.MsgUtil.*;

/**
 * Created by Pavel Pishchynski on 26.04.2015.
 */

@WebServlet("/80chat")
public class ChatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(ChatServlet.class.getName());

//    @Override
//    public void init() throws ServletException {
//        try {
//            loadHistory();
//        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
//            logger.error(e);
//        }
//    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet");
        String token = request.getParameter(TOKEN);
        logger.info("Token " + token);

        if (token != null && !"".equals(token)) {
            int index = getIndex(token);
            logger.info("Index " + index);
            String messages = formResponse(index);
            response.setContentType(ServletUtil.APPLICATION_JSON);
            PrintWriter out = response.getWriter();
            out.print(messages);
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "'token' parameter needed");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost");
        String data = ServletUtil.getMessageBody(request);
        logger.info(data);
        try {
            JSONObject json = stringToJson(data);
            Msg msg = jsonToMsg(json);
            MsgStorage.addMsg(msg);
            //XMLHistoryUtil.addData(task);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ParseException /*| ParserConfigurationException | SAXException | TransformerException*/ e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPut");
        String data = ServletUtil.getMessageBody(request);
        logger.info(data);
        try {
            JSONObject json = stringToJson(data);
            Msg msg = jsonToMsg(json);
            String id = msg.getID();
            Msg msgToUpdate = MsgStorage.getMsgById(id);
            if (msgToUpdate != null) {
                msgToUpdate.setUsername(msg.getUsername());
                msgToUpdate.setText(msg.getText());
                //XMLHistoryUtil.updateData(taskToUpdate);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Task does not exist");
            }
        } catch (ParseException/* | ParserConfigurationException | SAXException | TransformerException | XPathExpressionException*/ e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @SuppressWarnings("unchecked")
    private String formResponse(int index) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MESSAGES, MsgStorage.getSubMsgListByIndex(index));
        jsonObject.put(TOKEN, getToken(MsgStorage.getSize()));
        return jsonObject.toJSONString();
    }

//    private void loadHistory() throws SAXException, IOException, ParserConfigurationException, TransformerException  {
//        if (XMLHistoryUtil.doesStorageExist()) {
//            TaskStorage.addAll(XMLHistoryUtil.getTasks());
//        } else {
//            XMLHistoryUtil.createStorage();
//            addStubData();
//        }
//    }

//    private void addStubData() throws ParserConfigurationException, TransformerException {
//        Msg[] stubTasks = {
//                new Msg("1", "Create markup", true),
//                new Msg("2", "Learn JavaScript", true),
//                new Msg("3", "Learn Java Servlet Technology", false),
//                new Msg("4", "Write The Chat !", false), };
//        TaskStorage.addAll(stubTasks);
//        for (Task task : stubTasks) {
//            try {
//                XMLHistoryUtil.addData(task);
//            } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
//                logger.error(e);
//            }
//        }
//    }

}
