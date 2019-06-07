package com.ccy.controller;

import com.ccy.model.News;
import com.ccy.model.ViewObject;
import com.ccy.service.NewsService;
import com.ccy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;

    public List<ViewObject> getNews(int userId,int offset,int limit)
    {
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);
        List<ViewObject> vos=new ArrayList<>();
        for(int i=0;i<newsList.size();i++)
        {
            News news=newsList.get(i);
            ViewObject vo=new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));
            if(i==0||!news.getCreatedDate().toString().substring(0,10).equals(newsList.get(i-1).getCreatedDate().toString().substring(0,10)))
            {
                vo.set("ifprint",true);
            }else
            {
                vo.set("ifprint",false);
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/","/index"},method = {RequestMethod.GET,RequestMethod.POST})
    public String index(Model model)
    {
        model.addAttribute("vos",getNews(0,0,10));
        return "home";
    }

    @RequestMapping(path = {"/usr/{userId}/"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId")int userId)
    {
        model.addAttribute("vos",getNews(userId,0,10));
        return "home";
    }

    public static void main(String[] args) {
        String value="2019-06-05 15:27:14";
        System.out.println();
    }
}
