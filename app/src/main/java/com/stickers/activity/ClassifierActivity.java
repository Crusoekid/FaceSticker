package com.stickers.activity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.util.Size;

import com.stickers.R;
import com.stickers.camera.CameraEngine;
import com.stickers.currencyview.OverlayView;
import com.stickers.encoder.BitmapEncoder;
import com.stickers.encoder.CircleEncoder;
import com.stickers.encoder.EncoderBus;
import com.stickers.encoder.RectEncoder;
import com.stickers.encoder.StickerEncoder;
import com.tenginekit.KitCore;
import com.tenginekit.face.Face;
import com.tenginekit.face.FaceDetectInfo;
import com.tenginekit.face.FaceLandmarkInfo;
import com.tenginekit.model.TenginekitPoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ClassifierActivity extends CameraActivity {

    private static final String TAG = "ClassifierActivity";

    private OverlayView trackingOverlay;
    private Bitmap earBitmap;

    @Override
    protected int getLayoutId() {
        return R.layout.camera_connection_fragment;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return new Size(1280, 960);
    }

    public void Registe() {
        /**
         * canvas 绘制人脸框，人脸关键点
         * */
        EncoderBus.GetInstance().Registe(new BitmapEncoder(this));
        EncoderBus.GetInstance().Registe(new CircleEncoder(this));
        EncoderBus.GetInstance().Registe(new RectEncoder(this));
        EncoderBus.GetInstance().Registe(new StickerEncoder(this));
    }

    @Override
    public void onPreviewSizeChosen(final Size size) {
        Registe();
        earBitmap = getImageFromAssetsFile("ear/ear_000.png");
        EncoderBus.GetInstance().onSetFrameConfiguration(previewHeight, previewWidth);

        trackingOverlay = (OverlayView) findViewById(R.id.facing_overlay);
        trackingOverlay.addCallback(new OverlayView.DrawCallback() {
            @Override
            public void drawCallback(final Canvas canvas) {
                EncoderBus.GetInstance().onDraw(canvas);
            }
        });
    }

    @Override
    protected void processImage() {
        if (sensorEventUtil != null) {
            getCameraBytes();
            int degree = CameraEngine.getInstance().getCameraOrientation(sensorEventUtil.orientation);
            /**
             * 设置旋转角
             */
            KitCore.Camera.setRotation(degree - 90, false, (int) CameraActivity.ScreenWidth, (int) CameraActivity.ScreenHeight);

            /**
             * 获取人脸信息
             */
            Face.FaceDetect faceDetect = Face.detect(mNV21Bytes);
            List<FaceDetectInfo> faceDetectInfos = new ArrayList<>();
            List<FaceLandmarkInfo> landmarkInfos = new ArrayList<>();
            if (faceDetect.getFaceCount() > 0) {
                faceDetectInfos = faceDetect.getDetectInfos();
                landmarkInfos = faceDetect.landmark2d();
            }
            Log.d("#####", "processImage: " + faceDetectInfos.size());
            if (faceDetectInfos != null && faceDetectInfos.size() > 0) {
                Rect[] face_rect = new Rect[faceDetectInfos.size()];

                List<List<TenginekitPoint>> face_landmarks = new ArrayList<>();
                for (int i = 0; i < faceDetectInfos.size(); i++) {
                    Rect rect = new Rect();
                    rect = faceDetectInfos.get(i).asRect();
                    face_rect[i] = rect;
                    face_landmarks.add(landmarkInfos.get(i).landmarks);
                }
//                EncoderBus.GetInstance().onProcessResults("rect", face_rect);
//                EncoderBus.GetInstance().onProcessResults("landmark", face_landmarks);
                EncoderBus.GetInstance().onProcessResults("sticker", earBitmap, face_landmarks.get(0));
            }
        }

        runInBackground(new Runnable() {
            @Override
            public void run() {
                readyForNextImage();
                if (trackingOverlay != null) {
                    trackingOverlay.postInvalidate();
                }
            }
        });
    }

    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}