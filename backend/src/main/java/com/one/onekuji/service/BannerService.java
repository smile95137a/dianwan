package com.one.onekuji.service;

import com.one.onekuji.model.Banner;
import com.one.onekuji.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    public Banner findById(Long bannerId) {
        return bannerRepository.findById(bannerId);
    }

    public List<Banner> findAll() {
        return bannerRepository.findAll();
    }

    public void createBanner(Banner banner) {
        banner.setBannerUid(UUID.randomUUID().toString());
        banner.setCreatedAt(LocalDateTime.now());
        banner.setUpdatedAt(LocalDateTime.now());
        bannerRepository.insert(banner);
    }

    public void updateBanner(Long bannerId , Banner banner) {
        Banner reqBanner = bannerRepository.findById(bannerId);
        reqBanner.setBannerId(bannerId);
        reqBanner.setBannerImageUrl(banner.getBannerImageUrl());
        reqBanner.setProductId(banner.getBannerId());
        reqBanner.setStatus(banner.getStatus());
        banner.setUpdatedAt(LocalDateTime.now());

        bannerRepository.update(reqBanner);
    }

    public void deleteBanner(Long bannerId) {
        bannerRepository.deleteById(bannerId);
    }
}
