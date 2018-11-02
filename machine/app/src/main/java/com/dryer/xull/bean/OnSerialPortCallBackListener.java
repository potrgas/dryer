package com.dryer.xull.bean;

import com.dryer.xull.utils.OnSerialPortCallback;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

abstract public class OnSerialPortCallBackListener {
    public Timer timer;
    public TimerTask timerTask;

    public OnSerialPortCallBackListener(){
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                OnFail();
            }
        };
        timer.schedule(timerTask,10000);
    }
    public void OnSuccess(List<Byte>data){
        stopTimer();
    };
    abstract public void OnFail();
    public void stopTimer(){

        if(timerTask!=null){
            timerTask.cancel();
            timerTask=null;
        }
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
    }
}
