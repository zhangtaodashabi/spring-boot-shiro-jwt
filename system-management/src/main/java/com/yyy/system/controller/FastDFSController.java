package com.yyy.system.controller;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.yyy.api.http.HttpResult;
import io.swagger.annotations.Api;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @BelongsProject: demo-fastdfs
 * @BelongsPackage: com.example.demofastdfs
 * @Author: Farben
 * @CreateTime: 2019-07-18 13:25
 * @Description: ${Description}
 * 文件上传
 */
@Api(tags ="文件上传")
@RestController
@RequestMapping(value = "/file")
public class FastDFSController {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Value("${uploadFile.host}")
    public String hostAndPort;

//    @Value("${uploadFile.mirrorUrl}")
//    public String mirrorUrl;

    /**
     * 上传图片同时生成默认大小缩略图
     * @param file
     */

    /*public HttpResult uploadImage(@RequestParam("file") MultipartFile file){
        Set<MetaData> metaDataSet = new HashSet<>();

        try {
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), metaDataSet);
            System.out.println("上传结果---" + storePath);
            //拿到元数据
            Set<MetaData> metadata = fastFileStorageClient.getMetadata(storePath.getGroup(), storePath.getPath());
            System.out.println("元数据---" + metadata);
            // 带分组的路径
            String fullPath = storePath.getFullPath();
            System.out.println("带分组的路径---" + fullPath);
            String path = storePath.getPath();
            System.out.println("路径---" + path);
            // 获取缩略图路径
            String thumbImagePath = thumbImageConfig.getThumbImagePath(storePath.getPath());
            System.out.println("缩略图路径---" + thumbImagePath);
            return HttpResult.ok(fullPath);
        } catch (IOException e) {
            return HttpResult.error("上传失败:"+e.getMessage());
        }
    }*/

    /**
     * 上传
     */
    @RequestMapping(value = "/fileSave", method = RequestMethod.POST)
    public HttpResult uploadimgCustom(@RequestParam("file") MultipartFile file) throws Exception {
try {
    FastImageFile fastImageFile = crtFastImageFileOnly(file);

    StorePath storePath = fastFileStorageClient.uploadImage(fastImageFile);
    System.out.println("上传结果---" + storePath);
    String fullPath = storePath.getFullPath();
    HttpResult<Object> objectHttpResult = new HttpResult<>();
    objectHttpResult.setData(hostAndPort + "/" +fullPath);
    objectHttpResult.setMsg("上传成功。");
    objectHttpResult.setCode(200);

    return objectHttpResult;

}catch (IOException e){
            return HttpResult.error("上传失败:"+e.getMessage());
        }


            }

    /**
     * 只上传文件
     *
     * @return
     * @throws Exception
     */
    private FastImageFile crtFastImageFileOnly(MultipartFile file) throws Exception {
        InputStream in = file.getInputStream();
        Set<MetaData> metaDataSet = new HashSet<>();
        metaDataSet.add(new MetaData("Author", "Author"));
        metaDataSet.add(new MetaData("CreateDate", "当前时间"));
        String name = file.getOriginalFilename();
        String fileExtName = FilenameUtils.getExtension(name);
        return new FastImageFile.Builder()
                .withFile(in, file.getSize(), fileExtName)
                .withMetaData(metaDataSet)
                .build();
    }
}
