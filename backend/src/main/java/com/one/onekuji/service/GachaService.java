package com.one.onekuji.service;

import com.one.model.Gacha;
import com.one.repository.GachaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GachaService {

    @Autowired
    private GachaRepository gachaRepository;

    public List<Gacha> getAllGacha() {
        return gachaRepository.getAllGacha();
    }

    public Gacha getGachaById(Integer gachaId) {
        return gachaRepository.getGachaById(gachaId);
    }

    public String createGacha(Gacha gacha) {
        try {
            gachaRepository.createGacha(gacha);
            return "1";
        }catch (Exception e){
            return "0";
        }
    }

    public String updateGacha(Gacha gacha) {
        try {
            gachaRepository.updateGacha(gacha);
            return "1";
        }catch (Exception e){
            return "0";
        }
    }

    public String deleteGacha(Integer gachaId) {
        try {
            gachaRepository.deleteGacha(gachaId);
            return "1";
        }catch (Exception e){
            return "0";
        }
    }
}
