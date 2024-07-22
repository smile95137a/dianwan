package com.one.onekuji.controller;

import com.one.onekuji.model.Prize;
import com.one.onekuji.service.PrizeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prize") // Common base URL for all endpoints in this controller
@Tag(name = "Prize Management", description = "Operations related to prizes")
public class PrizeController {

    @Autowired
    private PrizeService prizeService;

    @Operation(summary = "Get all prizes", description = "Retrieve a list of all prizes")
    @GetMapping
    public ResponseEntity<List<Prize>> getAll(){
        List<Prize> prizeList = prizeService.getAllPrize();
        return new ResponseEntity<>(prizeList, HttpStatus.OK);
    }

    @Operation(summary = "Get prize by ID", description = "Retrieve a prize by its ID")
    @ApiResponse(responseCode = "200", description = "Prize retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Prize not found")
    @GetMapping("/{prizeId}")
    public ResponseEntity<Prize> getPrizeById(@PathVariable Integer prizeId){
        Prize prize = prizeService.getPrizeById(prizeId);
        if (prize != null) {
            return new ResponseEntity<>(prize, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new prize", description = "Create a new prize")
    @ApiResponse(responseCode = "201", description = "Prize created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    public ResponseEntity<String> createPrize(@RequestBody Prize prize){
        String isSuccess = prizeService.createPrize(prize);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("創建成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("創建失敗", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update an existing prize", description = "Update an existing prize by its ID")
    @ApiResponse(responseCode = "200", description = "Prize updated successfully")
    @ApiResponse(responseCode = "404", description = "Prize not found")
    @PutMapping("/{prizeId}")
    public ResponseEntity<String> updatePrize(@PathVariable Integer prizeId, @RequestBody Prize prize){
        Prize existingPrize = prizeService.getPrizeById(prizeId);
        if (existingPrize != null) {
            String isSuccess = prizeService.updatePrize(prize);
            if ("1".equals(isSuccess)) {
                return new ResponseEntity<>("更新成功", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("更新失敗", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Prize not found", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a prize", description = "Delete a prize by its ID")
    @ApiResponse(responseCode = "200", description = "Prize deleted successfully")
    @ApiResponse(responseCode = "404", description = "Prize not found")
    @DeleteMapping("/{prizeId}")
    public ResponseEntity<String> deletePrize(@PathVariable Integer prizeId){
        String isSuccess = prizeService.deletePrize(prizeId);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("刪除成功", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("刪除失敗", HttpStatus.BAD_REQUEST);
        }
    }
}
