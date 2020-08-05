package com.sxw.baseui.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;


/**
 * 作者：sxw on 2019/6/26 10:44
 */
public class RoundUtil {

    private RectF roundRect = new RectF();
    private float rect_rdius = 10;
    private final Paint maskPaint = new Paint();
    private final Paint zonePaint = new Paint();

    public RoundUtil(Context context,float rect_rdius) {
        this.rect_rdius = rect_rdius;
        init(context);
    }

    private void init(Context context) {
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //
        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.WHITE);
        //
    }

    public void setRoundRect(RectF roundRect) {
        this.roundRect = roundRect;
    }

    public void setRect_rdius(float rect_rdius) {
        this.rect_rdius = rect_rdius;
    }

    public void draw(Canvas canvas) {
        canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(roundRect, rect_rdius, rect_rdius, zonePaint);
        canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
    }

}
