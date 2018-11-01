package com.dryer.xull.http;


import com.dryer.xull.R;
import com.dryer.xull.app.SupoffApp;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xll on 2016/10/17.
 */

public class HttpTaskUtils {
    private String BASE_URL;
    private static final int DEFAULT_TIMEOUT=20;
    private Retrofit retrofit;
    private SendServiceInterface myService;

    private HttpTaskUtils(){
        BASE_URL= SupoffApp.appContext.getResources().getString(R.string.base_url);
        OkHttpClient.Builder httpClientBuilder=new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.addNetworkInterceptor(new LogInterceptor());
        retrofit=new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        myService=retrofit.create(  SendServiceInterface.class);
    }

    private static class SingletonHolder{
        private static final HttpTaskUtils INSTANCE=new HttpTaskUtils();
    }
    public static HttpTaskUtils getInstence(){
        return SingletonHolder.INSTANCE;
    }


    public void SetObservable(Observable observable,Subscriber subscriber){
         observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 登录
     */
    public void device(Subscriber<ResponseBody>subscriber, RequestBody jsonParams){
        SetObservable(myService.device(jsonParams),subscriber);
    }
    /**
     * 文件上传
     */
    public void UpLoadFiles( Subscriber<ResponseBody>subscriber,MultipartBody body){
        SetObservable(myService.uploadFiles(body),subscriber);
    }
    /**
     * 文件下载
     */
    public void DownLoadFiles( Subscriber<ResponseBody>subscriber,String url){
        SetObservable(myService.downLoadFiles(url),subscriber);
    }

}
