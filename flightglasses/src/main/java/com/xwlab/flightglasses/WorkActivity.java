package com.xwlab.flightglasses;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;

import com.lgh.uvccamera.UVCCameraProxy;
import com.lgh.uvccamera.callback.ConnectCallback;

public class WorkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        TextureView ttvPreview = findViewById(R.id.ttv_preview);
        UVCCameraProxy mUVCCamera = new UVCCameraProxy(this);
        mUVCCamera.setPreviewTexture(ttvPreview);
        mUVCCamera.setConnectCallback(new ConnectCallback() {
            @Override
            public void onAttached(UsbDevice usbDevice) {
                mUVCCamera.requestPermission(usbDevice); // USB设备授权
                Toast.makeText(WorkActivity.this,"USB设备授权", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGranted(UsbDevice usbDevice, boolean granted) {
                if (granted) {
                    mUVCCamera.connectDevice(usbDevice); // 连接USB设备
                    Toast.makeText(WorkActivity.this,"连接USB设备", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onConnected(UsbDevice usbDevice) {
                mUVCCamera.openCamera(); // 打开相机
                Toast.makeText(WorkActivity.this,"打开相机", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCameraOpened() {
                mUVCCamera.setPreviewSize(640, 480); // 设置预览尺寸
                mUVCCamera.startPreview(); // 开始预览
                Toast.makeText(WorkActivity.this,"开始预览", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDetached(UsbDevice usbDevice) {
                mUVCCamera.closeCamera(); // 关闭相机
                Toast.makeText(WorkActivity.this,"关闭相机", Toast.LENGTH_SHORT).show();
            }
        });
        mUVCCamera.startPreview();
    }
}