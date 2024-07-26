package com.one.frontend.controller;

import com.one.frontend.service.BlindBoxService;
import com.one.model.BlindBox;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blindBox")
@Tag(name = "盲盒管理", description = "與盲盒相關的操作")
public class BlindBoxController {

    @Autowired
    private BlindBoxService blindBoxService;

    @Operation(summary = "獲取所有 Blind Box", description = "檢索所有 Blind Box 的列表")
    @GetMapping("/query")
    public ResponseEntity<List<BlindBox>> getAllBlindBox() {
        List<BlindBox> blindBox = blindBoxService.getAllBlindBox();
        return new ResponseEntity<>(blindBox, HttpStatus.OK);
    }

    @Operation(summary = "通過 ID 獲取 Blind Box", description = "根據其 ID 獲取 Blind Box")
    @GetMapping("/{blindBoxId}")
    public ResponseEntity<BlindBox> getBlindBoxById(
            @Parameter(description = "Blind Box 的 ID", example = "1") @PathVariable Integer blindBoxId) {
        BlindBox blindBox = blindBoxService.getBlindBoxById(blindBoxId);
        return new ResponseEntity<>(blindBox, HttpStatus.OK);
    }
}
