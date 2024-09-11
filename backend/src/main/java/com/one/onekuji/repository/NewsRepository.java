package com.one.onekuji.repository;

import com.one.onekuji.model.News;
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

    // 新增新闻
    @Insert("INSERT INTO news (title, preview, createdDate, imageUrl, status) " +
            "VALUES (#{title}, #{preview}, #{createdDate}, #{imageUrl}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertNews(News news);

    // 更新新闻
    @Update("UPDATE news SET title = #{title}, preview = #{preview}, createdDate = #{createdDate}, " +
            "imageUrl = #{imageUrl}, status = #{status} , title = #{title} WHERE news_uid = #{newsUid}")
    int updateNews(News news);

    // 删除新闻
    @Delete("DELETE FROM news WHERE  news_uid = #{newsUid}")
    int deleteNews(String newsUid);
}
