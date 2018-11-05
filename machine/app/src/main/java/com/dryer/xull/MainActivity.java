package com.dryer.xull;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.dryer.xull.activity.LoginActivity;
import com.dryer.xull.activity.OpenDoorDryActivity;
import com.dryer.xull.app.SupoffApp;
import com.dryer.xull.http.HttpTaskUtils;
import com.dryer.xull.http.OnSuccessAndFailSub;
import com.dryer.xull.http.ParamsUtils;
import com.dryer.xull.service.SocketService;
import com.dryer.xull.utils.ScreenUtil;
import com.dryer.xull.utils.Utils;
import com.dryer.xull.view.DialogUtils;
import com.dryer.xull.view.TipDialog;
import com.google.zxing.WriterException;
import com.yzq.zxinglibrary.encode.CodeCreator;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Bind(R.id.tv_device_code)
    TextView tvDeviceCode;
    @Bind(R.id.tv_tab_1)
    TextView tvTab1;
    @Bind(R.id.rl_tab1_line)
    RelativeLayout rlTab1Line;
    @Bind(R.id.rl_tab1)
    RelativeLayout rlTab1;
    @Bind(R.id.tv_tab_2)
    TextView tvTab2;
    @Bind(R.id.rl_tab2_line)
    RelativeLayout rlTab2Line;
    @Bind(R.id.rl_tab2)
    RelativeLayout rlTab2;
    @Bind(R.id.tv_tab_3)
    TextView tvTab3;
    @Bind(R.id.rl_tab3_line)
    RelativeLayout rlTab3Line;
    @Bind(R.id.rl_tab3)
    RelativeLayout rlTab3;
    @Bind(R.id.video_main)
    VideoView videoMain;
    @Bind(R.id.iv_main_1)
    ImageView ivMain1;
    @Bind(R.id.iv_main_2)
    ImageView ivMain2;
    @Bind(R.id.iv_video_start)
    ImageView ivVideoStart;
    TipDialog tipDialog;
    @Bind(R.id.iv_start_dry)
    ImageView ivStartDry;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.bootView)
    LinearLayout bootView;
    //popu
    public  LinearLayout ll_popup_;
    private  LinearLayout llChunMian;
    private  LinearLayout llHuaXian;
    private  LinearLayout llOther;
    private  TextView tvChunMian;
    private  TextView tvHuaxian;
    private  TextView tvOther;
    private  TextView tvBtn;
    private  ImageView ivBack;
    public  int indexSelete=0;
    private PopupWindow pop;
    private TextView tvTitlePop;
    private RelativeLayout rlCode;
    private LinearLayout llTypeSelect;
    private ImageView ivCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getDeviceCode();
        startService(new Intent(this, SocketService.class));
        setTabState(0);
        rlTab1.setOnClickListener(this);
        rlTab2.setOnClickListener(this);
        rlTab3.setOnClickListener(this);
        ivVideoStart.setOnClickListener(this);
        ivMain1.setOnClickListener(this);
        ivStartDry.setOnClickListener(this);
        ivSetting.setOnClickListener(this);

        Log.e("ssss", ScreenUtil.getScreenWidth()+"===="+ScreenUtil.getScreenHeight());
    }

