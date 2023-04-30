package com.example.heal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {

    private Paint paint;
    private RectF rectF;
    private float[] data;

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
        data = new float[]{20, 30, 10, 40};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float radius = Math.min(getWidth(), getHeight()) / 2f;
        rectF.set(getWidth() / 2f - radius, getHeight() / 2f - radius, getWidth() / 2f + radius, getHeight() / 2f + radius);

        float startAngle = 0;
        for (int i = 0; i < data.length; i++) {
            paint.setColor(getColorForIndex(i));
            float sweepAngle = data[i] / 100f * 360f;
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
            startAngle += sweepAngle;
        }
    }

    private int getColorForIndex(int index) {
        switch (index) {
            case 0:
                return Color.BLUE;
            case 1:
                return Color.RED;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.YELLOW;
            default:
                return Color.BLACK;
        }
    }

    public void setData(float[] data) {
        this.data = data;
        invalidate();
    }
}
