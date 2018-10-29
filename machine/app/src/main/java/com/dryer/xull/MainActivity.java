package com.dryer.xull;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dryer.xull.activity.TestActivity;
import com.google.zxing.WriterException;
import com.yzq.zxinglibrary.encode.CodeCreator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.iv_setting)
    ImageView btnSeting;
    @Bind(R.id.tv_number)
    TextView tvNumber;
    @Bind(R.id.cb_chunmian)
    CheckBox cbChunmian;
    @Bind(R.id.cb_huaxian)
    CheckBox cbHuaxian;
    @Bind(R.id.cb_other)
    CheckBox cbOther;
    @Bind(R.id.tv_pay)
    TextView tvPay;
    @Bind(R.id.iv_code)
    ImageView ivCode;
    @Bind(R.id.btn_open)
    Button btnOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_setting)
    public void click() {
        startActivity(new Intent(this, TestActivity.class));
    }

    @OnClick(R.id.iv_code)
    public void click1() {
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
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
