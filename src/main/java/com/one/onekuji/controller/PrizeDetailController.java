package com.one.onekuji.controller;

import com.one.onekuji.model.PrizeDetail;
import com.one.onekuji.service.PrizeDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prizeDetail") // 所有端點的公共基礎 URL
@Tag(name = "獎品詳細管理", description = "與獎品詳細相關的操作")
public class PrizeDetailController {

    @Autowired
    private PrizeDetailService prizeDetailService;

    @Operation(summary = "獲取所有獎品詳細", description = "檢索所有獎品詳細的列表")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "成功檢索獎品詳細列表")
    })
    @GetMapping
    public ResponseEntity<List<PrizeDetail>> getAll() {
        List<PrizeDetail> prizeDetailList = prizeDetailService.getAllPrizeDetails();
        return new ResponseEntity<>(prizeDetailList, HttpStatus.OK);
    }

    @Operation(summary = "根據 ID 獲取獎品詳細", description = "根據獎品 ID 獲取獎品詳細")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "成功檢索獎品詳細"),
            @ApiResponse(responseCode = "404", description = "未找到該獎品詳細")
    })
    @GetMapping("/{prizeDetailId}")
    public ResponseEntity<PrizeDetail> getPrizeDetailById(
            @Parameter(description = "獎品詳細的 ID", example = "1") @PathVariable Integer prizeDetailId) {
        PrizeDetail prizeDetail = prizeDetailService.getPrizeDetailById(prizeDetailId);
        if (prizeDetail != null) {
            return new ResponseEntity<>(prizeDetail, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "創建新的獎品詳細", description = "創建一個新的獎品詳細")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "獎品詳細創建成功"),
            @ApiResponse(responseCode = "400", description = "提供的輸入無效")
    })
    @PostMapping
    public ResponseEntity<String> createPrizeDetail(
            @Parameter(description = "要創建的獎品詳細信息") @RequestBody PrizeDetail prizeDetail) {
        String isSuccess = prizeDetailService.createPrizeDetail(prizeDetail);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("創建成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("創建失敗", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "更新現有獎品詳細", description = "根據 ID 更新現有的獎品詳細")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "獎品詳細更新成功"),
            @ApiResponse(responseCode = "404", description = "未找到該獎品詳細")
    })
    @PutMapping("/{prizeDetailId}")
    public ResponseEntity<String> updatePrizeDetail(
            @Parameter(description = "獎品詳細的 ID", example = "1") @PathVariable Integer prizeDetailId,
            @Parameter(description = "要更新的獎品詳細信息") @RequestBody PrizeDetail prizeDetail) {
        PrizeDetail existingPrizeDetail = prizeDetailService.getPrizeDetailById(prizeDetailId);
        if (existingPrizeDetail != null) {
            String isSuccess = prizeDetailService.updatePrizeDetail(prizeDetail);
            if ("1".equals(isSuccess)) {
                return new ResponseEntity<>("更新成功", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("更新失敗", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("獎品詳細未找到", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "刪除獎品詳細", description = "根據 ID 刪除獎品詳細")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "獎品詳細刪除成功"),
            @ApiResponse(responseCode = "404", description = "未找到該獎品詳細")
    })
    @DeleteMapping("/{prizeDetailId}")
    public ResponseEntity<String> deletePrizeDetail(
            @Parameter(description = "獎品詳細的 ID", example = "1") @PathVariable Integer prizeDetailId) {
        String isSuccess = prizeDetailService.deletePrizeDetail(prizeDetailId);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("刪除成功", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("刪除失敗", HttpStatus.BAD_REQUEST);
        }
    }
}
