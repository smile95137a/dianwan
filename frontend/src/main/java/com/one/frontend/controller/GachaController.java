package com.one.frontend.controller;

import com.one.frontend.service.GachaService;
import com.one.model.Gacha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gacha")
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
}
