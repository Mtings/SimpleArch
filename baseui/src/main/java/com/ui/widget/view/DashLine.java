package com.ui.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.ui.R;

public class DashLine extends View {
    static public int ORIENTATION_HORIZONTAL = 0;
    static public int ORIENTATION_VERTICAL = 1;
    private Paint mPaint;
    private int orientation;

    public DashLine(Context context) {
        this(context, null);
    }

    public DashLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        int dashGap, dashLength, dashThickness;
        int color;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DashLine, 0, 0);

        try {
            dashGap = a.getDimensionPixelSize(R.styleable.DashLine_dashGap, 5);
            dashLength = a.getDimensionPixelSize(R.styleable.DashLine_dashLength, 5);
            dashThickness = a.getDimensionPixelSize(R.styleable.DashLine_dashThickness, 3);
            color = a.getColor(R.styleable.DashLine_dividerLineColor, 0xff000000);
            orientation = a.getInt(R.styleable.DashLine_dividerOrientation, ORIENTATION_HORIZONTAL);
        } finally {
            a.recycle();
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dashThickness);
        mPaint.setPathEffect(new DashPathEffect(new float[]{dashGap, dashLength,}, 0));
    }

    public void setBgColor(@NonNull int color) {
        mPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (orientation == ORIENTATION_HORIZONTAL) {
            float center = getHeight() * 0.5f;
            canvas.drawLine(0, center, getWidth(), center, mPaint);
        } else {
            float center = getWidth() * 0.5f;
            canvas.drawLine(center, 0, center, getHeight(), mPaint);
        }
    }
}
