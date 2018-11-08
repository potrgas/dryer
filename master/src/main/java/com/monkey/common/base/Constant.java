package com.monkey.common.base;

import org.aspectj.apache.bcel.classfile.Unknown;
import org.omg.CORBA.UNKNOWN;

import java.util.HashSet;
import java.util.Set;

/**
 * @author liugh
 * @since 2018-05-03
 */
public class Constant {


    public static final int BYTE_BUFFER = 1024;

    public static  final String UnknownCode="wwwwwwwwwwwwwwww";
    public static Set<String>  METHOD_URL_SET = new HashSet<>();


    //小程序唯一标识   (在微信小程序管理后台获取)
   public static String wxspAppid = "wx3462f1fc533ebe71";
    //小程序的 app secret (在微信小程序管理后台获取)
    public static  String wxspSecret = "a3f39a8d450d822d232eb9ae2c43fc78";
    //授权（必填）
    public static String grant_type = "authorization_code";
    /**
     * 用户注册默认角色
     */
    public static final int DEFAULT_REGISTER_ROLE = 5;

    public static final int BUFFER_MULTIPLE = 10;

    //启用
    public static final int ENABLE = 1;
    //禁用
    public static final int DISABLE = 0;

    public static class FilePostFix{
        public static final String ZIP_FILE =".zip";

        public static final String [] IMAGES ={"jpg", "jpeg", "JPG", "JPEG", "gif", "GIF", "bmp", "BMP", "png"};
        public static final String [] ZIP ={"ZIP","zip","rar","RAR"};
        public static final String [] VIDEO ={"mp4","MP4","mpg","mpe","mpa","m15","m1v", "mp2","rmvb"};
        public static final String [] APK ={"apk","exe"};
        public static final String [] OFFICE ={"xls","xlsx","docx","doc","ppt","pptx"};

    }
    public class FileType{
        public static final int FILE_IMG = 1;
        public static final int FILE_ZIP = 2;
        public static final int FILE_VEDIO= 3;
        public static final int FILE_APK = 4;
        public static final int FIVE_OFFICE = 5;
        public static final String FILE_IMG_DIR= "/img/";
        public static final String FILE_ZIP_DIR= "/zip/";
        public static final String FILE_VEDIO_DIR= "/video/";
        public static final String FILE_APK_DIR= "/apk/";
        public static final String FIVE_OFFICE_DIR= "/office/";
    }


}
