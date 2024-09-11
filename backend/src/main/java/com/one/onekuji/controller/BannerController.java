package com.one.onekuji.controller;

import com.one.onekuji.model.ApiResponse;
import com.one.onekuji.model.Banner;
import com.one.onekuji.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Banner>> getBannerById(@PathVariable("id") Long bannerId) {
        try {
            Banner banner = bannerService.findById(bannerId);
            ApiResponse<Banner> response = new ApiResponse<>(200, "Banner found", true, banner);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Banner> response = new ApiResponse<>(400, "Banner not found", false, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Banner>>> getAllBanners() {
        try {
            List<Banner> banners = bannerService.findAll();
            ApiResponse<List<Banner>> response = new ApiResponse<>(200, "Banners found", true, banners);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<Banner>> response = new ApiResponse<>(400, "No banners found", false, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Banner>> createBanner(@RequestBody Banner banner) {
        try {

            bannerService.createBanner(banner);
            ApiResponse<Banner> response = new ApiResponse<>(200, "Banner created", true, banner);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Banner> response = new ApiResponse<>(400, "Banner creation failed", false, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Banner>> updateBanner(@PathVariable("id") Long bannerId, @RequestBody Banner banner) {
        try {

            bannerService.updateBanner(bannerId , banner);
            ApiResponse<Banner> response = new ApiResponse<>(200, "Banner updated", true, banner);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Banner> response = new ApiResponse<>(400, "Banner update failed", false, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBanner(@PathVariable("id") Long bannerId) {
        try {
            bannerService.deleteBanner(bannerId);
            ApiResponse<Void> response = new ApiResponse<>(200, "Banner deleted", true, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(400, "Banner deletion failed", false, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
