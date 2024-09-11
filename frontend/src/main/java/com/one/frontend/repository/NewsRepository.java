package com.one.frontend.repository;

import com.one.frontend.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NewsRepository {

    // 查询所有新闻
    @Select("SELECT * FROM news")
    List<News> getAllNews();

    // 根据ID查询新闻
    @Select("SELECT * FROM news WHERE news_uid = #{newsUid}")
    News getNewsById(String newsUid);

}
