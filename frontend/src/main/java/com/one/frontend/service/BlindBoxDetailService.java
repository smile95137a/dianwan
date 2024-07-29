package com.one.frontend.service;

import com.one.model.BlindBox;
import com.one.model.BlindBoxDetail;
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

    public List<BlindBoxDetail> getAllBlindBoxDetail(){
        return blindBoxDetailRepository.getAllBlindBoxDetail();
    }

    public BlindBox getBlindBoxById(Long blindBoxId){
        return blindBoxDetailRepository.getBlindBoxById(blindBoxId);
    }

    public BlindBoxDetail getBlindBoxDetailById(Long blindBoxDetailId){
        return blindBoxDetailRepository.getBlindBoxDetailById(blindBoxDetailId);
    }

}
