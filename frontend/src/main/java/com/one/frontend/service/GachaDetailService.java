package com.one.frontend.service;

import com.one.model.Gacha;
import com.one.model.GachaDetail;
import com.one.repository.GachaDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GachaDetailService {

    @Autowired
    private GachaDetailRepository gachaDetailRepository;

    public List<GachaDetail> getAllGachaDetail(){
        return gachaDetailRepository.getAllGachaDetail();
    }

    public Gacha getGachaById(Long gachaId){
        return gachaDetailRepository.getGachaById(gachaId);
    }
}
