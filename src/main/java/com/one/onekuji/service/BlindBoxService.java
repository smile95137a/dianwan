package com.one.onekuji.service;

import com.one.onekuji.model.BlindBox;
import com.one.onekuji.repository.BlindBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlindBoxService {

    @Autowired
    private BlindBoxRepository blindBoxRepository;
    public  String updateBlindBox(BlindBox blindBox) {
        try {
            blindBoxRepository.updateBlindBox(blindBox);
            return "1";
        }catch (Exception e){
            return "0";
        }
    }

    public List<BlindBox> getAllBlindBox() {
        return blindBoxRepository.getAllBlindBox();
    }

    public BlindBox getBlindBoxById(Integer blindBoxId) {
        return blindBoxRepository.getBlindBoxById(blindBoxId);
    }

    public String createBlindBox(BlindBox blindBox) {
        try {
            blindBoxRepository.createBlindBox(blindBox);
            return "1";
        }catch (Exception e){
            return "0";
        }
    }

    public String deleteBlindBox(Integer blindBoxId) {
        try {
            blindBoxRepository.deleteBlindBox(blindBoxId);
            return "1";
        }catch (Exception e){
            return "0";
        }
    }
}
