package com.one.onekuji.controller;

import com.one.model.Gacha;
import com.one.onekuji.service.GachaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gacha")
@Tag(name = "扭蛋管理", description = "與扭蛋相關的操作")
public class GachaController {

    @Autowired
    private GachaService gachaService;

    @Operation(summary = "獲取所有 Gacha", description = "檢索所有 Gacha 的列表")
    @GetMapping("/query")
    public ResponseEntity<List<Gacha>> getAllGacha() {
        List<Gacha> gacha = gachaService.getAllGacha();
        return new ResponseEntity<>(gacha, HttpStatus.OK);
    }

    @Operation(summary = "通過 ID 獲取 Gacha", description = "根據其 ID 獲取 Gacha")
    @GetMapping("/{gachaId}")
    public ResponseEntity<Gacha> getGachaById(
            @Parameter(description = "Gacha 的 ID", example = "1") @PathVariable Integer gachaId) {
        Gacha gacha = gachaService.getGachaById(gachaId);
        return new ResponseEntity<>(gacha, HttpStatus.OK);
    }

    @Operation(summary = "創建新的 Gacha", description = "創建一個新的 Gacha")
    @PostMapping("/add")
    public ResponseEntity<String> createGacha(
            @Parameter(description = "要創建的 Gacha 詳情") @RequestBody Gacha gacha) {
        String isSuccess = gachaService.createGacha(gacha);
        if (isSuccess.equals("1")) {
            return new ResponseEntity<>("創建成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("創建失敗", HttpStatus.CREATED);
        }
    }

    @Operation(summary = "更新 Gacha", description = "根據 ID 更新現有的 Gacha")
    @PutMapping("/{gachaId}")
    public ResponseEntity<String> updateGacha(
            @Parameter(description = "Gacha 的 ID", example = "1") @PathVariable Integer gachaId) {
        Gacha gacha = gachaService.getGachaById(gachaId);
        String isSuccess = gachaService.updateGacha(gacha);
        if (isSuccess.equals("1")) {
            return new ResponseEntity<>("更新成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("更新失敗", HttpStatus.CREATED);
        }
    }

    @Operation(summary = "刪除 Gacha", description = "根據 ID 刪除 Gacha")
    @DeleteMapping("/gacha/{gachaId}")
    public ResponseEntity<String> deleteGacha(
            @Parameter(description = "Gacha 的 ID", example = "1") @PathVariable Integer gachaId) {
        String isSuccess = gachaService.deleteGacha(gachaId);
        if (isSuccess.equals("1")) {
            return new ResponseEntity<>("刪除成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("刪除失敗", HttpStatus.CREATED);
        }
    }
}
