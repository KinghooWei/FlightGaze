package com.xwlab.blacklist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.madgaze.smartglass.otg.AudioCallback;
import com.madgaze.smartglass.otg.CameraHelper;
import com.madgaze.smartglass.otg.RecordVideoCallback;
import com.madgaze.smartglass.otg.SplitCamera;
import com.madgaze.smartglass.otg.SplitCameraCallback;
import com.madgaze.smartglass.otg.TakePictureCallback;
import com.madgaze.smartglass.view.SplitCameraView;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;

public class WorkActivity extends AppCompatActivity {

    private static final String TAG = "WorkActivity";
    private MyApplication myApplication;

    private Face face;
    private boolean workFlag = false;
    String[] RequiredPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private SplitCameraView cameraView;
    private ImageView ivFace;
    private TextView tvName;
    private TextView tvIdentifyID;
    private TextView tvGender;
    private TextView tvAge;
    private TextView tvType;
    private TextView tvCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        myApplication = MyApplication.getInstance();

        init();
        face = new Face();
        face.FaceModelInit(MyApplication.SD_PATH);
    }

    public void init() {
        cameraView = findViewById(R.id.camera_view);
        ivFace = findViewById(R.id.iv_face);
        tvName = findViewById(R.id.tv_name);
        tvIdentifyID = findViewById(R.id.tv_identify_id);
        tvGender = findViewById(R.id.tv_gender);
        tvAge = findViewById(R.id.tv_age);
        tvType = findViewById(R.id.tv_type);
        tvCase = findViewById(R.id.tv_case);

        setViews();
        if (!permissionReady()) {
            askForPermission();
        } else {
            setVideo();
            setAudio();
        }
    }


    public void askForPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, RequiredPermissions, 100);
        }
    }

    public boolean permissionReady() {
        ArrayList<String> PermissionsMissing = new ArrayList();

        for (int i = 0; i < RequiredPermissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, RequiredPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                PermissionsMissing.add(RequiredPermissions[i]);
            }
        }
        if (PermissionsMissing.size() > 0) {
            MDToast.makeText(this, String.format("Permission [%s] not allowed, please allows in the Settings.", String.join(", ", PermissionsMissing)), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
            return false;
        }
        return true;
    }


    public void setViews() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_on_off:
                        toggleCameraOnOffBtn();
                        break;
                    case R.id.btn_picture:
                        findViewById(R.id.btn_picture).setEnabled(false);
                        takePicture();
                        break;
                    case R.id.btn_vedio:
                        findViewById(R.id.btn_vedio).setEnabled(false);
                        toogleVideoBtn();
                        break;
                }
            }
        };

        (findViewById(R.id.btn_on_off)).setOnClickListener(listener);
        (findViewById(R.id.btn_picture)).setOnClickListener(listener);
        (findViewById(R.id.btn_vedio)).setOnClickListener(listener);

        (findViewById(R.id.btn_picture)).setEnabled(false);
        (findViewById(R.id.btn_vedio)).setEnabled(false);
    }

    public void setVideo() {
        SplitCamera.getInstance(this).setFrameFormat(CameraHelper.FRAME_FORMAT_MJPEG);
        SplitCamera.getInstance(this).setPreviewSize(SplitCamera.CameraDimension.DIMENSION_1280_720);
        SplitCamera.getInstance(this).start(findViewById(R.id.camera_view));

        //////

        /* Insert code segment below if you want to monitor the USB Camera connection status */
        SplitCamera.getInstance(this).setCameraCallback(new SplitCameraCallback() {
            @Override
            public void onConnected() {
                SplitCamera.getInstance(WorkActivity.this).startPreview();
                MDToast.makeText(WorkActivity.this, "Camera connected", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
//                updateUI(true);
            }

            @Override
            public void onDisconnected() {
                MDToast.makeText(WorkActivity.this, "Camera disconnected", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
                updateUI(false);
            }

            @Override
            public void onError(int code) {
                if (code == -1)
                    MDToast.makeText(WorkActivity.this, "There is no connecting MAD Gaze Cameras.", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                else
                    MDToast.makeText(WorkActivity.this, "MAD Gaze Camera Init Failure (Error=" + code + ")", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                updateUI(false);
            }
        });

        //////

        /* Insert code segment below if you want to retrieve the video frames in nv21 format */
        SplitCamera.getInstance(this).setOnPreviewFrameListener(new AbstractUVCCameraHandler.OnPreViewResultListener() {
            @Override
            public void onPreviewResult(byte[] bytes) {

            }
        });

        //////

        /* Insert code segment below if you want to record the video */
        SplitCamera.getInstance(this).setRecordVideoCallback(new RecordVideoCallback() {
            @Override
            public void onVideoSaved(String path) {
                MDToast.makeText(WorkActivity.this, "Video saved success in (" + path + ")", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((Button) findViewById(R.id.btn_vedio)).setText("录像");
                        ((findViewById(R.id.btn_vedio))).setEnabled(true);
                    }
                });
            }

            @Override
            public void onError(int code) {
                MDToast.makeText(WorkActivity.this, "Video saved (Error=" + code + ")", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((Button) findViewById(R.id.btn_vedio)).setText("录像");
                        ((findViewById(R.id.btn_vedio))).setEnabled(true);
                    }
                });
            }
        });
    }

    public void setAudio() {
        SplitCamera.getInstance(this).setAudioSamFreq(SplitCamera.SamFreq.SamFreq_48000);//default 48000

        /* Insert code segment below if you want to retrieve the audio data */
        SplitCamera.getInstance(this).setAudioCallback(new AudioCallback() {
            @Override
            public void onAudioReceived(byte[] decodedAudio) {
                //decodedAudio is audio byte data.
            }
        });
    }

    public void toggleCameraOnOffBtn() {
        if (SplitCamera.getInstance(WorkActivity.this).isPreviewStarted()) {
            updateUI(false);
            SplitCamera.getInstance(WorkActivity.this).stopPreview();
            workFlag = false;
            clear();
        } else {
            updateUI(true);
            SplitCamera.getInstance(WorkActivity.this).startPreview();
            workFlag = true;
            new Thread(workRunnable).start();
        }
    }

    Runnable workRunnable = new Runnable() {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (!workFlag) {
                    return;
                }

                final Bitmap image = cameraView.getBitmap();
                if (image == null) {
                    continue;
                }
                int width = image.getWidth();
                int height = image.getHeight();
                //bitmap转字节数组
                byte[] imageData = getPixelsRGBA(image);
                long timeDetectFace = System.currentTimeMillis();
                //实时人脸检测
//                Logger.i(TAG, "实时检测人脸");
                int[] faceInfo = face.FaceDetect(imageData, width, height, 4);
                if (faceInfo[0] == 1) {     //画面中有1张人脸

                    long sysTime = new Date().getTime();
                    //截取人脸
                    final Bitmap faceRect = Bitmap.createBitmap(image, faceInfo[1], faceInfo[2], faceInfo[3] - faceInfo[1], faceInfo[4] - faceInfo[2]);

                    int widthCan = faceRect.getWidth();
                    Paint paint = new Paint();
                    Bitmap circleBitmap = Bitmap.createBitmap(widthCan, widthCan, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(circleBitmap);
                    canvas.drawCircle(widthCan / 2f, widthCan / 2f, width / 2f, paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    canvas.drawBitmap(faceRect, 0, 0, paint);

                    //人脸转字节数组
                    byte[] faceDate = getPixelsRGBA(faceRect);
                    //特征提取
                    String feature = face.FaceFeatureRestore(faceDate, faceRect.getWidth(), faceRect.getHeight());
                    final KeyPersonnel keyPersonnel = myApplication.getFaceDatabase().featureCmp(feature);
                    if (keyPersonnel != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handler.removeMessages(CLEAR);
                                display(keyPersonnel);
                                Message msg = handler.obtainMessage();
                                msg.what = CLEAR;
                                handler.sendMessageDelayed(msg, 2000);
                            }
                        });
                    }
                }
            }
        }
    };

    private void log(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvName.setText(msg);
            }
        });
    }

    //    private static final int DISPLAY = 753;
    private static final int CLEAR = 840;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle data;
            switch (msg.what) {
                case CLEAR:
                    clear();
                    break;
            }
        }
    };

    private void display(KeyPersonnel keyPersonnel) {
        ivFace.setImageBitmap(keyPersonnel.getFace());
        tvName.setText(keyPersonnel.getName());
        tvIdentifyID.setText("身份证：" + keyPersonnel.getIdentifyId());
        tvGender.setText(keyPersonnel.getGender());
        tvAge.setText(keyPersonnel.getAge() + "岁");
        tvType.setText("类型：" + keyPersonnel.getType());
        tvCase.setText("案底：" + keyPersonnel.getCaseFile());
    }

    private void clear() {
        ivFace.setImageBitmap(null);
        tvName.setText(null);
        tvIdentifyID.setText(null);
        tvGender.setText(null);
        tvAge.setText(null);
        tvType.setText(null);
        tvCase.setText(null);
    }

    private byte[] getPixelsRGBA(Bitmap image) {
        int bytes = image.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        image.copyPixelsToBuffer(buffer);
        return buffer.array();
    }

    public void toogleVideoBtn() {
        if (!permissionReady()) return;
        if (SplitCamera.getInstance(this).isRecording()) {
            MDToast.makeText(this, "Stop Recording", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
            SplitCamera.getInstance(this).stopRecording();
            ((Button) (findViewById(R.id.btn_vedio))).setText("录像");
            ((findViewById(R.id.btn_vedio))).setEnabled(true);

        } else {
            MDToast.makeText(this, "Start Recording", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
            SplitCamera.getInstance(this).startRecording();
            ((Button) (findViewById(R.id.btn_vedio))).setText("停止");
            ((findViewById(R.id.btn_vedio))).setEnabled(true);

        }
    }

    public void takePicture() {
        if (!permissionReady()) return;
        SplitCamera.getInstance(this).takePicture(new TakePictureCallback() {
            @Override
            public void onImageSaved(String path) {
                MDToast.makeText(WorkActivity.this, "Image saved success in (" + path + ")", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                ((findViewById(R.id.btn_picture))).setEnabled(true);
            }

            @Override
            public void onError(int code) {
                MDToast.makeText(WorkActivity.this, "Image saved failure (Error=" + code + ")", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                ((findViewById(R.id.btn_picture))).setEnabled(true);
            }
        });
    }

    public void updateUI(boolean on) {
        if (on) {
            (findViewById(R.id.btn_on_off)).setEnabled(true);
            ((Button) findViewById(R.id.btn_on_off)).setText("关闭");
            (findViewById(R.id.btn_vedio)).setEnabled(true);
            (findViewById(R.id.btn_picture)).setEnabled(true);
            ((Button) findViewById(R.id.btn_vedio)).setText("录像");

        } else {
            (findViewById(R.id.btn_on_off)).setEnabled(true);
            ((Button) findViewById(R.id.btn_on_off)).setText("开启");
            (findViewById(R.id.btn_vedio)).setEnabled(false);
            (findViewById(R.id.btn_picture)).setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SplitCamera.getInstance(this).onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SplitCamera.getInstance(this).onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SplitCamera.getInstance(this).onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SplitCamera.getInstance(this).onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SplitCamera.getInstance(this).onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (!permissionReady()) {
                askForPermission();
            } else {
                setVideo();
                setAudio();
            }
        }
    }
}