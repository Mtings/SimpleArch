package com.song.sakura.network;

import androidx.lifecycle.LiveData;

import com.network.api.ApiResponse;
import com.song.sakura.entity.response.ArticleBean;
import com.song.sakura.entity.response.BannerVO;
import com.song.sakura.entity.response.HomePageEntity;
import com.song.sakura.entity.response.ProjectTree;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Title: com.song.sakura.network
 * Description:
 * Copyright:Copyright(c) 2020
 * CreateTime:2020/06/24 11:28
 *
 * @author SogZiw
 * @version 1.0
 */
public interface ApiService {

    /**
     * 首页文章列表
     * @param page 页数
     */
    @GET("article/list/{page}/json")
    LiveData<ApiResponse<HomePageEntity>> getHomeArticleList(@Path("page") int page);


    /**
     * 置顶文章
     */
    @GET("article/top/json")
    LiveData<ApiResponse<List<ArticleBean>>> getHomeTopList();

    /**
     * 首页banner
     */
    @GET("banner/json")
    LiveData<ApiResponse<List<BannerVO>>> bannerList();

    /**
     * 项目分类
     */
    @GET("project/tree/json")
    LiveData<ApiResponse<List<ProjectTree>>> projectTree();

}
