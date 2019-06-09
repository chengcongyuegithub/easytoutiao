package com.ccy.service;

import com.ccy.dao.NewsDAO;
import com.ccy.model.News;
import com.ccy.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId,int offset,int limit)
    {
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }

    public String saveImage(MultipartFile file) throws IOException
    {
        System.out.println("!!!!"+file.getOriginalFilename());
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
        String fileName=UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
        Files.copy(file.getInputStream(),
                new File(ToutiaoUtil.IMAGE_DIR+fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+fileName;
    }
    public void addNews(News news)
    {
        newsDAO.addNews(news);
    }
}
