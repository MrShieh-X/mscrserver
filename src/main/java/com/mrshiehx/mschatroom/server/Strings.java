package com.mrshiehx.mschatroom.server;

import java.util.Locale;

public class Strings {
        public static String app_title;
        public static String send_text;
        public static String message_connected_message;
        public static String message_connect_closed;
        public static String message_have_a_error;
        public static String message_success_send;
        public static String message_presend_content;
        public static String message_input_port;
        public static String connections_number;
        public static String message_connecting_to_database;
        public static String message_success_connect_to_database;
        public static String message_failed_connect_to_database;
        public static String message_sent;
        public static String message_received;
        public static String message_session_closed;
        public static String message_session_opened;
        static {
            if(Locale.getDefault().getLanguage().equals("zh")){
                app_title="MSCR\u670d\u52a1\u5668";
                send_text="\u53d1\u9001";
                message_connected_message="\u8fde\u63a5\u6210\u529f";
                message_connect_closed="\u8fde\u63a5\u5173\u95ed";
                message_have_a_error="\u51fa\u73b0\u9519\u8bef\uff1a%s";
                message_success_send="\u53d1\u9001\u6210\u529f";
                message_presend_content="\u5f85\u53d1\u9001\u5185\u5bb9";
                message_input_port="\u8bf7\u8f93\u5165\u4e00\u4e2a\u7aef\u53e3\u53f7\uff08\u6574\u6570\uff0c\u9ed8\u8ba46553\uff09\uff1a";
                connections_number="\u8fde\u63a5\u6570\uff1a%1$s/%2$s";
                message_connecting_to_database="\u6b63\u5728\u8fde\u63a5MySQL\u6570\u636e\u5e93...";
                message_success_connect_to_database="\u8fde\u63a5MySQL\u6570\u636e\u5e93\u6210\u529f";
                message_failed_connect_to_database="\u8fde\u63a5MySQL\u6570\u636e\u5e93\u5931\u8d25";
                message_sent="\u53d1\u9001\uff1a%s";
                message_received="\u63a5\u6536\uff1a%s";
                message_session_closed="\u4f1a\u8bdd\u5173\u95ed\uff1a%1$s\uff08%2$s\uff09";
                message_session_opened="\u4f1a\u8bdd\u6253\u5f00\uff1a%1$s\uff08%2$s\uff09";
            }else{
                app_title="MSCRServer";
                message_connected_message="Connected";
                message_connect_closed="Connection is closed";
                message_have_a_error="An error occurred: %s";
                message_success_send="Successfully to send";
                message_presend_content="Content to be sent";
                message_input_port="Please input a port number (integer, default is 6553): ";
                connections_number="Number of connections: %1$s/%2$s";
                message_connecting_to_database="Connecting to the MySQL database...";
                message_success_connect_to_database="Successfully to connect to the MySQL database";
                message_failed_connect_to_database="Failed to connect to the MySQL database";
                message_sent="Sent: %s";
                message_received="Received: %s";
                message_session_closed="Session is closed: %1$s (%2$s)";
                message_session_opened="Session is opened: %1$s (%2$s)";
            }
        }

}
