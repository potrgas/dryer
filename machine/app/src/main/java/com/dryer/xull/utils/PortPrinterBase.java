package com.dryer.xull.utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by WNI10 on 2018/3/31.
 */

public class PortPrinterBase {
    private OutputStream out;
    public PortPrinterBase(OutputStream out){
        this.out = out;
    }
    public static final byte packageHeader_1 = 0x43;
    public static final byte packageHeader_2 = 0x48;

    public static final byte packageLongth_1 = 0x00;
    public static final byte packageLongth_2_sm = 0x0F;
    public static final byte packageLongth_2_lon = 0x41;

    public static final byte mes_out = 0x01;
    public static final byte mes_in = 0x02;

    public static  byte order_num = 0x00;

    public static final byte reservedBit = 0x000000;
    //门状态
    public static final byte cmdDoorState = 0x0010;
    //开门
    public static final byte cmdOpenDoor = 0x0011;
    //是否在烘干
    public static final byte cmdIsDrying = 0x0012;
    //启动烘干
    public static final byte cmdStartDrying = 0x0013;
    //停止烘干
    public static final byte cmdStopDrying = 0x0014;
    //是否进行收纳
    public static final byte cmdIsStorage = 0x0015;
    //启动收纳
    public static final byte cmdStartStorage = 0x0016;
    //关闭收纳
    public static final byte cmdStopStorage = 0x0017;
    //获取设备信息
    public static final byte cmdDeviceInfo = 0x0018;
    //故障信息
    public static final byte cmdErrorInfo = 0x0019;
    //查询管理员状态是否开启
    public static final byte cmdIsManagerOpen = 0x001A;
    //管理员状态 开启
    public static final byte cmdManagerOpen = 0x001B;
    //管理员状态 关闭
    public static final byte cmdIsManagerClose = 0x001C;

    //设置参数
    public static final byte cmdSetParam = 0x0030;
    //查询参数
    public static final byte cmdGetParam = 0x0031;
    //查询设备当前所有状态
    public static final byte cmdGetAllState = 0x0032;


    public void mySend(String str){
        try{
            out.write(str.getBytes());
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
