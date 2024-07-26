package com.one.frontend.service;

import com.one.model.Gacha;
import com.one.model.GachaDetail;
import com.one.repository.BlindBoxDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlindBoxDetailService {

    @Autowired
    private BlindBoxDetailRepository blindBoxDetailRepository;

    public List<GachaDetail> getAllBlindBoxDetail(){
        return blindBoxDetailRepository.getAllBlindBoxDetail();
    }

    public Gacha getBlindBoxById(Long gachaId){
        return blindBoxDetailRepository.getBlindBoxById(gachaId);
    }

}
