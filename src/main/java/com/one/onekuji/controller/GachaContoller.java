package com.one.onekuji.controller;

import com.one.onekuji.model.Gacha;
import com.one.onekuji.service.GachaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GachaContoller {

    @Autowired
    private GachaService gachaService;


    @GetMapping("/gacha")
    public ResponseEntity<List<Gacha>> getAllGacha(){
        List<Gacha> gacha = gachaService.getAllGacha();
        return new ResponseEntity<>(gacha, HttpStatus.OK);
    }

    @GetMapping("/gacha/{gachaId}")
    public ResponseEntity<Gacha> getGachaById(@PathVariable Integer gachaId){
        Gacha gacha = gachaService.getGachaById(gachaId);
        return new ResponseEntity<>(gacha , HttpStatus.OK);
    }

    @PostMapping("/gacha")
    public ResponseEntity<String> createGacha(@RequestBody Gacha gacha){
        String isSuccess = gachaService.createGacha(gacha);
        if(isSuccess.equals("1")){
            return new ResponseEntity<>("創建成功" , HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("創建失敗" , HttpStatus.CREATED);
        }
    }

    @PutMapping("/gacha/{gachaId}")
    public ResponseEntity<String> updateGacha(@PathVariable Integer gachaId){
        Gacha gacha = gachaService.getGachaById(gachaId);

        String isSuccess = gachaService.updateGacha(gacha);

        if(isSuccess.equals("1")){
            return new ResponseEntity<>("更新成功" , HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("更新失敗" , HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/gacha/{gachaId}")
    public ResponseEntity<String> deleteGacha(@PathVariable Integer gachaId){
        String isSuccess = gachaService.deleteGacha(gachaId);
        if(isSuccess.equals("1")){
            return new ResponseEntity<>("刪除成功" , HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("刪除失敗" , HttpStatus.CREATED);
        }
    }
}
