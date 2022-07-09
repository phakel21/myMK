package com.Rpg.service;

import com.Rpg.dto.UserDTO;
import com.Rpg.entity.MyUser;

import java.util.List;

public interface MyUserService {

    MyUser registration(UserDTO userDTO);

    UserDTO getByName(String name);

    List<UserDTO> getAll();

    void deleteByName(String name);

    MyUser get(String name);

}
