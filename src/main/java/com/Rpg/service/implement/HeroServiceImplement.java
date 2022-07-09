package com.Rpg.service.implement;

import com.Rpg.dto.HeroDTO;
import com.Rpg.dto.MonsterDTO;
import com.Rpg.entity.*;
import com.Rpg.repository.HeroRepository;

import com.Rpg.service.HeroService;
import com.Rpg.service.MyCharacterService;
import com.Rpg.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeroServiceImplement implements HeroService {

    private HeroRepository heroRepository;

    private MyCharacterService myCharacterService;

    private MyUserService myUserService;

    @Autowired
    public HeroServiceImplement(HeroRepository heroRepository, MyCharacterService myCharacterService, MyUserService myUserService) {
        this.heroRepository = heroRepository;
        this.myCharacterService = myCharacterService;
        this.myUserService = myUserService;
    }

    private Hero map(HeroDTO heroDTO) {
        Hero hero = new Hero();
        hero.setName(heroDTO.getName());
        hero.setCurrentHp(heroDTO.getCurrentHp());
        hero.setMyCharacter(heroDTO.getMyCharacter());
        hero.setMyUser(heroDTO.getMyUser());
        return hero;
    }

    private HeroDTO map(Hero hero) {
        HeroDTO heroDTO = new HeroDTO();
        heroDTO.setName(hero.getName());
        heroDTO.setCurrentHp(hero.getCurrentHp());
        heroDTO.setMyCharacter(hero.getMyCharacter());
        heroDTO.setMyUser(hero.getMyUser());
        return heroDTO;
    }

    private List<HeroDTO> map(List<Hero> heroes) {
        List<HeroDTO> heroDtos = new ArrayList<>();
        for (Hero hero : heroes) {
            heroDtos.add(map(hero));
        }
        return heroDtos;
    }

    @Override
    public void create(HeroDTO heroDTO, String chooseCharacter, String name) {
        heroDTO.setMyCharacter(myCharacterService.get(chooseCharacter));
        heroDTO.setMyUser(myUserService.get(name));
        save(map(heroDTO));
    }

    private void save(Hero hero) {
        heroRepository.save(hero);
    }

    @Override
    public HeroDTO getByName(String name) {
        return map(findOne(name));
    }

    private Hero findOne(String name) {
        return heroRepository.findByName(name);
    }

    @Override
    public List<HeroDTO> getAll() {
        return map(findAll());
    }

    private List<Hero> findAll() {
        return heroRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        heroRepository.deleteByName(name);
    }

    @Override
    public List<HeroDTO> getHeroesByUserName(String name) {
        return map(findAllByUserLogin(name));
    }

    private List<Hero> findAllByUserLogin(String name) {
        return heroRepository.findAllByMyUserLogin(name);
    }

    @Override
    public Hero get(String name) {
        return findOne(name);
    }

    @Override
    public void saving(HeroDTO heroDTO) {
        Hero hero = findOne(heroDTO.getName());
        hero.setCurrentHp(heroDTO.getCurrentHp());
        save(hero);
    }

    @Override
    public void kick(HeroDTO heroDTO, MonsterDTO monsterDTO) {

        if (heroDTO.getCurrentHp() > 0) {
            heroDTO.setCurrentHp(heroDTO.getCurrentHp() - monsterDTO.getPower());
        }
        saving(heroDTO);
    }

    @Override
    public void heroAlive(HeroDTO heroDTO) {
        heroDTO.setCurrentHp(heroDTO.getMyCharacter().getHp());
        saving(heroDTO);
    }

    @Override
    public void updateName(String name, String editName) {
        Hero hero = findOne(name);
        hero.setName(editName);
        save(hero);
    }

    @Override
    public void updateCharacter(String name, String editCharacter) {
        Hero hero = findOne(name);
        hero.setMyCharacter(myCharacterService.get(editCharacter));
        save(hero);
    }

    @Override
    public void updateUser(String name, String editUser) {
        Hero hero = findOne(name);
        hero.setMyUser(myUserService.get(editUser));
        save(hero);
    }

}
