package com.ccy;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class Test02 {

    public static void main(String[] args) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone1());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "uyzVohsMDK4fqS4WbvZ26GsP0evvgP8ofbi6Er-1";
        String secretKey = "wK4k36ZEkwbmFtPlbAzfMEvCCwb98Z5yf9kRpidd";
        String bucket = "ccyimage";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "/home/chengcongyue/图片/2019-05-08 18-48-56 的屏幕截图.png";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }


    }
}
