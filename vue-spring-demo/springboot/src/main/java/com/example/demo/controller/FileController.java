package com.example.demo.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.demo.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/files")
@CrossOrigin
public class FileController {
    @Value("${server.port}")
    private String port;

    private static final String ip="http://localhost";


    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) throws IOException {


        String ProjectPath = System.getProperty("user.dir");//根项目所在的文件夹的路径，也就是 D:\Project\AforDemo\SpringVueDemo\vue-spring-demo
        String flag = IdUtil.fastSimpleUUID();//使用uuid标识符
        String originalFilename = file.getOriginalFilename();//获取传进来的文件名

        String filePath = ProjectPath + "/springboot/src/main/resources/files/"+flag+'_'+originalFilename;//完整的文件路径

        FileUtil.writeBytes(file.getBytes(),filePath);//写入文件的字节流
        return Result.success(ip+":"+port+"/files/"+flag);
    }

    /**
     * 富文本传递接口
     * @param file
     * @return
     * @throws IOException
     */

    @PostMapping("/editor/upload")
    public JSON editorUpload(MultipartFile file) throws IOException {


        String ProjectPath = System.getProperty("user.dir");//根项目所在的文件夹的路径，也就是 D:\Project\AforDemo\SpringVueDemo\vue-spring-demo
        String flag = IdUtil.fastSimpleUUID();//使用uuid标识符
        String originalFilename = file.getOriginalFilename();//获取传进来的文件名

        String filePath = ProjectPath + "/springboot/src/main/resources/files/"+flag+'_'+originalFilename;//完整的文件路径

        FileUtil.writeBytes(file.getBytes(),filePath);//写入文件的字节流

        String url=ip+":"+port+"/files/"+flag;
        JSONObject json = new JSONObject();
        json.set("errno",0);

        JSONArray arr = new JSONArray();
        json.set("data",arr);

        JSONObject data = new JSONObject();
        data.set("url",url);

        arr.add(data);

        return json;
    }


    @GetMapping("/{flag}")
    public void getFiles(@PathVariable String flag, HttpServletResponse response){


        //目的：利用输入进来的flag 过滤 file目录下的所有文件
        String basePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/";//file目录
        List<String> fileNames = FileUtil.listFileNames(basePath);//获取file目录下的所有文件

        //fileNames.stream().filter   开启fileNames数组中的filter
        //name->name.contains      name=自定义的变量，相当于v-for中的item
        //findAny()只返回一个符合要求的数据，orElse就是啥也没找到，返回的数据
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse(null);//找到与flag相关的所有文件名

        try{
            if(StrUtil.isNotEmpty(fileName)){
                // response.addHeader("Content-Disposition"相当于 使用Content-Disposition来处理数据
                //"attachment;相当于激活了浏览器的下载框
                //URLEncoder.encode将文件名中的!@#$%^之类的符号转义（不包括下划线），后面的UTF-8就是说用UTF-8再次编译fileName
                response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8"));
                //传输的内容是以二进制流的形式传输
                response.setContentType("application/octet-stream");

                OutputStream os =response.getOutputStream();//通过输出返回文件
                byte[] bytes = FileUtil.readBytes(basePath + fileName);//读取文件字节流，相当上传 文件的所在地址
                os.write(bytes);//在输出流中写入读取到的文件字节流
                os.flush();//flush()方法将输入流和输出流中的缓冲进行刷新，    使缓冲区中的元素即时做输入和输出，而不必等缓冲区满
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }

    }
}
