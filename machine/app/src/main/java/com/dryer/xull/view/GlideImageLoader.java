package com.dryer.xull.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoaderInterface;

class GlideImageLoader implements ImageLoaderInterface {


    @Override
    public void displayImage(Context context, Object path, View imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).into((ImageView) imageView);
    }

    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
    @Override
    public ImageView createImageView(Context context) {
        //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView

        return null;
    }

}
