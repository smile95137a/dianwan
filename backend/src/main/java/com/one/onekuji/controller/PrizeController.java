package com.one.onekuji.controller;

import com.one.onekuji.model.Prize;
import com.one.onekuji.service.PrizeService;
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
@RequestMapping("/prize") // 所有端點的公共基礎 URL
@Tag(name = "獎品管理", description = "與獎品相關的操作")
public class PrizeController {

    @Autowired
    private PrizeService prizeService;

    @Operation(summary = "獲取所有獎品", description = "檢索所有獎品的列表")
    @GetMapping
    public ResponseEntity<List<Prize>> getAll() {
        List<Prize> prizeList = prizeService.getAllPrize();
        return new ResponseEntity<>(prizeList, HttpStatus.OK);
    }

    @Operation(summary = "通過 ID 獲取獎品", description = "根據其 ID 獲取獎品")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "獎品檢索成功"),
            @ApiResponse(responseCode = "404", description = "獎品未找到")
    })
    @GetMapping("/{prizeId}")
    public ResponseEntity<Prize> getPrizeById(
            @Parameter(description = "獎品的 ID", example = "1") @PathVariable Integer prizeId) {
        Prize prize = prizeService.getPrizeById(prizeId);
        if (prize != null) {
            return new ResponseEntity<>(prize, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "創建新的獎品", description = "創建一個新的獎品")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "獎品創建成功"),
            @ApiResponse(responseCode = "400", description = "無效的輸入")
    })
    @PostMapping
    public ResponseEntity<String> createPrize(
            @Parameter(description = "要創建的獎品詳細信息") @RequestBody Prize prize) {
        String isSuccess = prizeService.createPrize(prize);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("創建成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("創建失敗", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "更新現有獎品", description = "根據 ID 更新現有的獎品")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "獎品更新成功"),
            @ApiResponse(responseCode = "404", description = "獎品未找到")
    })
    @PutMapping("/{prizeId}")
    public ResponseEntity<String> updatePrize(
            @Parameter(description = "獎品的 ID", example = "1") @PathVariable Integer prizeId,
            @Parameter(description = "要更新的獎品詳細信息") @RequestBody Prize prize) {
        Prize existingPrize = prizeService.getPrizeById(prizeId);
        if (existingPrize != null) {
            String isSuccess = prizeService.updatePrize(prize);
            if ("1".equals(isSuccess)) {
                return new ResponseEntity<>("更新成功", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("更新失敗", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("獎品未找到", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "刪除獎品", description = "根據 ID 刪除獎品")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "獎品刪除成功"),
            @ApiResponse(responseCode = "404", description = "獎品未找到")
    })
    @DeleteMapping("/{prizeId}")
    public ResponseEntity<String> deletePrize(
            @Parameter(description = "獎品的 ID", example = "1") @PathVariable Integer prizeId) {
        String isSuccess = prizeService.deletePrize(prizeId);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("刪除成功", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("刪除失敗", HttpStatus.BAD_REQUEST);
        }
    }
}
