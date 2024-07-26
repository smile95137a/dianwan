package com.one.frontend.service;

import com.one.frontend.dto.PrizeDetailDto;
import com.one.model.PrizeDetail;
import com.one.repository.PrizeDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrizeDetailService {

    @Autowired
    private PrizeDetailRepository prizeDetailRepository;

    public PrizeDetailDto getPrizeDetailById(Integer prizeDetailId) {
        PrizeDetail prizeDetail = prizeDetailRepository.getPrizeDetailById(prizeDetailId);
        PrizeDetailDto prizeDetailDto = new PrizeDetailDto();
        prizeDetailDto.setPrizeDetailId(prizeDetail.getPrizeDetailId());
        prizeDetailDto.setPrizeId(prizeDetail.getPrizeId());
        prizeDetailDto.setDescription(prizeDetail.getDescription());
        prizeDetailDto.setSize(prizeDetail.getSize());
        prizeDetailDto.setMaterial(prizeDetail.getMaterial());
        prizeDetailDto.setSecret(prizeDetail.isSecret());
        prizeDetailDto.setQuantity(prizeDetail.getQuantity());
        prizeDetailDto.setProductName(prizeDetail.getProductName());
        prizeDetailDto.setGrade(prizeDetail.getGrade());
        prizeDetailDto.setCount(prizeDetail.getCount());
        prizeDetailDto.setImage(prizeDetail.getImage());
        prizeDetailDto.setStatus(prizeDetail.getStatus());
        return prizeDetailDto;
    }

    public List<PrizeDetailDto> getAllPrizeDetails() {
        List<PrizeDetail> prizeDetails = prizeDetailRepository.getAllPrizeDetails();
        List<PrizeDetailDto> prizeDetailDtos = new ArrayList<>();
        for (PrizeDetail prizeDetail : prizeDetails) {
            PrizeDetailDto prizeDetailDto = new PrizeDetailDto();
            prizeDetailDto.setPrizeDetailId(prizeDetail.getPrizeDetailId());
            prizeDetailDto.setPrizeId(prizeDetail.getPrizeId());
            prizeDetailDto.setDescription(prizeDetail.getDescription());
            prizeDetailDto.setSize(prizeDetail.getSize());
            prizeDetailDto.setMaterial(prizeDetail.getMaterial());
            prizeDetailDto.setSecret(prizeDetail.isSecret());
            prizeDetailDto.setQuantity(prizeDetail.getQuantity());
            prizeDetailDto.setProductName(prizeDetail.getProductName());
            prizeDetailDto.setGrade(prizeDetail.getGrade());
            prizeDetailDto.setCount(prizeDetail.getCount());
            prizeDetailDto.setImage(prizeDetail.getImage());
            prizeDetailDto.setStatus(prizeDetail.getStatus());
            prizeDetailDtos.add(prizeDetailDto);
        }
        return prizeDetailDtos;
    }
}
