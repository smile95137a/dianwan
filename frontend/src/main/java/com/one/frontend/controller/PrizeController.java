package com.one.frontend.controller;

import com.one.frontend.dto.PrizeDto;
import com.one.frontend.service.PrizeService;
import com.one.model.Prize;
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
@RequestMapping("/prize")
@Tag(name = "獎品管理", description = "與獎品相關的操作")
public class PrizeController {

    @Autowired
    private PrizeService prizeService;

    @Operation(summary = "獲取所有獎品", description = "檢索所有獎品的列表")
    @GetMapping("/query")
    public ResponseEntity<List<PrizeDto>> getAll() {
        List<PrizeDto> prizeList = prizeService.getAllPrize();
        return new ResponseEntity<>(prizeList, HttpStatus.OK);
    }

    @Operation(summary = "通過 ID 獲取獎品", description = "根據其 ID 獲取獎品")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "獎品檢索成功"),
            @ApiResponse(responseCode = "404", description = "獎品未找到")
    })
    @GetMapping("/{prizeId}")
    public ResponseEntity<PrizeDto> getPrizeById(
            @Parameter(description = "獎品的 ID", example = "1") @PathVariable Integer prizeId) {
        PrizeDto prize = prizeService.getPrizeById(prizeId);
        if (prize != null) {
            return new ResponseEntity<>(prize, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
