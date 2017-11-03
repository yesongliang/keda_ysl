package com.kedacom.ysl.service;

import com.kedacom.ysl.model.User;

public interface TestService {

    User login(String userName);
    
    User getUser(int id);
    
    void upPhoto(byte[] photo,String userName);
}
