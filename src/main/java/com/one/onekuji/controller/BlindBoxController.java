package com.one.onekuji.controller;

import com.one.onekuji.model.BlindBox;
import com.one.onekuji.service.BlindBoxService;
import com.one.onekuji.service.BlindBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BlindBoxController     {

    @Autowired
    private BlindBoxService blindBoxService;


    @GetMapping("/blindBox")
    public ResponseEntity<List<BlindBox>> getAllBlindBox(){
        List<BlindBox> blindBox = blindBoxService.getAllBlindBox();
        return new ResponseEntity<>(blindBox, HttpStatus.OK);
    }

    @GetMapping("/blindBox/{blindBoxId}")
    public ResponseEntity<BlindBox> getBlindBoxById(@PathVariable Integer BlindBoxId){
        BlindBox blindBox = blindBoxService.getBlindBoxById(BlindBoxId);
        return new ResponseEntity<>(blindBox , HttpStatus.OK);
    }

    @PostMapping("/blindBox")
    public ResponseEntity<String> createBlindBox(@RequestBody BlindBox blindBox){
        String isSuccess = blindBoxService.createBlindBox(blindBox);
        if(isSuccess.equals("1")){
            return new ResponseEntity<>("創建成功" , HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("創建失敗" , HttpStatus.CREATED);
        }
    }

    @PutMapping("/blindBox/{blindBoxId}")
    public ResponseEntity<String> updateBlindBox(@PathVariable Integer blindBoxId){
        BlindBox blindBox = blindBoxService.getBlindBoxById(blindBoxId);

        String isSuccess = blindBoxService.updateBlindBox(blindBox);

        if(isSuccess.equals("1")){
            return new ResponseEntity<>("更新成功" , HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("更新失敗" , HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/blindBox/{blindBoxId}")
    public ResponseEntity<String> deleteBlindBox(@PathVariable Integer blindBoxId){
        String isSuccess = blindBoxService.deleteBlindBox(blindBoxId);
        if(isSuccess.equals("1")){
            return new ResponseEntity<>("刪除成功" , HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("刪除失敗" , HttpStatus.CREATED);
        }
    }
}
