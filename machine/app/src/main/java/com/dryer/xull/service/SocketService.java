package com.dryer.xull.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.dryer.xull.bean.MessageSendService;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class SocketService extends Service {
    /**
     * 心跳检测时间
     */
    private static final long HEART_BEAT_RATE = 15 * 1000;//每隔15秒进行一次对长连接的心跳检测
    public  static  String WEBSOCKET_HOST_AND_PORT ="wss://dryerservice.leftins.com/websocket/aaaa";//可替换为自己的主机名和端口号
    //public  static  String WEBSOCKET_HOST_AND_PORT ="ws://192.168.8.101:8080/JavaWebSocket/websocket";//可替换为自己的主机名和端口号

    private WebSocket mWebSocket;
    //创建线程池，
    ExecutorService writeExecutor = Executors.newSingleThreadExecutor();
    @Override
    public void onCreate() {
        super.onCreate();
        new InitSocketThread().start();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                initSocket();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 初始化socket
    private void initSocket() throws UnknownHostException, IOException {
        Log.e("ssssss","initSocket====================");
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES).build();
        Request request = new Request.Builder().url(WEBSOCKET_HOST_AND_PORT).build();
        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                Log.e("ssssss","onOpen====================");
                mWebSocket=webSocket;
                //建立连接成功后，发送消息给服务器端
                writeExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //socket 发送信息到服务器
                        mWebSocket.send(new Gson().toJson(new MessageSendService()));

                    }
                });
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                Log.e("ssss",text+"onMessage=================");
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                Log.e("ssss",bytes.hex()+"========ByteString=========");
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                Log.e("ssssss","onClosing========"+code+"==="+reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                //writeExecutor.shutdown();
                Log.e("ssssss","onClosed====="+code+"==="+reason);

            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                Log.e("ssssss","onFailure=======");

            }
        });
        client.dispatcher().executorService().shutdown();
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
    }


    private long sendTime = 0L;
    // 发送心跳包
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                if(mWebSocket==null){
                    return;
                }
                boolean isSuccess = mWebSocket.send(new Gson().toJson(new MessageSendService()));//发送一个空消息给服务器，通过发送消息的成功失败来判断长连接的连接状态
                Log.e("ssss","heartBeat================="+isSuccess);
                if (!isSuccess) {//长连接已断开
                    mHandler.removeCallbacks(heartBeatRunnable);
                    mWebSocket.cancel();//取消掉以前的长连接
                    new InitSocketThread().start();//创建一个新的连接
                } else {//长连接处于连接状态

                }

                sendTime = System.currentTimeMillis();
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);//每隔一定的时间，对长连接进行一次心跳检测
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(heartBeatRunnable);
        if (mWebSocket != null) {
            mWebSocket.close(1000, null);
        }
    }
}
