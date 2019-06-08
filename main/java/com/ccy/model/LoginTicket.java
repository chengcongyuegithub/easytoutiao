package com.ccy.model;

import lombok.Data;

import java.util.Date;

@Data
public class LoginTicket {
    private int id;
    private int userId;
    private Date expired;//过期时间
    private int status;
    private String ticket;
}
