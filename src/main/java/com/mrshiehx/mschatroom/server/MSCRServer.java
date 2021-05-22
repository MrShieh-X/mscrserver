package com.mrshiehx.mschatroom.server;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MSCRServer {
    static int port;
    static Connection connection;
    private final static Map<String, IoSession> map = new HashMap<>();

    public static void main(String[] args) {
        port = Variables.getPort();

        editConnectionsNumber(0);

        connectDatabase();

        NioSocketAcceptor acceptor;
        try {
            acceptor = new NioSocketAcceptor();
            acceptor.setHandler(new IoHandlerAdapter() {
                @Override
                public void exceptionCaught(IoSession session, Throwable cause) {
                    System.out.println(getTimeString() + String.format(Strings.message_have_a_error, cause));
                    cause.printStackTrace();
                    clear();
                }

                @Override
                public void messageReceived(IoSession session, Object message) throws Exception {
                    //System.out.println(getTimeString()+"R1: "+message.toString());

                    new Thread(() -> {
                        try {
                            String aoeE = "aoe=";
                            if (((String) message).startsWith(aoeE)) {
                                //if (Variables.getIsPrint()) {
                                //    System.out.println(getTimeString() + String.format(Strings.message_received, URLDecoder.decode((String) message, "utf-8")));
                                //}
                                String c = ((String) message).substring(aoeE.length());
                                if (!c.equals("aoe=" + "/") && c.contains("/")) {
                                    String[] strings = c.split("/");
                                    boolean b = false;
                                    try {
                                        b = tryLoginWithoutPassword(Variables.getDatabaseTableName(), "account", strings[0]) || tryLoginWithoutPassword(Variables.getDatabaseTableName(), "email", strings[1]);
                                    } catch (Exception ignored) {
                                    }
                                    if (b) {
                                        map.put(c, session);
                                        System.out.println(getTimeString() + String.format(Strings.message_session_opened, ((String) message).substring(aoeE.length()), session.toString()));
                                        editConnectionsNumber(map.size());
                                    }
                                }
                            } else {
                                if (new ArrayList<>(map.values()).contains(session)) {
                                    if (Variables.getIsPrint()) {
                                        System.out.println(getTimeString() + String.format(Strings.message_received, URLDecoder.decode((String) message, "utf-8")));
                                    }
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = JSONObject.parseObject(URLDecoder.decode((String) message));
                                    } catch (Throwable ignore) {
                                    }
                                    if (jsonObject != null) {
                                        boolean toServer = jsonObject.getBoolean("toServer") != null ? jsonObject.getBoolean("toServer") : false;
                                        String content = jsonObject.getString("content");
                                        String currentUserEOAEncrypted = jsonObject.getString("from");

                                        if (!toServer) {
                                            String toUserEncrypted = jsonObject.getString("to");
                                            Integer type = jsonObject.getInteger("type");
                                            if (type == null) {
                                                type = 0;
                                            }
                                            List<String> var = new ArrayList<>(map.keySet());

                                            if (content != null && content.length() != 0 && toUserEncrypted != null && toUserEncrypted.length() != 0 && currentUserEOAEncrypted != null && currentUserEOAEncrypted.length() != 0) {
                                                IoSession session1 = null;
                                                for (int i = 0; i < var.size(); i++) {
                                                    String str = var.get(i);
                                                    String[] var2 = str.split("/");
                                                    String a = var2[0];
                                                    String e = var2[1];
                                                    if (a.equals(toUserEncrypted) || e.equals(toUserEncrypted)) {
                                                        session1 = new ArrayList<>(map.values()).get(i);
                                                        break;
                                                    }
                                                }


                                                if (session1 != null&&session.isConnected()) {
                                                    //online
                                                    JSONObject jsonObject1 = new JSONObject();
                                                    jsonObject1.put("from", currentUserEOAEncrypted);
                                                    jsonObject1.put("type", type);
                                                    /**code for send types*/
                                                    if (type == MessageTypes.FILE.code/**file*/) {
                                                        String name = jsonObject.getString("fileName");
                                                        if (name == null) name = "file";
                                                        jsonObject1.put("fileName", name);
                                                        String aa = jsonObject.getString("millis");
                                                        jsonObject1.put("content", aa);
                                                        File file = new File("files", String.valueOf(aa));
                                                        if (!file.getParentFile().exists())
                                                            file.getParentFile().mkdirs();
                                                        if (file.exists()) file.delete();
                                                        file.createNewFile();
                                                        try {
                                                            Utils.hexWrite(content, file);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                        jsonObject1.put("fileSize", file.length());
                                                    } else {
                                                        jsonObject1.put("content", content);
                                                    }
                                                    session1.write(URLEncoder.encode(/*"{\"f\":\""+currentUserEOAEncrypted+"\",\"c\":\""+content+"\"}"*/jsonObject1.toString(), "UTF-8"));
                                                } else {
                                                    //offline


                                                    //String eoaEnc=eoasEncrypted.get(indexOf);
                                                    //String eoaCle=EnDeCryptTextUtils.decrypt(toUserEncrypted);

                                                    /**new*/
                                                    String by = "account";
                                                    String messages = getString(Variables.getDatabaseTableName(), "messages", by, toUserEncrypted);
                                                    if (messages != null && messages.length() != 0) {
                                                        JSONArray head;
                                                        try {
                                                            head = JSONArray.parseArray(messages);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            head = new JSONArray();
                                                        }
                                                        boolean did = false;
                                                        for (int i = 0; i < head.size(); i++) {
                                                            JSONObject thisAcc = head.getJSONObject(i);
                                                            String account = null;
                                                            try {
                                                                account = thisAcc.getString("account");
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            if (account != null && account.length() != 0) {
                                                                if (account.equals(currentUserEOAEncrypted)) {
                                                                    JSONArray messagesArray = thisAcc.getJSONArray("messages");
                                                                    if (messagesArray != null) {
                                                                        JSONObject messageObj = new JSONObject();

                                                                        /**code for send types*/
                                                                        if (type == MessageTypes.FILE.code/**file*/) {
                                                                            String name = jsonObject.getString("fileName");
                                                                            if (name == null) name = "file";
                                                                            messageObj.put("fileName", name);
                                                                            String aa = jsonObject.getString("millis");
                                                                            messageObj.put("text", aa);
                                                                            File file = new File("files", String.valueOf(aa));
                                                                            if (!file.getParentFile().exists())
                                                                                file.getParentFile().mkdirs();
                                                                            if (file.exists()) file.delete();
                                                                            file.createNewFile();
                                                                            try {
                                                                                Utils.hexWrite(content, file);
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            messageObj.put("fileSize", file.length());

                                                                        } else {
                                                                            messageObj.put("text", content);
                                                                        }

                                                                        messageObj.put("type", type);
                                                                        messageObj.put("time", System.currentTimeMillis());
                                                                        messagesArray.add(messageObj);
                                                                        //thisAcc.remove("messages");
                                                                        thisAcc.put("messages", messagesArray);
                                                                    } else {
                                                                        JSONArray newMessages = new JSONArray();
                                                                        //newMessages.add(content);

                                                                        JSONObject messageObj = new JSONObject();

                                                                        /**code for send types*/
                                                                        if (type == MessageTypes.FILE.code/**file*/) {
                                                                            String name = jsonObject.getString("fileName");
                                                                            if (name == null) name = "file";
                                                                            messageObj.put("fileName", name);
                                                                            String aa = jsonObject.getString("millis");
                                                                            messageObj.put("text", aa);
                                                                            File file = new File("files", String.valueOf(aa));
                                                                            if (!file.getParentFile().exists())
                                                                                file.getParentFile().mkdirs();
                                                                            if (file.exists()) file.delete();
                                                                            file.createNewFile();
                                                                            try {
                                                                                Utils.hexWrite(content, file);
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            messageObj.put("fileSize", file.length());

                                                                        } else {
                                                                            messageObj.put("text", content);
                                                                        }


                                                                        messageObj.put("type", type);
                                                                        messageObj.put("time", System.currentTimeMillis());
                                                                        newMessages.add(messageObj);


                                                                        thisAcc.put("messages", newMessages);
                                                                    }
                                                                    head.remove(i);
                                                                    head.add(thisAcc);
                                                                    setString(Variables.getDatabaseTableName(), "messages", head.toString(), by, toUserEncrypted);
                                                                    did = true;
                                                                    break;
                                                                }

                                                            }
                                                        }
                                                        if (!did) {
                                                            JSONObject thisAcc = new JSONObject();
                                                            thisAcc.put("account", currentUserEOAEncrypted);
                                                            JSONArray messagesArray = new JSONArray();
                                                            JSONObject messageObj = new JSONObject();

                                                            /**code for send types*/
                                                            if (type == MessageTypes.FILE.code/**file*/) {
                                                                String name = jsonObject.getString("fileName");
                                                                if (name == null) name = "file";
                                                                messageObj.put("fileName", name);
                                                                String aa = jsonObject.getString("millis");
                                                                messageObj.put("text", aa);
                                                                File file = new File("files", String.valueOf(aa));
                                                                if (!file.getParentFile().exists())
                                                                    file.getParentFile().mkdirs();
                                                                if (file.exists()) file.delete();
                                                                file.createNewFile();
                                                                try {
                                                                    Utils.hexWrite(content, file);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                messageObj.put("fileSize", file.length());

                                                            } else {
                                                                messageObj.put("text", content);
                                                            }


                                                            messageObj.put("type", type);
                                                            messageObj.put("time", System.currentTimeMillis());
                                                            messagesArray.add(messageObj);
                                                            thisAcc.put("messages", messagesArray);
                                                            head.add(thisAcc);
                                                        }
                                                        //NOT EMPTY, USE OLD
                                /*JSONObject jsonObject2 = JSON.parseObject(messages);
                                JSONArray array=jsonObject2.getJSONArray(currentUserEOAEncrypted);
                                if(array!=null){
                                    //ARRAY IS EXISTS, USE OLD
                                    array.add(content);
                                    //array.toString();
                                    jsonObject2.replace(currentUserEOAEncrypted,array);
                                }else{
                                    //ARRAY IS NOT EXISTS, CREATE A
                                    JSONArray array1t=new JSONArray();
                                    array1.add(content);
                                    jsonObject2.put(currentUserEOAEncrypted,array1);
                                }*/
                                                        setString(Variables.getDatabaseTableName(), "messages", head.toString(), by, toUserEncrypted);
                                                    } else {
                                                        /**new*/
                                                        JSONArray head = new JSONArray();
                                                        JSONObject thisAcc = new JSONObject();
                                                        JSONArray messagesArray = new JSONArray();
                                                        JSONObject messageObj = new JSONObject();

                                                        /**code for send types*/
                                                        if (type == MessageTypes.FILE.code/**file*/) {
                                                            String name = jsonObject.getString("fileName");
                                                            if (name == null) name = "file";
                                                            messageObj.put("fileName", name);
                                                            String aa = jsonObject.getString("millis");
                                                            messageObj.put("text", aa);
                                                            File file = new File("files", String.valueOf(aa));
                                                            if (!file.getParentFile().exists())
                                                                file.getParentFile().mkdirs();
                                                            if (file.exists()) file.delete();
                                                            file.createNewFile();
                                                            try {
                                                                Utils.hexWrite(content, file);
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                            messageObj.put("fileSize", file.length());
                                                        } else {
                                                            messageObj.put("text", content);
                                                        }


                                                        messageObj.put("type", type);
                                                        messageObj.put("time", System.currentTimeMillis());
                                                        messagesArray.add(messageObj);
                                                        thisAcc.put("messages", messagesArray);
                                                        thisAcc.put("account", currentUserEOAEncrypted);
                                                        head.add(thisAcc);


                                                        //EMPTY, NEW A
                                                        setString(Variables.getDatabaseTableName(), "messages",/*"{\""+currentUserEOAEncrypted+"\":[\""+content+"\"]}"*/head.toString(), by, toUserEncrypted);
                                                    }
                                                }
                                            }
                                        } else {
                                            if (content != null && content.length() != 0 && content.contains(" ")) {
                                                String[] command = content.split(" ");
                                                String operate = command[0];
                                                Long id = jsonObject.getLong("id");
                                                if (id != null) {
                                                    switch (operate) {
                                                        case "downloadFile":
                                                            String arg = command[1];
                                                            File file = new File("files", arg);
                                                            JSONObject jsonObject1 = new JSONObject();
                                                            jsonObject1.put("id", id);
                                                            jsonObject1.put("toClient", true);
                                                            if (file.exists()) {
                                                                String str = "";
                                                                try {
                                                                    str = Utils.bytesToString(Utils.toByteArray(file));
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                jsonObject1.put("content", str);
                                                                file.delete();
                                                            }
                                                            //try{ Thread.sleep(2000);}catch(Exception e){e.printStackTrace();}

                                                            session.write(URLEncoder.encode(jsonObject1.toString()));
                                                            break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            //editConnectionsNumber(map.keySet().size(),map.values().size());
                            clear();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }


                @Override
                public void messageSent(IoSession session, Object message) throws Exception {
                    if (Variables.getIsPrint()) {
                        System.out.println(getTimeString() + String.format(Strings.message_sent, URLDecoder.decode((String) message, "UTF-8")));
                    } else {
                        System.out.println(getTimeString() + Strings.message_success_send);
                    }
                    clear();
                }

                @Override
                public void sessionClosed(IoSession session) throws Exception {
                    List<String> strings = new ArrayList<>(map.keySet());

                    List<IoSession> sessions = new ArrayList<>(map.values());

                    int index = sessions.indexOf(session);
                    if (index != -1) {
                        String str = strings.get(index);

                        System.out.println(getTimeString() + String.format(Strings.message_session_closed, str, session.toString()));
                        map.remove(str);
                    }
                    editConnectionsNumber(map.size());
                    clear();
                }

                @Override
                public void sessionOpened(IoSession session) throws Exception {
                    /**map.put(session, "");
                     System.out.println(getTimeString()+String.format(Strings.message_session_opened, map.get(session), session.toString()));
                     editConnectionsNumber(map.keySet().size(), map.values().size());*/
                    clear();
                }

                @Override
                public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
                    clear();
                    System.out.println(getTimeString() + "sessionIdle: " + status.toString());
                }
            });

            TextLineCodecFactory lineCodec = new TextLineCodecFactory(Charset.forName("UTF-8"));
            lineCodec.setDecoderMaxLineLength(1024 * 10240); //10M
            lineCodec.setEncoderMaxLineLength(1024 * 10240); //10M
            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(lineCodec));

            acceptor.setReuseAddress(true);
            acceptor.bind(new InetSocketAddress(port));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(getTimeString() + e.toString());
        }
    }

    static void editConnectionsNumber(int n) {
        System.out.println(getTimeString() + String.format(Strings.connections_number, n));
    }

    static Connection getConnection(String serverAddress, String databaseName, String userName, String userPassword) throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        return DriverManager.getConnection("jdbc:mysql://" + serverAddress + "/" + databaseName, userName, userPassword);
    }

    static boolean setString(String dbTableName, String needToSet, String needToSetContent, String by, String byContent) throws SQLException {
        if (connection == null) {
            connectDatabase();
        } else {
            boolean closed = true;
            try {
                closed = connection.isClosed();
            } catch (Exception ignore) {
            }
            if (closed) connectDatabase();
        }
        Statement stmt = connection.createStatement();
        String sql = "update " + dbTableName + " set " + needToSet + "='" + needToSetContent + "' where " + by + "='" + byContent + "'";
        return stmt.executeUpdate(sql) != 0;

    }

    static void clear() {
        List<IoSession> sessions = new ArrayList<>(map.values());
        List<String> eoas = new ArrayList<>(map.keySet());
        for (int i = 0; i < map.size(); i++) {
            IoSession session1 = sessions.get(i);
            String eoa = eoas.get(i);
            if (!session1.isConnected()) {
                map.remove(eoa);
            }
        }
    }


    static String getString(String dbTableName, String needToGet, String by, String byContent) throws SQLException {
        if (connection == null) {
            connectDatabase();
        } else {
            boolean closed = true;
            try {
                closed = connection.isClosed();
            } catch (Exception ignore) {
            }
            if (closed) connectDatabase();
        }
        PreparedStatement prepar = connection.prepareStatement("select * from " + dbTableName + " where " + by + "='" + byContent + "'");
        ResultSet set = prepar.executeQuery();
        while (set.next()) {
            return set.getString(needToGet);
        }
        return null;
    }

    public static boolean isEmail(String string) {
        return string.matches("^[a-zA-Z0-9.+_-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9-]{2,24}$");
    }

    private static void connectDatabase() {
        System.out.println(getTimeString() + Strings.message_connecting_to_database);

        try {
            connection = getConnection(Variables.getServerAddress(), Variables.getDatabaseName(), Variables.getDatabaseUserName(), Variables.getDatabaseUserPassword());
        } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        if (connection != null) {
            System.out.println(getTimeString() + Strings.message_success_connect_to_database);
        } else {
            System.out.println(getTimeString() + Strings.message_failed_connect_to_database);
        }
    }


    public static boolean tryLoginWithoutPassword(String dbTableName, String loginMethod, String accountOrEmail) throws SQLException {
        if (connection == null) {
            connectDatabase();
        } else {
            boolean closed = true;
            try {
                closed = connection.isClosed();
            } catch (Exception ignore) {
            }
            if (closed) connectDatabase();
        }

        String sql = "select * from " + dbTableName + " where " + loginMethod + "=?";
        PreparedStatement pres = connection.prepareStatement(sql);
        pres.setString(1, accountOrEmail);
        ResultSet res = pres.executeQuery();
        return res.next();
    }

    private static String getTimeString() {
        return new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date(System.currentTimeMillis()));
    }
}
