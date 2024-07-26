package com.one.frontend.service;

import com.one.frontend.dto.PrizeDto;
import com.one.model.Prize;
import com.one.repository.PrizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrizeService {
    @Autowired
    private PrizeRepository prizeRepository;

    public List<PrizeDto> getAllPrize(){
        List<PrizeDto> resultList = new ArrayList<>();
        List<Prize> prizeList = prizeRepository.getAllPrize();
        for(Prize prize : prizeList){
            PrizeDto prizeDto = new PrizeDto();
            prizeDto.setPrizeId(prize.getPrizeId());
            prizeDto.setDescription(prize.getDescription());
            prizeDto.setPrice(prize.getPrice());
            prizeDto.setTotalQuantity(prize.getTotalQuantity());
            prizeDto.setRemainingQuantity(prize.getRemainingQuantity());
            prizeDto.setMainImageUrl(prize.getMainImageUrl());
            prizeDto.setReleaseDate(prize.getReleaseDate());
            prizeDto.setManufacturer(prize.getManufacturer());
            prizeDto.setLimited(prize.isLimited());
            prizeDto.setName(prize.getName());
            prizeDto.setStartDate(prize.getStartDate());
            prizeDto.setEndDate(prize.getEndDate());
            prizeDto.setUnshippedQuantity(prize.getUnshippedQuantity());
            resultList.add(prizeDto);
        }
        return resultList;
    }

    public PrizeDto getPrizeById(Integer prizeId) {
        Prize prize =  prizeRepository.getPrizeById(prizeId);
        PrizeDto prizeDto = new PrizeDto();
        prizeDto.setPrizeId(prize.getPrizeId());
        prizeDto.setDescription(prize.getDescription());
        prizeDto.setPrice(prize.getPrice());
        prizeDto.setTotalQuantity(prize.getTotalQuantity());
        prizeDto.setRemainingQuantity(prize.getRemainingQuantity());
        prizeDto.setMainImageUrl(prize.getMainImageUrl());
        prizeDto.setReleaseDate(prize.getReleaseDate());
        prizeDto.setManufacturer(prize.getManufacturer());
        prizeDto.setLimited(prize.isLimited());
        prizeDto.setName(prize.getName());
        prizeDto.setStartDate(prize.getStartDate());
        prizeDto.setEndDate(prize.getEndDate());
        prizeDto.setUnshippedQuantity(prize.getUnshippedQuantity());
        return prizeDto;
    }


}