//    @OnClick(R.id.iv_setting)
//    public void click() {
//        startActivity(new Intent(this, TestActivity.class));
//    }


    public void getUrlCode() {
        try {
            /*
             * contentEtString：字符串内容
             * w：图片的宽
             * h：图片的高
             * logo：不需要logo的话直接传null
             * */

            // Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            Bitmap bitmap = CodeCreator.createQRCode("http://www.baidu.com", 300, 300, null);
            ivCode.setImageBitmap(bitmap);
            ivCode.setOnClickListener(this);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    //获取设备编号
    public void getDeviceCode() {
        Map<String, String> params = new HashMap<>();
        params.put("deviceName", Utils.getPhoneSign());
        params.put("deviceNum", Utils.getPhoneSign());
        RequestBody requestBody = ParamsUtils.paramsObjectToRequestBody(params);
        HttpTaskUtils.getInstence().device(new OnSuccessAndFailSub(1,
                new OnSuccessAndFailSub.OnHttpResquestCallBack() {
                    @Override
                    public void OnSuccessResult(int requestCode, String data) {

                    }

                    @Override
                    public void OnFailResult(int requestCode, String errorMsg) {

                    }
                }), requestBody);
    }

    public void setTabState(int index) {
        rlTab1.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
        rlTab2.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
        rlTab3.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
        tvTab1.setTextColor(ContextCompat.getColor(this, R.color.color_black));
        tvTab2.setTextColor(ContextCompat.getColor(this, R.color.color_black));
        tvTab3.setTextColor(ContextCompat.getColor(this, R.color.color_black));
        rlTab1Line.setVisibility(View.GONE);
        rlTab2Line.setVisibility(View.GONE);
        rlTab3Line.setVisibility(View.GONE);
        if (index == 0) {
            rlTab1.setBackgroundColor(ContextCompat.getColor(this, R.color.color_bg));
            tvTab1.setTextColor(ContextCompat.getColor(this, R.color.color_blue));
            rlTab1Line.setVisibility(View.VISIBLE);
        } else if (index == 1) {
            rlTab2.setBackgroundColor(ContextCompat.getColor(this, R.color.color_bg));
            tvTab2.setTextColor(ContextCompat.getColor(this, R.color.color_blue));
            rlTab2Line.setVisibility(View.VISIBLE);
        } else {
            rlTab3.setBackgroundColor(ContextCompat.getColor(this, R.color.color_bg));
            tvTab3.setTextColor(ContextCompat.getColor(this, R.color.color_blue));
            rlTab3Line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, SocketService.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_tab1:
                setTabState(0);
                break;
            case R.id.rl_tab2:
                setTabState(1);
                break;
            case R.id.rl_tab3:
                setTabState(2);
                break;
            case R.id.iv_video_start:
                videoMain.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.test));
                videoMain.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        videoMain.start();
                    }
                });
                break;
            case R.id.iv_main_1:
                showDialog();
                break;
            case R.id.iv_start_dry:
                showPopup();
                break;
            case R.id.iv_setting:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.iv_code:
                startActivity(new Intent(this, OpenDoorDryActivity.class));
                break;
        }
    }

    public void showDialog() {
        if (tipDialog == null) {
            tipDialog = new TipDialog(this);
        }
        tipDialog.show();
    }

    public void showPopup() {
        if (pop == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_popupwindows, null);
            llChunMian = view.findViewById(R.id.ll_chunmian);
            llHuaXian = view.findViewById(R.id.ll_huaxian);
            llOther = view.findViewById(R.id.ll_qita);
            tvChunMian = view.findViewById(R.id.tv_cunmian);
            tvHuaxian = view.findViewById(R.id.tv_huaxian);
            tvOther = view.findViewById(R.id.tv_other);
            tvBtn = view.findViewById(R.id.tv_sure_and_pay);
            ivBack = view.findViewById(R.id.iv_pop_back);
            tvTitlePop = view.findViewById(R.id.tv_title_pop);
            rlCode = view.findViewById(R.id.rl_code);
            llTypeSelect = view.findViewById(R.id.ll_type_select);
            ivCode =view.findViewById(R.id.iv_code);
            llChunMian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setState(0);
                }
            });
            llHuaXian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setState(1);
                }
            });
            llOther.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setState(2);
                }
            });
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DialogUtils.ll_popup_ != null) {
                        DialogUtils.ll_popup_.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.activity_translate_out));
                    }
                    pop.dismiss();
                }
            });


            ll_popup_ = (LinearLayout) view.findViewById(R.id.ll_popup);
            // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
            pop = new PopupWindow(view,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
            pop.setFocusable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x33000000);
            pop.setBackgroundDrawable(dw);

            RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
            parent.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (DialogUtils.ll_popup_ != null) {
                        DialogUtils.ll_popup_.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.activity_translate_out));
                    }
                    pop.dismiss();

                }
            });
            tvBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (DialogUtils.ll_popup_ != null) {
//                        DialogUtils.ll_popup_.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.activity_translate_out));
//                    }
//                    pop.dismiss();

                    llTypeSelect.setVisibility(View.GONE);
                    tvBtn.setVisibility(View.GONE);
                    rlCode.setVisibility(View.VISIBLE);
                    tvTitlePop.setText("请扫码付款");
                    getUrlCode();
                }
            });
        }
        if (DialogUtils.ll_popup_ != null) {
            DialogUtils.ll_popup_.startAnimation(AnimationUtils.loadAnimation(this, R.anim.activity_translate_in));
        }
        indexSelete=0;
        llTypeSelect.setVisibility(View.VISIBLE);
        tvBtn.setVisibility(View.VISIBLE);
        rlCode.setVisibility(View.GONE);
        tvTitlePop.setText("请选择织物类型");
        pop.showAtLocation(bootView, Gravity.BOTTOM, 0, 0);
    }

    public  void setState(int index_){
        indexSelete=index_;
        llChunMian.setBackgroundResource(R.drawable.shape_corner_grey);
        llHuaXian.setBackgroundResource(R.drawable.shape_corner_grey);
        llOther.setBackgroundResource(R.drawable.shape_corner_grey);
        tvChunMian.setTextColor(ContextCompat.getColor(SupoffApp.appContext,R.color.color_grey));
        tvHuaxian.setTextColor(ContextCompat.getColor(SupoffApp.appContext,R.color.color_grey));
        tvOther.setTextColor(ContextCompat.getColor(SupoffApp.appContext,R.color.color_grey));
        switch (index_){
            case 0:
                llChunMian.setBackgroundResource(R.drawable.shape_corner_blue_little);
                tvChunMian.setTextColor(ContextCompat.getColor(SupoffApp.appContext,R.color.color_blue_2));

                break;
            case 1:

                llHuaXian.setBackgroundResource(R.drawable.shape_corner_blue_little);
                tvHuaxian.setTextColor(ContextCompat.getColor(SupoffApp.appContext,R.color.color_blue_2));

                break;
            case 2:
                llOther.setBackgroundResource(R.drawable.shape_corner_blue_little);
                tvOther.setTextColor(ContextCompat.getColor(SupoffApp.appContext,R.color.color_blue_2));

                break;
        }
    }
}
