package com.one.frontend.controller;

import com.one.frontend.dto.DrawRequest;
import com.one.frontend.model.DrawResult;
import com.one.frontend.model.PrizeNumber;
import com.one.frontend.service.DrawResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/draw")
@Tag(name = "DrawController", description = "抽奖相关操作")
public class DrawController {

    @Autowired
    private DrawResultService drawResultService;

    @PutMapping("/oneprize")
    @Operation(summary = "扭蛋抽獎", description = "根据用户ID、请求列表和产品ID进行抽奖")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "抽奖成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DrawResult.class))),
            @ApiResponse(responseCode = "400", description = "请求错误")
    })
    public ResponseEntity<DrawResult> drawPrize(
            @RequestParam Integer userId,
            @RequestBody(description = "抽奖请求列表", required = true, content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DrawRequest.class)))) List<DrawRequest> drawRequests,
            @RequestParam Long productId) throws Exception {
        DrawResult results = drawResultService.handleDraw(userId, drawRequests, productId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/status/{productId}")
    @Operation(summary = "檢視抽況", description = "根据产品ID获取所有奖项状态")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功返回奖项状态列表", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PrizeNumber.class))))
    })
    public ResponseEntity<List<PrizeNumber>> getDrawStatus(@PathVariable Long productId) {
        List<PrizeNumber> prizes = drawResultService.getAllPrizes(productId);
        return ResponseEntity.ok(prizes);
    }

    @PostMapping("/execute/{productId}")
    @Operation(summary = "一番賞、盲盒抽獎", description = "根据产品ID和用户ID执行抽奖")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "抽奖执行成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DrawResult.class))),
            @ApiResponse(responseCode = "400", description = "请求错误")
    })
    public ResponseEntity<DrawResult> executeDraw(
            @PathVariable Long productId,
            @RequestParam Long userId,
            @RequestParam Integer prizeNumber) {
        try {
            DrawResult drawResult = drawResultService.handleDraw(userId, productId, prizeNumber);
            return ResponseEntity.ok(drawResult);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("/random/{productId}")
    @Operation(summary = "紅利抽獎(隨機制)", description = "为特定产品和用户执行随机抽奖")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "随机抽奖执行成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DrawResult.class))),
            @ApiResponse(responseCode = "400", description = "请求错误")
    })
    public ResponseEntity<DrawResult> executeRandom(
            @PathVariable Long productId,
            @RequestParam Long userId) {
        try {
            DrawResult drawResult = drawResultService.handleDrawRandom(userId, productId);
            return ResponseEntity.ok(drawResult);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}
