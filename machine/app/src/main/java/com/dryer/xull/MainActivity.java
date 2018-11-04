package com.dryer.xull;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.dryer.xull.http.HttpTaskUtils;
import com.dryer.xull.http.OnSuccessAndFailSub;
import com.dryer.xull.http.ParamsUtils;
import com.dryer.xull.service.SocketService;
import com.dryer.xull.utils.ScreenUtil;
import com.dryer.xull.utils.Utils;
import com.dryer.xull.view.DialogUtils;
import com.dryer.xull.view.TipDialog;

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
        Log.e("ssss", ScreenUtil.getScreenWidth()+"===="+ScreenUtil.getScreenHeight());
    }

//    @OnClick(R.id.iv_setting)
//    public void click() {
//        startActivity(new Intent(this, TestActivity.class));
//    }

//    @OnClick(R.id.iv_code)
//    public void click1() {
//        try {
//            /*
//             * contentEtString：字符串内容
//             * w：图片的宽
//             * h：图片的高
//             * logo：不需要logo的话直接传null
//             * */
//
//            // Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//            Bitmap bitmap = CodeCreator.createQRCode("http://www.baidu.com", 300, 300, null);
//            ivCode.setImageBitmap(bitmap);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//
//    }

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
        }
    }

    public void showDialog() {
        if (tipDialog == null) {
            tipDialog = new TipDialog(this);
        }
        tipDialog.show();
    }

    PopupWindow pop;

    public void showPopup() {
        if (pop == null) {
            pop = DialogUtils.createPopupWindow_xull(this, new DialogUtils.OnPopupClickListener() {
                @Override
                public void onPopupClick(int position,int index) {
                    if (pop != null) {
                        if (DialogUtils.ll_popup_ != null) {
                            DialogUtils.ll_popup_.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.activity_translate_out));
                        }
                        pop.dismiss();
                    }
                    switch (position) {
                        case 1:

                            break;
                    }

                }
            });
        }
        if (DialogUtils.ll_popup_ != null) {
            DialogUtils.ll_popup_.startAnimation(AnimationUtils.loadAnimation(this, R.anim.activity_translate_in));
        }
        pop.showAtLocation(bootView, Gravity.BOTTOM, 0, 0);
    }
}
