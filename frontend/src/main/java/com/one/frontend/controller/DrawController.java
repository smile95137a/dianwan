package com.one.frontend.controller;

import com.one.frontend.dto.DrawRequest;
import com.one.frontend.service.DrawResultService;
import com.one.model.DrawResult;
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
                                                @RequestBody List<DrawRequest> drawRequests) throws Exception {
        drawResultService.handleDraw(userId, drawRequests);
        return ResponseEntity.ok().build();
    }

}
