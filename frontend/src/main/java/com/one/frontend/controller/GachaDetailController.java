package com.one.frontend.controller;

import com.one.frontend.service.GachaDetailService;
import com.one.model.GachaDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/gachaDetail")
public class GachaDetailController {

    @Autowired
    private GachaDetailService gachaDetailService;
    @GetMapping("/query")
    public ResponseEntity<List<GachaDetail>> getAllGachaDetail(){
        List<GachaDetail> resultList = gachaDetailService.getAllGachaDetail();
        return ResponseEntity.ok(resultList);
    }

    @GetMapping("/{gachaDetailId}")
    public ResponseEntity<GachaDetail> getGachaDetailById(@PathVariable Long gachaDetailId){
        GachaDetail gachaDetail = gachaDetailService.getGachaDetailById(gachaDetailId);
        return ResponseEntity.ok(gachaDetail);
    }
}
