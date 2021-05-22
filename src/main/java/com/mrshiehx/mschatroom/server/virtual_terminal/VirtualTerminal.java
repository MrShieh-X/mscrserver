package com.mrshiehx.mschatroom.server.virtual_terminal;

import com.mrshiehx.mschatroom.server.virtual_terminal.utils.CommandStringUtils;
import com.mrshiehx.mschatroom.server.virtual_terminal.utils.DeviceUtils;

import java.io.File;
import java.util.Scanner;

public class VirtualTerminal {
    private static File currentDir=new File("/users/mrshiehx/home");
    private static User currentUser=new User("mrshiehx");
    private static boolean echoOpen=true;
    public static void main(String[]args){
        while(true){
            if(echoOpen) {
                System.out.print(currentUser.userName);
                System.out.print("@");
                System.out.print(DeviceUtils.getDeviceName());
                System.out.print(":");
                System.out.print(currentDir.getAbsolutePath());
                System.out.print("$ ");
            }
            String command;
            Scanner scanner=new Scanner(System.in);
            command = CommandStringUtils.format(scanner.nextLine());

            if(command.equals("echo off")){
                echoOpen=false;
            }else if(command.equals("echo on")){
                echoOpen=true;
            }else{
                String[]arguments;
                if(command.contains(" ")){
                    arguments=command.split(" ");
                }else{
                    arguments=new String[]{command};
                }
                CommandExecutor.execute(arguments);
            }
        }
    }
}
