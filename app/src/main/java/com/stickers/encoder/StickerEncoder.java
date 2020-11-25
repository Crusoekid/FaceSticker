package com.stickers.encoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
            trackedObjects = (Bitmap) object1;
            List<TenginekitPoint> points = (List<TenginekitPoint>) object2;
            marginLeft = points.get(69).X - 130;
            marginTop = points.get(21).Y - 80;
        }
    }
}
