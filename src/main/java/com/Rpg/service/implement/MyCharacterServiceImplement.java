package com.Rpg.service.implement;

import com.Rpg.dto.CharacterDTO;

import com.Rpg.entity.MyCharacter;
import com.Rpg.repository.MyCharacterRepository;

import com.Rpg.service.ImageService;
import com.Rpg.service.MyCharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MyCharacterServiceImplement implements MyCharacterService {

    @Value("${myCharacters}")
    private String charactersPath;

    private MyCharacterRepository myCharacterRepository;

    private ImageService imageService;

    @Autowired
    public MyCharacterServiceImplement(@Lazy MyCharacterRepository myCharacterRepository, ImageService imageService) {
        this.myCharacterRepository = myCharacterRepository;
        this.imageService = imageService;
    }

    private MyCharacter map(CharacterDTO characterDTO) {
        MyCharacter myCharacter = new MyCharacter();
        myCharacter.setName(characterDTO.getName());
        myCharacter.setHp(characterDTO.getHp());
        myCharacter.setMp(characterDTO.getMp());
        myCharacter.setPower(characterDTO.getPower());
        myCharacter.setImage(characterDTO.getImage());
        return myCharacter;
    }

    private CharacterDTO map(MyCharacter myCharacter) {
        CharacterDTO characterDTO = new CharacterDTO();
        characterDTO.setName(myCharacter.getName());
        characterDTO.setHp(myCharacter.getHp());
        characterDTO.setMp(myCharacter.getMp());
        characterDTO.setPower(myCharacter.getPower());
        characterDTO.setImage(myCharacter.getImage());
        return characterDTO;
    }

    private List<CharacterDTO> map(List<MyCharacter> myCharacters) {
        List<CharacterDTO> characterDTOS = new ArrayList<>();
        for (MyCharacter myCharacter : myCharacters) {
            characterDTOS.add(map(myCharacter));
        }
        return characterDTOS;
    }

    @Override
    public void create(CharacterDTO characterDTO, MultipartFile multipartFile) throws IOException {
        MyCharacter myCharacter = map(characterDTO);
        String resultFileName = imageService.saveFile(charactersPath, multipartFile);
        myCharacter.setImage(resultFileName);
        save(myCharacter);
    }

    private void save(MyCharacter myCharacter) {
        myCharacterRepository.save(myCharacter);
    }

    @Override
    public CharacterDTO getByName(String name) {
        return map(findOne(name));
    }

    private MyCharacter findOne(String name) {
        return myCharacterRepository.findByName(name);
    }

    @Override
    public List<CharacterDTO> getAll() {
        return map(findAll());
    }

    private List<MyCharacter> findAll() {
        return myCharacterRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        if (imageService.deleteFile(charactersPath, findOne(name).getImage()))
            myCharacterRepository.deleteByName(name);
    }

    @Override
    public MyCharacter get(String name) {
        return findOne(name);
    }

    @Override
    public void updateName(String name, String updateName) {
        MyCharacter myCharacter = findOne(name);
        myCharacter.setName(updateName);
        save(myCharacter);
    }

    @Override
    public void updateHp(String name, Integer updateHp) {
        MyCharacter myCharacter = findOne(name);
        myCharacter.setHp(updateHp);
        save(myCharacter);
    }

    @Override
    public void updateMp(String name, Integer updateMp) {
        MyCharacter myCharacter = findOne(name);
        myCharacter.setMp(updateMp);
        save(myCharacter);
    }

    @Override
    public void updatePower(String name, Integer updatePower) {
        MyCharacter myCharacter = findOne(name);
        myCharacter.setPower(updatePower);
        save(myCharacter);
    }

    @Override
    public void updateImage(String name, MultipartFile multipartFile) throws IOException {
        MyCharacter myCharacter = findOne(name);
        if (myCharacter.getImage() != null)
            imageService.deleteFile(charactersPath, myCharacter.getImage());

        myCharacter.setImage(imageService.saveFile(charactersPath, multipartFile));
        save(myCharacter);
    }

}
