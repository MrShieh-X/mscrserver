package com.mrshiehx.mschatroom.server;

import com.alibaba.fastjson.JSONObject;

import java.io.*;

public class Variables {
    public static JSONObject configs;
    public static final File configsFile=new File("mscrserver_config.json");

    public static JSONObject getConfigs(){
        if(configs==null){
            if(configsFile.exists()) {
                try {
                    JSONObject newj=JSONObject.parseObject(Utils.getString(configsFile));
                    if(newj!=null) {
                        configs = newj;
                    }else{
                        configs=new JSONObject();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                configs=new JSONObject();
            }
        }
        return configs;
    }

    public static int getPort(){
        Integer integer=getConfigs().getInteger("port");
        if(integer==null)return 6553;
        return integer;
    }

    public static String getServerAddress(){
        String string=getConfigs().getString("serverAddress");
        if(Utils.isEmpty(string))return "127.0.0.1";
        return string;
    }

    public static String getDatabaseName(){
        String string=getConfigs().getString("databaseName");
        if(Utils.isEmpty(string))return "mschatroom";
        return string;
    }

    public static String getDatabaseUserName(){
        String string=getConfigs().getString("databaseUserName");
        if(Utils.isEmpty(string))return "root";
        return string;
    }

    public static String getDatabaseUserPassword(){
        String string=getConfigs().getString("databaseUserPassword");
        if(Utils.isEmpty(string))return "mypassword";
        return string;
    }

    public static String getDatabaseTableName(){
        String string=getConfigs().getString("databaseTableName");
        if(Utils.isEmpty(string))return "users";
        return string;
    }

    public static boolean getIsPrint(){
        Boolean aBoolean=getConfigs().getBoolean("isPrint");
        if(aBoolean==null)return false;
        return aBoolean;
    }
}
