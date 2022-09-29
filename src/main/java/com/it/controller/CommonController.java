package com.it.controller;

import com.it.utli.SystemJsonResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 * @since  2022-9-22
 * @author  hyj
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    /**
     * 文件上传
     * @return
     */
    @PostMapping("/upload")
    public SystemJsonResponse upload(MultipartFile file){
        //原始文件名
        String filename=file.getOriginalFilename();

        //UUID随机生成名字，防止重复
        String s = UUID.randomUUID().toString();
        String substring = filename.substring(filename.lastIndexOf("."));
        s+=substring;
        //文件转存到指定位置
        try {
            file.transferTo(new File("C:\\Users\\waili\\Desktop\\usual\\Javaopenfile\\ssmheima\\photo\\"+s));
        } catch (IOException e) {
            throw new RuntimeException("文件保持失败");
        }
        return SystemJsonResponse.success(s);
    }

    /**
     * 文件下载
     */
    @GetMapping("/download")
    public  void download(String name,HttpServletResponse response ){
        //输入流读取文件内容
        FileInputStream fileInputStream=null;
        try {
             fileInputStream=new FileInputStream(
                    new File("C:\\Users\\waili\\Desktop\\usual\\Javaopenfile\\ssmheima\\photo\\"+name));

            //输出流写会浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            byte[] b=new byte[1024];
            int len=0;
            while((len = fileInputStream.read(b)) != -1){
                outputStream.write(b,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
