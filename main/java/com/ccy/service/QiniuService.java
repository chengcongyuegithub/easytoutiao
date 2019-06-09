package com.ccy.service;

import com.ccy.util.ToutiaoUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@Service
public class QiniuService {

    private static final Logger logger= LoggerFactory.getLogger(QiniuService.class);


    private static final String accessKey = "uyzVohsMDK4fqS4WbvZ26GsP0evvgP8ofbi6Er-1";
    private static final String secretKey = "wK4k36ZEkwbmFtPlbAzfMEvCCwb98Z5yf9kRpidd";
    private static final String bucketname ="ccyimage";
    private static Configuration cfg;
    private static UploadManager uploadManager;

    static {
        cfg = new Configuration(Zone.zone1());
        //...其他参数参考类注释
        uploadManager = new UploadManager(cfg);
    }

    public String saveImage(MultipartFile file) throws IOException
    {
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if(dotPos<0)
        {
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(fileExt))//传入的不是图片类型的文件
        {
            return null;
        }
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucketname);
        DefaultPutRet putRet=null;
        try {
                Response response = uploadManager.put(file.getInputStream(),key,upToken,null, null);
                //解析上传成功的结果
                putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
        }
        return ToutiaoUtil.QINIU_TOUTIAO_DOMAIN+putRet.key;
    }
}
