package com.ccy.controller;

import com.ccy.model.HostHolder;
import com.ccy.model.News;
import com.ccy.service.NewsService;
import com.ccy.service.QiniuService;
import com.ccy.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Date;

@Controller
public class NewsController {

   private static final Logger logger= LoggerFactory.getLogger(NewsController.class);

   @Autowired
   public NewsService newsService;
   @Autowired
   public QiniuService qiniuService;
   @Autowired
   public HostHolder hostHolder;

    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name")String imageName, HttpServletResponse response) throws Exception
    {
        try {
            response.setContentType("image/jpeg");
            //StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+imageName)), response.getOutputStream());
            StreamUtils.copy(new URL(ToutiaoUtil.QINIU_TOUTIAO_DOMAIN+imageName).openConnection().getInputStream(),
                    response.getOutputStream());
        }catch (Exception e)
        {
            logger.error("读取图片错误"+imageName+e.getMessage());
        }
    }
    @RequestMapping(path = {"/uploadImage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file")MultipartFile file)
    {
        try {
            //String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if(fileUrl==null)
            {
                return ToutiaoUtil.getJSONString(1,"上传图片失败");
            }
            return ToutiaoUtil.getJSONString(0,fileUrl);
        }catch (Exception e)
        {
            logger.error("上传图片失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"图片上传失败");
        }
   }
   @RequestMapping(path = {"/upload"}, method = {RequestMethod.GET})
   public String uploadPage()
   {
       return "UpLoad";
   }

   @RequestMapping(path = {"/user/addNews/"},method = {RequestMethod.POST})
   @ResponseBody
   public String addNews(@RequestParam("image")String image,@RequestParam("title")String title,
                         @RequestParam("link")String link)
   {
       try {
           News news=new News();
           news.setCreatedDate(new Date());
           news.setTitle(title);
           news.setImage(image);
           news.setLink(link);
           if(hostHolder.getUser()!=null)
           {
               news.setUserId(hostHolder.getUser().getId());
           }else
           {
               news.setUserId(ToutiaoUtil.ANONUMOUS);
           }
           newsService.addNews(news);
           return ToutiaoUtil.getJSONString(0);
       }catch (Exception e)
       {
           logger.error("添加资讯失败"+e.getMessage());
           return ToutiaoUtil.getJSONString(1,"发布失败");
       }
   }
}
