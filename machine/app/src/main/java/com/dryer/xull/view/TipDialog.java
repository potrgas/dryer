package com.dryer.xull.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import com.dryer.xull.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义的AlertDialog
 */
public class TipDialog extends Dialog implements View.OnClickListener {

	private Context context;
    private Banner banner;
    private RelativeLayout bootView;
    public TipDialog(@NonNull Context context) {
        super(context);
    }
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tip_dialog);
        banner=findViewById(R.id.banner);
        bootView=findViewById(R.id.TipDialog);
        bootView.setOnClickListener(this);
        List<Integer>images=new ArrayList<>();
        images.add(R.mipmap.ic_baner_1);
        images.add(R.mipmap.ic_banner_2);
        banner.setImages(images).setImageLoader(new GlideImageLoader()).start();
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
