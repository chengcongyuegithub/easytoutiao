package com.ccy.service;

import com.ccy.dao.UserDAO;
import com.ccy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public User getUser(int id)
    {
        return userDAO.selectById(id);
    }
}
