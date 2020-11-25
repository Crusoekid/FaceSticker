package com.stickers.encoder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class RectEncoder extends DrawEncoder{
    private boolean drawRect = true;
    private int frameWidth;
    private int frameHeight;

    private final Paint rectPaint = new Paint();
    private Rect[] trackedObjects;

    public RectEncoder(final Context context) {
        rectPaint.setColor(Color.RED);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(2.0f);
    }

    @Override
    public synchronized void setFrameConfiguration(final int width, final int height) {
        if (!drawRect) return;
        frameWidth = width;
        frameHeight = height;
    }

    @Override
    public synchronized void draw(final Canvas canvas) {
        if (!drawRect) return;
        if (trackedObjects == null || trackedObjects.length == 0) return;

        for (int i = 0; i < trackedObjects.length; i++) {
            canvas.drawRect(trackedObjects[i], rectPaint);
        }
    }

    @Override
    void processResults(String tag, Object object1) {
        if (!drawRect || object1 == null) return;
        if (tag.equals("rect")) {
            trackedObjects = (Rect[])object1;
        }
    }

    @Override
    void processResults(String tag, Object object1, Object object2) {

    }
}
