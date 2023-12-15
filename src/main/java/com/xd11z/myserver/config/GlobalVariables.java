package com.xd11z.myserver.config;

import org.springframework.stereotype.Component;

@Component
public class GlobalVariables {
    public static final String photoPath = getPhotoPath();

    /**
     * 获取本项目所在路径，保证可移植性
     * @return
     */
    public static String getPhotoPath(){
        String path = System.getProperty("user.dir").replace("\\","/") + "/src/main/resources/static/";
        return path;
    };
}
