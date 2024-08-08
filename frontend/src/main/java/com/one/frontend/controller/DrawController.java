package com.one.frontend.controller;

import com.one.frontend.dto.DrawRequest;
import com.one.frontend.model.DrawResult;
import com.one.frontend.model.PrizeNumber;
import com.one.frontend.service.DrawResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/draw")
public class DrawController {

    @Autowired
    private DrawResultService drawResultService;
    @PutMapping("/oneprize")
    public ResponseEntity<DrawResult> drawPrize(@RequestParam Integer userId,
                                                @RequestBody List<DrawRequest> drawRequests,
                                                @RequestParam Long productId) throws Exception {
        DrawResult results = drawResultService.handleDraw(userId, drawRequests , productId);
        return ResponseEntity.ok(results);
    }

    // 查看抽奖状态
    @GetMapping("/status/{productId}")
    public ResponseEntity<List<PrizeNumber>> getDrawStatus(@PathVariable Long productId) {
        List<PrizeNumber> prizes = drawResultService.getAllPrizes(productId);
        return ResponseEntity.ok(prizes);
    }

    // 执行抽奖
    @PostMapping("/execute/{productId}")
    public ResponseEntity<DrawResult> executeDraw(@PathVariable Long productId, @RequestParam Long userId, @RequestParam Integer prizeNumber) {
        try {
            DrawResult drawResult = drawResultService.handleDraw(userId, productId, prizeNumber);
            return ResponseEntity.ok(drawResult);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("/random/{productId}")
    public ResponseEntity<DrawResult> executeRandom(@PathVariable Long productId, @RequestParam Long userId) {
        try {
            DrawResult drawResult = drawResultService.handleDrawRandom(userId, productId);
            return ResponseEntity.ok(drawResult);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}
