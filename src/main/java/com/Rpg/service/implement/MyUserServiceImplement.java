package com.Rpg.service.implement;

import com.Rpg.dto.UserDTO;
import com.Rpg.entity.Role;
import com.Rpg.entity.MyUser;
import com.Rpg.repository.MyUserRepository;
import com.Rpg.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
public class MyUserServiceImplement implements MyUserService, UserDetailsService {

    private MyUserRepository myUserRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public MyUserServiceImplement(MyUserRepository myUserRepository, PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private MyUser map(UserDTO userDTO) {
        MyUser MyUser = new MyUser();
        MyUser.setLogin(userDTO.getLogin());
        return MyUser;
    }

    private UserDTO map(MyUser MyUser) {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(MyUser.getLogin());
        return userDTO;
    }

    private List<UserDTO> map(List<MyUser> MyUsers) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (MyUser MyUser : MyUsers) {
            userDTOS.add(map(MyUser));
        }
        return userDTOS;
    }

    @Override
    public MyUser registration(UserDTO userDTO) {
        if (!registrationValidation(userDTO)) ;
        MyUser MyUser = new MyUser();
        MyUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        MyUser.setLogin(userDTO.getLogin());
        if (userDTO.getLogin().equals("admin")) MyUser.setRole(Role.ADMIN);
        else MyUser.setRole(Role.USER);
        return save(MyUser);
    }

    private MyUser save(MyUser MyUser) {
        return myUserRepository.save(MyUser);
    }

    @Override
    public UserDTO getByName(String name) {
        return map(findOne(name));
    }

    private MyUser findOne(String name) {
        return myUserRepository.findByLogin(name);
    }

    @Override
    public List<UserDTO> getAll() {
        return map(findAll());
    }

    private List<MyUser> findAll() {
        return myUserRepository.findAll();
    }

    @Override
    public void deleteByName(String name) {
        myUserRepository.deleteByLogin(name);
    }

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        MyUser byName = myUserRepository.findByLogin(s);
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + byName.getRole().toString()));
        return new org.springframework.security.core.userdetails.User(
                byName.getLogin(),
                byName.getPassword(),
                simpleGrantedAuthorities
        );
    }

    private Boolean registrationValidation(UserDTO userDTO) {
        if (!userDTO.getPassword().equals(userDTO.getPasswordRepeat())) return false;
        if (myUserRepository.countByLogin(userDTO.getLogin()) > 0) return false;
        return true;
    }

    @Override
    public MyUser get(String name) {
        return findOne(name);
    }
}
