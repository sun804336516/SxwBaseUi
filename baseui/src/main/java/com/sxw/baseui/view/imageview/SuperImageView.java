package com.sxw.baseui.view.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sxw.baseui.R;
import com.sxw.baseui.utils.ViewUtils;


/**
 * 圆角、圆形、椭圆  ImageView控件
 */
public class SuperImageView extends ImageView {
    /**
     * 圆角
     */
    public static final int TYPE_ROUND = 1;
    /**
     * 圆形
     */
    public static final int TYPE_CIRLCE = 2;
    /**
     * 椭圆或者不规则圆角
     */
    public static final int TYPE_OVAL = 3;

    private RectF layerRect = new RectF();
    private final Paint maskPaint = new Paint();
    private final Paint zonePaint = new Paint();
    private Path drawPath = new Path();
    private int type = TYPE_ROUND;
    private float roundRdius = 10;

    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    private float[] radius;

    public SuperImageView(Context context) {
        this(context, null);
    }

    public SuperImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SuperImageView);
        type = typedArray.getInteger(R.styleable.SuperImageView_type, TYPE_ROUND);
        roundRdius = typedArray.getDimension(R.styleable.SuperImageView_round_radius, ViewUtils.dp2px( 5));

        float tlRadius = typedArray.getDimension(R.styleable.SuperImageView_tl_radius, 0);
        float trRadius = typedArray.getDimension(R.styleable.SuperImageView_tr_radius, 0);
        float blRadius = typedArray.getDimension(R.styleable.SuperImageView_bl_radius, 0);
        float brRadius = typedArray.getDimension(R.styleable.SuperImageView_br_radius, 0);

        typedArray.recycle();

        radius = new float[]{tlRadius, tlRadius, trRadius, trRadius, blRadius, blRadius, brRadius, brRadius,};

        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //
        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.WHITE);
        //

    }

    /**
     * 设置圆角
     *
     * @param round_rdius
     */
    public void setRoundRdius(float round_rdius) {
        if (type == TYPE_ROUND) {
            this.roundRdius = round_rdius;
            drawPath.reset();
            drawPath.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), this.roundRdius, this.roundRdius, Path.Direction.CW);
            invalidate();
        }
    }

    /**
     * 设置不规则圆角
     *
     * @param tlRadius
     * @param trRadius
     * @param blRadius
     * @param brRadius
     */
    public void setOvalRadius(float tlRadius, float trRadius, float blRadius, float brRadius) {
        radius = new float[]{tlRadius, tlRadius, trRadius, trRadius, blRadius, blRadius, brRadius, brRadius,};
        if (type == TYPE_OVAL) {
            drawPath.reset();
            drawPath.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), radius, Path.Direction.CW);
            invalidate();
        }
    }

    /**
     * 设置类型{@link #TYPE_CIRLCE#TYPE_ROUND#TYPE_OVAL}
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
        drawPath.reset();
        switch (type) {
            case TYPE_CIRLCE:
                int circleRaidus = Math.min(getWidth(), getHeight()) >> 1;
                drawPath.addCircle(getWidth() >> 1, getHeight() >> 1, circleRaidus, Path.Direction.CW);
                break;
            case TYPE_ROUND:
                drawPath.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), this.roundRdius, this.roundRdius, Path.Direction.CW);
                break;
            case TYPE_OVAL:
                drawPath.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), radius, Path.Direction.CW);
                break;
        }
        invalidate();
    }

    /**
     * 自定义绘制形状
     *
     * @param drawPath
     */
    public void setDrawPath(Path drawPath) {
        this.drawPath.reset();
        this.drawPath.addPath(drawPath);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int wid = right - left;
        int hei = bottom - top;
        layerRect = new RectF(0, 0, wid, hei);
        drawPath.reset();
        switch (type) {
            case TYPE_CIRLCE:
                int circleRaidus = Math.min(wid, hei) >> 1;
                drawPath.addCircle(wid >> 1, hei >> 1, circleRaidus, Path.Direction.CW);
                break;
            case TYPE_ROUND:
                drawPath.addRoundRect(new RectF(0, 0, wid, hei), this.roundRdius, this.roundRdius, Path.Direction.CW);
                break;
            case TYPE_OVAL:
                drawPath.addRoundRect(new RectF(0, 0, wid, hei), radius, Path.Direction.CW);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.saveLayer(layerRect, zonePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(drawPath, zonePaint);
        canvas.saveLayer(layerRect, maskPaint, Canvas.ALL_SAVE_FLAG);

        super.onDraw(canvas);
        canvas.restore();
    }
}
