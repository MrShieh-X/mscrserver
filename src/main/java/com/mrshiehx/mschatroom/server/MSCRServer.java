package com.mrshiehx.mschatroom.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MSCRServer {
    static int port;
    static Connection connection;
    static Map<IoSession, String> map=new HashMap<>();
    public static void main(String[] args){
        port=Variables.getPort();

        editConnectionsNumber(0,0);

        System.out.println(Strings.message_connecting_to_database);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connection=getConnection(Variables.getServerAddress(),Variables.getDatabaseName(),Variables.getDatabaseUserName(),Variables.getDatabaseUserPassword());
                } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
                if(connection!=null){
                    System.out.println(Strings.message_success_connect_to_database);
                }else{
                    System.out.println(Strings.message_failed_connect_to_database);
                }
            }
        }).start();

        NioSocketAcceptor acceptor;
        try {
            acceptor = new NioSocketAcceptor();
            acceptor.setHandler(new IoHandlerAdapter() {
                @Override
                public void exceptionCaught(IoSession session, Throwable cause) {
                    System.out.println(String.format(Strings.message_have_a_error, cause));
                    cause.printStackTrace();
                }

                @Override
                public void messageReceived(IoSession session, Object message) throws Exception {
                    //System.out.println("R1: "+message.toString());
                    if(Variables.getIsPrint()) {
                        System.out.println(String.format(Strings.message_received,URLDecoder.decode((String) message, "utf-8")));
                    }
                    if (((String) message).startsWith("aoE:")) {
                        map.put(session,((String) message).substring(4));
                    } else{
                        //String currentUserEOAEncrypted=map.get(session);
                        JSONObject jsonObject = JSONObject.parseObject(URLDecoder.decode((String) message));
                        String toUserEncrypted = jsonObject.getString("t");
                        String content = jsonObject.getString("c");
                        String currentUserEOAEncrypted = jsonObject.getString("f");
                        List<String>var=new ArrayList<>(map.values());

                        IoSession session1 = null;
                        for(int i=0;i<var.size();i++){
                            String str=var.get(i);
                            String[]var2=str.split("\\\\");
                            String a=var2[0];
                            String e=var2[1];
                            if(a.equals(toUserEncrypted)||e.equals(toUserEncrypted)){
                                session1=new ArrayList<>(map.keySet()).get(i);
                                break;
                            }
                        }


                        if(session1!=null){
                            //online
                            session1.write(URLEncoder.encode("{\"f\":\""+currentUserEOAEncrypted+"\",\"c\":\""+content+"\"}","UTF-8"));
                        }else{
                            //offline
                            //String eoaEnc=eoasEncrypted.get(indexOf);
                            //String eoaCle=EnDeCryptTextUtils.decrypt(toUserEncrypted);

                            /**new*/
                            String by="account";
                            String messages=getString(Variables.getDatabaseTableName(),"messages",by,toUserEncrypted);
                            if(messages!=null&&messages.length()!=0){
                                /*JSONArray head=JSONArray.parseArray(messages);
                                boolean did=false;
                                for(int i=0;i<head.size();i++){
                                    JSONObject thisAcc=head.getJSONObject(i);
                                    String account=thisAcc.getString("account");
                                    if(account!=null&&account.length()!=0){
                                        if(account.equals(currentUserEOAEncrypted)){
                                            JSONArray messagesArray=thisAcc.getJSONArray("messages");
                                            if(messagesArray!=null){
                                                messagesArray.add(content);
                                                thisAcc.remove("messages");
                                                thisAcc.put("messages",messagesArray);
                                            }else{
                                                JSONArray newMessages=new JSONArray();
                                                newMessages.add(content);
                                                thisAcc.put("messages",newMessages);
                                            }
                                            head.remove(i);
                                            head.add(thisAcc);
                                            setString(Variables.getDatabaseTableName(),"messages",head.toString(),by,toUserEncrypted);
                                            did=true;
                                            break;
                                        }

                                    }
                                }
                                if(!did){
                                    JSONObject thisAcc=new JSONObject();
                                    thisAcc.put("account",currentUserEOAEncrypted);
                                    JSONArray messagesArray=new JSONArray();
                                    messagesArray.add(content);
                                    thisAcc.put("messages",messagesArray);
                                    head.add(thisAcc);
                                }*/



                                //NOT EMPTY, USE OLD
                                JSONObject jsonObject2 = JSON.parseObject(messages);
                                JSONArray array=jsonObject2.getJSONArray(currentUserEOAEncrypted);
                                if(array!=null){
                                    //ARRAY IS EXISTS, USE OLD
                                    array.add(content);
                                    //array.toString();
                                    jsonObject2.replace(currentUserEOAEncrypted,array);
                                }else{
                                    //ARRAY IS NOT EXISTS, CREATE A
                                    JSONArray array1=new JSONArray();
                                    array1.add(content);
                                    jsonObject2.put(currentUserEOAEncrypted,array1);
                                }
                                setString(Variables.getDatabaseTableName(),"messages",jsonObject2.toString(),by,toUserEncrypted);
                            }else{
                                /**new*/
                                /*JSONArray head=new JSONArray();
                                JSONObject thisAcc=new JSONObject();
                                thisAcc.put("account",currentUserEOAEncrypted);
                                JSONArray messagesArray=new JSONArray();
                                messagesArray.add(content);
                                thisAcc.put("messages",messagesArray);
                                head.add(thisAcc);*/


                                //EMPTY, NEW A
                                setString(Variables.getDatabaseTableName(),"messages","{\""+currentUserEOAEncrypted+"\":[\""+content+"\"]}"/*head.toString()*/, by,toUserEncrypted);
                            }
                        }
                    }
                    //editConnectionsNumber(map.keySet().size(),map.values().size());
                }


                @Override
                public void messageSent(IoSession session, Object message) throws Exception {
                    if(Variables.getIsPrint()) {
                        System.out.println(String.format(Strings.message_sent,URLDecoder.decode((String) message,"UTF-8")));
                    }else{
                        System.out.println(Strings.message_success_send);
                    }
                }

                @Override
                public void sessionClosed(IoSession session) throws Exception {
                    map.remove(session);
                    System.out.println(String.format(Strings.message_session_closed,map.get(session),session.toString()));
                    editConnectionsNumber(map.keySet().size(),map.values().size());
                }

                @Override
                public void sessionOpened(IoSession session) throws Exception {
                    map.put(session,"");
                    System.out.println(String.format(Strings.message_session_opened,map.get(session),session.toString()));
                    editConnectionsNumber(map.keySet().size(),map.values().size());
                }

                @Override
                public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
                    System.out.println("sessionIdle");
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
            System.out.println(e + "");
        }
    }

    static void editConnectionsNumber(int n, int n2){
        System.out.println(String.format(Strings.connections_number, n,n2));
    }

    static Connection getConnection(String serverAddress, String databaseName, String userName, String userPassword) throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        return DriverManager.getConnection("jdbc:mysql://"+serverAddress+"/" + databaseName, userName, userPassword);
    }

    static boolean setString(String dbTableName, String needToSet, String needToSetContent, String by, String byContent) throws SQLException {
        if (connection == null) {
            System.out.println("setString:connection is null");
            return false;
        } else {
            Statement stmt = connection.createStatement();
            String sql = "update " + dbTableName + " set " + needToSet + "='" + needToSetContent + "' where " + by + "='" + byContent + "'";
            return stmt.executeUpdate(sql)!=0;
        }
    }


    static String getString(String dbTableName, String needToGet, String by, String byContent) throws SQLException {
        if (connection == null) {
            System.out.println("getString:connection is null");
        } else {
            ResultSet set = null;
            PreparedStatement prepar = connection.prepareStatement("select * from " + dbTableName + " where " + by + "='" + byContent + "'");
            set = prepar.executeQuery();
            while (set.next()) {
                return set.getString(needToGet);
            }
        }
        return null;
    }

    public static boolean isEmail(String string) {
        return string.matches("^[a-zA-Z0-9.+_-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9-]{2,24}$");
    }
}
