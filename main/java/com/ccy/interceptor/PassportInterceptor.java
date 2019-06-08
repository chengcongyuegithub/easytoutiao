package com.ccy.interceptor;

import com.ccy.dao.LoginTicketDAO;
import com.ccy.dao.UserDAO;
import com.ccy.model.HostHolder;
import com.ccy.model.LoginTicket;
import com.ccy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
@Component
public class PassportInterceptor implements HandlerInterceptor{

    @Autowired
    private LoginTicketDAO loginTicketDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket=null;
        //这个if获取到浏览器中的cookie,也就是那个令牌
        if(request.getCookies()!=null)
        {
            for(Cookie cookie:request.getCookies())
            {
                if(cookie.getName().equals("ticket"))
                {
                    ticket=cookie.getValue();
                    break;
                }
            }
        }
        if(ticket!=null)
        {
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            //是否过期,是否过时
            if(loginTicket==null||loginTicket.getExpired().before(new Date())||
                    loginTicket.getStatus()!=0)
            {
                return true;
            }
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
         if(modelAndView!=null&&hostHolder.getUser()!=null)
         {
             modelAndView.addObject("user",hostHolder.getUser());
         }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
