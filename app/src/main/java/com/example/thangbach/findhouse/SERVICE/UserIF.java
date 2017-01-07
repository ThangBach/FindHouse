package com.example.thangbach.findhouse.SERVICE;

import com.example.thangbach.findhouse.DAO.User;

/**
 * Created by THANG_BACH on 09/22/16.
 */

public interface UserIF {
    public void addUser(User user);
    public void deleteUser(String idUser);
    public User checkUser(String userName);
}
