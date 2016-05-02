package com.gw.api;

import com.gw.entity.GankEntity;
import com.gw.entity.base.Response;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gongwen on 2016/4/24.
 */
public interface ApiService {
    //分类数据: http://gank.io.com/api/data/数据类型/请求个数/第几页
    @GET("data/{type}/{PAGE_SIZE}/{page}")
    Observable<Response<List<GankEntity>>> getOneTypeData(
            @Path("type") Enum type,
            @Path("PAGE_SIZE") int pageSize,
            @Path("page") int page);

  /*   //每日数据： http://gank.avosapps.com/api/day/年/月/日
    @GET("day/{year}/{month}/{day}")
    Observable<MultiTypeResultEntity> getOneDayData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);

    //随机数据：http://gank.avosapps.com/api/random/data/分类/个数
    @GET("random/data/{type}/{PAGE_SIZE}")
    Observable<SingleTypeResultEntity> getRandomData(
            @Path("type") String type,
            @Path("PAGE_SIZE") int PAGE_SIZE);*/

}
