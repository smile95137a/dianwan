package com.one.frontend.controller;

import com.one.frontend.service.BlindBoxDetailService;
import com.one.model.BlindBoxDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blindBoxDetail")
public class BlindBoxDetailController {

    @Autowired
    private BlindBoxDetailService blindBoxDetailService;
    @GetMapping("/query")
    public ResponseEntity<List<BlindBoxDetail>> getAllBlindBoxDetail(){
        List<BlindBoxDetail> resultList = blindBoxDetailService.getAllBlindBoxDetail();
        return ResponseEntity.ok(resultList);
    }

    @GetMapping("/{blindBoxDetailId}")
    public ResponseEntity<BlindBoxDetail> getBlindBoxDetailById(@PathVariable Long blindBoxDetailId){
        BlindBoxDetail blindBoxDetail = blindBoxDetailService.getBlindBoxDetailById(blindBoxDetailId);
        return ResponseEntity.ok(blindBoxDetail);
    }
}
