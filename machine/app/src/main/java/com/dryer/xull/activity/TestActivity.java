package com.dryer.xull.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dryer.xull.R;
import com.dryer.xull.utils.LockerPortInterface;
import com.dryer.xull.utils.LockerSerialportUtil;
import com.dryer.xull.utils.PortPrinterBase;

import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity implements View.OnClickListener, LockerPortInterface {

    @BindView(R.id.open)
    Button open;
    @BindView(R.id.close)
    Button close;
    @BindView(R.id.send)
    Button send;
    /**
     * 波特率
     */
    private int BAUDRATE = 9600;
    /**
     * 输出流，向串口发送指令
     */
    private OutputStream mOutputStream;
    private PortPrinterBase portPrinterBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        open.setOnClickListener(this);
        close.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open:
                openPort();
                break;
            case R.id.close:
                closePort();
                break;
            case R.id.send:
                send();
                break;
        }
    }

    /**
     * 打开串口
     */
    private void openPort(){
        String path = "/dev/ttyS1";
        if("".equals(path)){
            Toast.makeText(this,"请选择串口号",Toast.LENGTH_SHORT).show();
            return;
        }
        LockerSerialportUtil.init(this,path,BAUDRATE,this);
    }

    /**
     * 关闭串口
     */
    private void closePort(){
        LockerSerialportUtil.getInstance().closeSerialPort();
    }
    private void send(){
        String text = "1111111111";
        portPrinterBase.mySend(text);
    }

    @Override
    public void onLockerDataReceived(byte[] buffer, int size, String path) {
        final String result = new String(buffer,0,size);
        Log.e("ssss","onLockerDataReceived===="+result);
    }

    @Override
    public void onLockerOutputStream(OutputStream outputStream) {
        this.mOutputStream=outputStream;
        portPrinterBase=new PortPrinterBase(outputStream,"1");
    }
}
