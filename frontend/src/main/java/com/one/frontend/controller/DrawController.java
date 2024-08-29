package com.one.frontend.controller;

import com.one.frontend.config.security.CustomUserDetails;
import com.one.frontend.config.security.SecurityUtils;
import com.one.frontend.dto.DrawRequest;
import com.one.frontend.model.DrawResult;
import com.one.frontend.model.PrizeNumber;
import com.one.frontend.service.DrawResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/draw")
@Tag(name = "DrawController", description = "抽奖相关操作")
public class DrawController {

    @Autowired
    private DrawResultService drawResultService;

    @PostMapping("/oneprize/{userUid}")
    @Operation(summary = "扭蛋抽獎", description = "根据用户ID、请求和产品ID进行抽奖")
    public ResponseEntity<DrawResult> drawPrize(
            @PathVariable String userUid,
            @RequestBody(description = "抽奖请求", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = DrawRequest.class))) List<DrawRequest> drawRequest) throws Exception {

        CustomUserDetails userDetails = SecurityUtils.getCurrentUserPrinciple();

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long authenticatedUserId = userDetails.getId();

        if (!authenticatedUserId.equals(userUid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }else{
            List<DrawResult> result = drawResultService.handleDraw(userUid, drawRequest);
            return ResponseEntity.ok((DrawResult) result);
        }



    }


    @GetMapping("/status/{productId}")
    @Operation(summary = "檢視抽況", description = "根据产品ID获取所有奖项状态")
    public ResponseEntity<List<PrizeNumber>> getDrawStatus(@PathVariable Long productId) {
        List<PrizeNumber> prizes = drawResultService.getAllPrizes(productId);
        return ResponseEntity.ok(prizes);
    }

    @PostMapping("/execute/{productId}")
    @Operation(summary = "一番賞、盲盒抽獎", description = "根据产品ID和用户ID执行抽奖")
    public ResponseEntity<DrawResult> executeDraw(
            @PathVariable Long productId,
            @RequestParam String userUid,
            @RequestParam List<Integer> prizeNumber) {
        try {
            DrawResult drawResult = drawResultService.handleDraw2(userUid, productId, prizeNumber);
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
            @RequestParam String userUid) {
        try {
            DrawResult drawResult = drawResultService.handleDrawRandom(userUid, productId);
            return ResponseEntity.ok(drawResult);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}
