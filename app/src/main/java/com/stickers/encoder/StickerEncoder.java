package com.stickers.encoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.tenginekit.model.TenginekitPoint;

import java.util.List;

public class StickerEncoder extends DrawEncoder {
    private boolean drawSticker = true;
    private int mWidth = 0;
    private int mHeight = 0;

    private float marginLeft = 0;
    private float marginTop = 0;

    private final Paint stickerPaint = new Paint();
    private Bitmap trackedObjects;


    public StickerEncoder(final Context context) {
        stickerPaint.setColor(Color.RED);
        stickerPaint.setStyle(Paint.Style.STROKE);
        stickerPaint.setStrokeWidth(2.0f);
    }

    @Override
    void setFrameConfiguration(int width, int height) {
        if (!drawSticker) return;
        mWidth = width;
        mHeight = height;
    }

    @Override
    void draw(Canvas canvas) {
        if (!drawSticker) return;
        if (trackedObjects == null) return;
        canvas.drawBitmap(trackedObjects, marginLeft, marginTop, stickerPaint);
    }

    @Override
    void processResults(String tag, Object object1) {
//        if (!drawSticker || object1 == null) return;
//        if (tag.equals("sticker")) {
//            trackedObjects = (Bitmap) object1;
//        }
    }

    //452 , 150
    @Override
    void processResults(String tag, Object object1, Object object2) {
        if (!drawSticker || object1 == null) return;
        if (tag.equals("sticker")) {
            Bitmap stick = (Bitmap) object1;
            List<TenginekitPoint> points = (List<TenginekitPoint>) object2;

            int faceDiff = (int)(points.get(0).X - points.get(68).X);
            int faceWidth = (int)(faceDiff * 1.4f);
            int faceHeight = (int)(150.0f * 1.0f / 452.0f * faceWidth);

            int width = stick.getWidth();
            int height = stick.getHeight();

            float scaleWidth = ((float) faceWidth) / width;
            float scaleHeight = ((float) faceHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            trackedObjects = Bitmap.createBitmap(stick, 0, 0, width, height, matrix, true);
            marginLeft = points.get(68).X -  faceDiff * 0.2f;
            marginTop = points.get(21).Y - faceHeight* 0.8f;
        }
    }
}
