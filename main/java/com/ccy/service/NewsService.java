package com.ccy.service;

import com.ccy.dao.NewsDAO;
import com.ccy.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId,int offset,int limit)
    {
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }
}
