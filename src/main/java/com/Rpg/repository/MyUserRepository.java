package com.Rpg.repository;

import com.Rpg.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    MyUser findByLogin(String name);

    Integer countByLogin (String name);

    void deleteByLogin(String name);

}
