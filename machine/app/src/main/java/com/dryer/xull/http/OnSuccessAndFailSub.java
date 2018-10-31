package com.dryer.xull.http;

import android.widget.Toast;

import com.dryer.xull.app.SupoffApp;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLHandshakeException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by xll on 2016/10/31.
 */

public class OnSuccessAndFailSub extends Subscriber<ResponseBody> {
    /**
     * 请求码
     */
    protected int requestCode;
    /**
     * 请求回调
     */
    protected OnHttpResquestCallBack callBack;
    /**
     * 构造方法
     * @param requestCode
     * @param callBack
     */
    public OnSuccessAndFailSub(int requestCode, OnHttpResquestCallBack callBack){
        this.requestCode=requestCode;
        this.callBack=callBack;
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {//请求超时
            Toast.makeText(SupoffApp.appContext,"请求超时",Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {//网络连接超时
            Toast.makeText(SupoffApp.appContext,"网络连接超时",Toast.LENGTH_SHORT).show();

        } else if (e instanceof SSLHandshakeException) {//安全证书异常
            Toast.makeText(SupoffApp.appContext,"安全证书异常",Toast.LENGTH_SHORT).show();

        } else if (e instanceof HttpException) {//请求的地址不存在
            Toast.makeText(SupoffApp.appContext,"网络请求失败",Toast.LENGTH_SHORT).show();
        } else  {//域名解析失败
            Toast.makeText(SupoffApp.appContext,"网络请求失败",Toast.LENGTH_SHORT).show();

        }
        Logger.e(e.getMessage());
        callBack.OnFailResult(requestCode,"");
    }

    @Override
    public void onNext(ResponseBody body) {
        try {
            final String result = body.string();
            Logger.e(result);
            JSONObject jsonObject = new JSONObject(result);
            int resultCode = jsonObject.optInt("success");
            if (resultCode>0){
                callBack.OnSuccessResult(requestCode,result);
            } else {
                String errorMsg = jsonObject.getString("error");
                callBack.OnFailResult(requestCode,errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("JSON解析出错");
            callBack.OnFailResult(requestCode,"");
        }
    }


    public interface OnHttpResquestCallBack{
        void OnSuccessResult(int requestCode, String data);
        void OnFailResult(int requestCode, String errorMsg);
    }
}
