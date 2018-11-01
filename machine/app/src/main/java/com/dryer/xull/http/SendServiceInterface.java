package com.dryer.xull.http;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by xll on 2016/10/17.
 */

public interface SendServiceInterface {
    /**
     * 注册设备号
     */
    @PUT("api/device")
    Observable<ResponseBody>device(@Body RequestBody jsonParams);

    /**
     * 文件上传
     */
    @POST("http://superimage.huaeryun.com/api/image/upload")
    Observable<ResponseBody>uploadFiles(@Body MultipartBody body);
    /**
     * 文件下载
     */
    @GET
    Observable<ResponseBody>downLoadFiles(@Url String fileUrl);
  }
