package com.ccy;

import com.ccy.dao.NewsDAO;
import com.ccy.dao.UserDAO;
import com.ccy.model.News;
import com.ccy.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToutiaoApplication.class)
public class Test01 {

    @Autowired
    private NewsDAO newsDAO;

    @Autowired
    private UserDAO userDAO;
    @Test
    public void test01()
    {

        /*for(int i=0;i<10;i++)
        {
            User user = new User();
            user.setHeadUrl(String.format("https://images.nowcoder.com/head/%sm.png",(int)(Math.random()*1000)));
            user.setName(String.format("user%d",i));
            user.setPassword("qq907223933");
            user.setSalt("123");
            userDAO.addUser(user);
        }*/

        for(int i=0;i<15;i++)
        {
            News news=new News();
            news.setUserId((int)(Math.random()*10)+1);
            news.setTitle(String.format("title{%d}",i));
            news.setLikeCount((int)(Math.random()*10)+1);
            news.setCommentCount((int)(Math.random()*10)+1);
            news.setImage(String.format("https://images.nowcoder.com/head/%sm.png",(int)(Math.random()*1000)));
            news.setCreatedDate(new Date());
            news.setLink("");
            newsDAO.addNews(news);
        }
    }


}
