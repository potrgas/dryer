package com.dryer.xull.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dryer.xull.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OpenDoorDryActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.tv_device_code)
    TextView tvDeviceCode;
    @Bind(R.id.tv_start)
    TextView tvStart;
    @Bind(R.id.tv_opendoor)
    TextView tvOpendoor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_door_dry);
        ButterKnife.bind(this);
        tvStart.setOnClickListener(this);
        tvOpendoor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
