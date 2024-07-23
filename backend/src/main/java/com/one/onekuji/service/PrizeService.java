package com.one.onekuji.service;

import com.one.onekuji.model.Prize;
import com.one.onekuji.model.User;
import com.one.onekuji.repository.PrizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrizeService {
    @Autowired
    private PrizeRepository prizeRepository;

    public List<Prize> getAllPrize(){
        return prizeRepository.getAllPrize();
    }

    public Prize getPrizeById(Integer prizeId) {
        return prizeRepository.getPrizeById(prizeId);
    }

    public String updatePrize(Prize prize) {
        try {
            prizeRepository.updatePrize(prize);
            return "1";
        }catch (Exception e){
            return "0";
        }
    }

    public String createPrize(Prize prize) {
        try {
            prizeRepository.createPrize(prize);
            return "1";
        }catch (Exception e){
            return "0";
        }
    }

    public String deletePrize(Integer prizeId) {
        try {
            prizeRepository.deletePrize(prizeId);
            return "1";
        }catch (Exception e){
            return "0";
        }
    }
}
