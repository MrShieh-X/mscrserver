package com.mrshiehx.mschatroom.server.virtual_terminal;

import java.io.File;

public class SystemDirectories {
    private SystemDirectories(){}
    public static final File USERS=new File("/users");
    public static final File SYSTEM=new File("/system");
    public static final File SYSTEM_CLASSES=new File(SYSTEM,"classes");
}
