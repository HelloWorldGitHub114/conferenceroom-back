package com.xd11z.myserver.util;

import com.xd11z.myserver.config.GlobalVariables;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PhotoUtil {
    public static String getPath(MultipartFile file) throws IOException {
        // 定义存储图片的地址
        String folder = GlobalVariables.photoPath;
        // 文件夹
        File imgFolder = new File(folder);
        // 获取文件名
        String fname = file.getOriginalFilename();
        // 获取文件后缀
        String ext = "." + fname.substring(fname.lastIndexOf(".")+1);
        // 获取时间字符串
        String dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        // 生成新的文件名
        String newFileName = dateTimeFormatter + UUID.randomUUID().toString().replaceAll("-","") + ext;
        // 文件在本机的全路径
        File filePath = new File(imgFolder, newFileName);
        if (!filePath.getParentFile().exists()){
            filePath.getParentFile().mkdirs();
        }
        try{
            file.transferTo(filePath);
            // 返回文件路径
            String ans = "http://localhost:8080/photo/" + filePath.getName();
            return ans;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean delete(String path){
        String name = path.substring(path.lastIndexOf('/') + 1);//获取文件名，从而转换为本地路径
        File file = new File(GlobalVariables.getPhotoPath()+name);
        boolean deleted = file.delete();
        return deleted;
    }

}
