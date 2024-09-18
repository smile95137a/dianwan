package com.one.onekuji.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.one.onekuji.model.ApiResponse;
import com.one.onekuji.model.News;
import com.one.onekuji.service.NewsService;
import com.one.onekuji.util.ImageUtil;
import com.one.onekuji.util.ResponseUtils;
import com.one.onekuji.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<News>>> getAllNews() {
        try {
            List<News> newsList = newsService.getAllNews();
            ApiResponse<List<News>> response = ResponseUtils.success(200, null, newsList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<News>> response = ResponseUtils.failure(500, "获取新闻列表失败", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{newsUid}")
    public ResponseEntity<ApiResponse<News>> getNewsById(@PathVariable String newsUid) {
        try {
//            CustomUserDetails userDetails = SecurityUtils.getCurrentUserPrinciple();
            News news = newsService.getNewsById(newsUid);
            if (news != null) {
                ApiResponse<News> response = ResponseUtils.success(200, null, news);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<News> response = ResponseUtils.failure(404, "新闻不存在", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<News> response = ResponseUtils.failure(500, "获取新闻失败", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createNews(@RequestPart("newsReq") String news,
                                                        @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
//            // 获取当前用户ID
//            CustomUserDetails userDetails = SecurityUtils.getCurrentUserPrinciple();
//            Long id = userDetails.getId();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS , true);
            News newsReq = objectMapper.readValue(news, News.class);
            List<String> fileUrls = new ArrayList<>();
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String fileUrl = ImageUtil.upload(image); // 使用 ImageUtil 上传文件
                        fileUrls.add(fileUrl);
                    }
                }
            }
            newsReq.setImageUrls(fileUrls);
            int result = newsService.insertNews(newsReq);
            if (result > 0) {
                ApiResponse<Void> response = ResponseUtils.success(201, "新闻创建成功", null);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                ApiResponse<Void> response = ResponseUtils.failure(400, "新闻创建失败", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<Void> response = ResponseUtils.failure(500, "创建新闻时发生错误", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{newsUid}")
    public ResponseEntity<ApiResponse<Void>> updateNews(@PathVariable String newsUid , @RequestPart("newsReq") String news,
                                                        @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            // 获取当前用户ID
//            CustomUserDetails userDetails = SecurityUtils.getCurrentUserPrinciple();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS , true);
            News newsReq = objectMapper.readValue(news, News.class);
            List<String> fileUrls = new ArrayList<>();
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String fileUrl = ImageUtil.upload(image); // 使用 ImageUtil 上传文件
                        fileUrls.add(fileUrl);
                    }
                }
            }
            newsReq.setImageUrls(fileUrls);
            int result = newsService.updateNews(newsUid , newsReq);
                ApiResponse<Void> response = ResponseUtils.success(200, "新闻更新成功", null);
                return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<Void> response = ResponseUtils.failure(500, "更新新闻时发生错误", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{newsUid}")
    public ResponseEntity<ApiResponse<Void>> deleteNews(@PathVariable String newsUid) {
        try {
            // 获取当前用户ID
            var userDetails = SecurityUtils.getCurrentUserPrinciple();
            int result = newsService.deleteNews(newsUid);
            if (result > 0) {
                ApiResponse<Void> response = ResponseUtils.success(200, "新闻删除成功", null);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Void> response = ResponseUtils.failure(400, "新闻删除失败", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            ApiResponse<Void> response = ResponseUtils.failure(500, "删除新闻时发生错误", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}