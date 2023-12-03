package com.xd11z.myserver.myserver.util;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class logger {
    public static void write(String filepath, String content) {
        try (FileWriter fileWriter = new FileWriter(filepath,true)) {
            fileWriter.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"   "+content+'\n');
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }

    public static void write(String content){
        write("./src/main/java/com/xd11z/myserver/util/log.txt",content);
    }
}
