package com.dryer.xull.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dryer.xull.R;
import com.dryer.xull.app.SupoffApp;

import butterknife.Bind;

public class DialogUtils {
    public static LinearLayout ll_popup_;
    private static LinearLayout llChunMian;
    private static LinearLayout llHuaXian;
    private static LinearLayout llOther;
    private static TextView tvChunMian;
    private static TextView tvHuaxian;
    private static TextView tvOther;
    private static TextView tvBtn;
    private static ImageView ivBack;
    public static int index=0;
    private static View tvTitlePop;


    public interface OnPopupClickListener {
        void onPopupClick(int position,int index);
    }

    public interface OnTipDialogClickListener {
        void onNegativeClick();

        void onPositiveClick();
    }


    public static PopupWindow createPopupWindow_xull(Context context, final OnPopupClickListener onPopupClickListener) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_popupwindows, null);
        llChunMian = view.findViewById(R.id.ll_chunmian);
        llHuaXian = view.findViewById(R.id.ll_huaxian);
        llOther = view.findViewById(R.id.ll_qita);
        tvChunMian = view.findViewById(R.id.tv_cunmian);
        tvHuaxian = view.findViewById(R.id.tv_huaxian);
        tvOther = view.findViewById(R.id.tv_other);
        tvBtn = view.findViewById(R.id.tv_sure_and_pay);
        ivBack = view.findViewById(R.id.iv_pop_back);


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
                if (onPopupClickListener != null) {
                    onPopupClickListener.onPopupClick(0,0);
                }
            }
        });


        ll_popup_ = (LinearLayout) view.findViewById(R.id.ll_popup);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        PopupWindow pop = new PopupWindow(view,
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
                if (onPopupClickListener != null) {
                    onPopupClickListener.onPopupClick(0,0);
                }

            }
        });
        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPopupClickListener != null) {
                    onPopupClickListener.onPopupClick(1,index);
                }
            }
        });
        return pop;
    }
    public static void setState(int index_){
        index=index_;
        llChunMian.setBackgroundResource(R.drawable.shape_corner_grey);
        llHuaXian.setBackgroundResource(R.drawable.shape_corner_grey);
        llOther.setBackgroundResource(R.drawable.shape_corner_grey);
        tvChunMian.setTextColor(ContextCompat.getColor(SupoffApp.appContext,R.color.color_grey));
        tvHuaxian.setTextColor(ContextCompat.getColor(SupoffApp.appContext,R.color.color_grey));
        tvOther.setTextColor(ContextCompat.getColor(SupoffApp.appContext,R.color.color_grey));
        switch (index){
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
