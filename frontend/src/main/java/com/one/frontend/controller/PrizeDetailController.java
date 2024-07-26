package com.one.frontend.controller;

import com.one.frontend.dto.PrizeDetailDto;
import com.one.frontend.service.PrizeDetailService;
import com.one.model.PrizeDetail;
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
@RequestMapping("/prizeDetail")
@Tag(name = "獎品詳細管理", description = "與獎品詳細相關的操作")
public class PrizeDetailController {

    @Autowired
    private PrizeDetailService prizeDetailService;

    @Operation(summary = "獲取所有獎品詳細", description = "檢索所有獎品詳細的列表")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "成功檢索獎品詳細列表")
    })
    @GetMapping("/query")
    public ResponseEntity<List<PrizeDetailDto>> getAll() {
        List<PrizeDetailDto> prizeDetailList = prizeDetailService.getAllPrizeDetails();
        return new ResponseEntity<>(prizeDetailList, HttpStatus.OK);
    }

    @Operation(summary = "根據 ID 獲取獎品詳細", description = "根據獎品 ID 獲取獎品詳細")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "成功檢索獎品詳細"),
            @ApiResponse(responseCode = "404", description = "未找到該獎品詳細")
    })
    @GetMapping("/{prizeDetailId}")
    public ResponseEntity<PrizeDetailDto> getPrizeDetailById(
            @Parameter(description = "獎品詳細的 ID", example = "1") @PathVariable Integer prizeDetailId) {
        PrizeDetailDto prizeDetail = prizeDetailService.getPrizeDetailById(prizeDetailId);
        if (prizeDetail != null) {
            return new ResponseEntity<>(prizeDetail, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
