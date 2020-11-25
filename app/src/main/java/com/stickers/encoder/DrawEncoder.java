package com.stickers.encoder;

import android.graphics.Canvas;

public abstract class DrawEncoder {
    abstract void setFrameConfiguration(final int width, final int height);

    abstract void draw(final Canvas canvas);

    abstract void processResults( String tag, Object object1);

    abstract void processResults( String tag, Object object1, Object object2);
}
