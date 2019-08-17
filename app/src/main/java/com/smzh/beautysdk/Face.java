package com.smzh.beautysdk;

import android.graphics.PointF;
import android.graphics.RectF;

public class Face {

    private RectF rect;
    private PointF[] facePoints;

    public void setRect(RectF rect) {
        this.rect = rect;
    }

    public void setFacePoints(PointF[] facePoints) {
        this.facePoints = facePoints;
    }

    public PointF[] getFacePoints() {
        return facePoints;
    }

    public RectF getRect() {
        return rect;
    }
}
