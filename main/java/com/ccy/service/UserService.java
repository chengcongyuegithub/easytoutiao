package com.ccy.service;

import com.ccy.dao.LoginTicketDAO;
import com.ccy.dao.UserDAO;
import com.ccy.model.LoginTicket;
import com.ccy.model.User;
import com.ccy.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {

    private static final Logger logger= LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id)
    {
        return userDAO.selectById(id);
    }

    public Map<String,Object> register(String username, String password)
    {
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isEmpty(username))
        {
            map.put("msgname","用户名不能为空");
        }
        if(StringUtils.isEmpty(password))
        {
            map.put("msgpwd","密码不为空");
        }
        User user=userDAO.selectByName(username);
        if(user!=null)
        {
            map.put("msgname","用户名已经注册");
        }

        user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }

    public Map<String,Object> login(String username,String password)
    {
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isEmpty(username))
        {
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password))
        {
            map.put("msgpwd","密码不能为空");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user==null)
        {
            map.put("msgname","密码不正确");
            return map;
        }
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }


    private String addLoginTicket(int userId)
    {
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);//有效
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }


    public void logout(String ticket)
    {
        loginTicketDAO.updateStatus(ticket,1);
    }
}
