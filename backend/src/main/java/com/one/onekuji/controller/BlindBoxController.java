package com.one.onekuji.controller;

import com.one.onekuji.model.BlindBox;
import com.one.onekuji.service.BlindBoxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "盲盒管理", description = "與盲盒相關的操作")
public class BlindBoxController {

    @Autowired
    private BlindBoxService blindBoxService;

    @Operation(summary = "獲取所有 Blind Box", description = "檢索所有 Blind Box 的列表")
    @GetMapping("/blindBox")
    public ResponseEntity<List<BlindBox>> getAllBlindBox() {
        List<BlindBox> blindBox = blindBoxService.getAllBlindBox();
        return new ResponseEntity<>(blindBox, HttpStatus.OK);
    }

    @Operation(summary = "通過 ID 獲取 Blind Box", description = "根據其 ID 獲取 Blind Box")
    @GetMapping("/blindBox/{blindBoxId}")
    public ResponseEntity<BlindBox> getBlindBoxById(
            @Parameter(description = "Blind Box 的 ID", example = "1") @PathVariable Integer blindBoxId) {
        BlindBox blindBox = blindBoxService.getBlindBoxById(blindBoxId);
        return new ResponseEntity<>(blindBox, HttpStatus.OK);
    }

    @Operation(summary = "創建新的 Blind Box", description = "創建一個新的 Blind Box")
    @PostMapping("/blindBox")
    public ResponseEntity<String> createBlindBox(
            @Parameter(description = "要創建的 Blind Box 詳情") @RequestBody BlindBox blindBox) {
        String isSuccess = blindBoxService.createBlindBox(blindBox);
        if (isSuccess.equals("1")) {
            return new ResponseEntity<>("創建成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("創建失敗", HttpStatus.CREATED);
        }
    }

    @Operation(summary = "更新 Blind Box", description = "根據 ID 更新現有的 Blind Box")
    @PutMapping("/blindBox/{blindBoxId}")
    public ResponseEntity<String> updateBlindBox(
            @Parameter(description = "Blind Box 的 ID", example = "1") @PathVariable Integer blindBoxId) {
        BlindBox blindBox = blindBoxService.getBlindBoxById(blindBoxId);
        String isSuccess = blindBoxService.updateBlindBox(blindBox);
        if (isSuccess.equals("1")) {
            return new ResponseEntity<>("更新成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("更新失敗", HttpStatus.CREATED);
        }
    }

    @Operation(summary = "刪除 Blind Box", description = "根據 ID 刪除 Blind Box")
    @DeleteMapping("/blindBox/{blindBoxId}")
    public ResponseEntity<String> deleteBlindBox(
            @Parameter(description = "Blind Box 的 ID", example = "1") @PathVariable Integer blindBoxId) {
        String isSuccess = blindBoxService.deleteBlindBox(blindBoxId);
        if (isSuccess.equals("1")) {
            return new ResponseEntity<>("刪除成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("刪除失敗", HttpStatus.CREATED);
        }
    }
}
