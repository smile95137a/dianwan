package com.one.onekuji.service;

import com.one.onekuji.model.News;
import com.one.onekuji.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    // 获取所有新闻
    public List<News> getAllNews() {
        return newsRepository.getAllNews();
    }

    // 根据ID获取新闻
    public News getNewsById(String newsUid) {
        return newsRepository.getNewsById(newsUid);
    }

    // 新增新闻
    public int insertNews(News news) {
        news.setNewsUid(UUID.randomUUID().toString());
        news.setCreatedDate(LocalDateTime.now());
        return newsRepository.insertNews(news);
    }

    // 更新新闻
    public int updateNews(String newsUid , News news) {
        News the_news = newsRepository.getNewsById(newsUid);
        News req = new News();
        req.setContent(the_news.getContent());
        req.setImageUrl(the_news.getImageUrl());
        req.setTitle(the_news.getTitle());
        req.setPreview(the_news.getPreview());
        req.setUpdatedDate(the_news.getUpdatedDate());
        req.setStatus(the_news.getStatus());
        return newsRepository.updateNews(news);
    }

    // 删除新闻
    public int deleteNews(String newsUid) {
        return newsRepository.deleteNews(newsUid);
    }
}
