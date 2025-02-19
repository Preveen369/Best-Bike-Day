package com.example.bestbikeday.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.bestbikeday.R;

public class CircularScoreView extends View {
    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;
    private RectF circleRect;
    private int score = 0;

    public CircularScoreView(Context context) {
        super(context);
        init();
    }

    public CircularScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(dpToPx(8));
        backgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.score_background));

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(dpToPx(8));
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.score_text));

        circleRect = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = Math.min(MeasureSpec.getSize(widthMeasureSpec), 
                           MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int padding = dpToPx(12);
        circleRect.set(padding, padding, w - padding, h - padding);
        textPaint.setTextSize(w / 4f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw background circle
        canvas.drawArc(circleRect, 0, 360, false, backgroundPaint);

        // Draw progress arc
        float sweepAngle = (score * 360f) / 100f;
        progressPaint.setColor(getScoreColor(score));
        canvas.drawArc(circleRect, -90, sweepAngle, false, progressPaint);

        // Draw score text
        String scoreText = score + "%";
        float textHeight = textPaint.descent() - textPaint.ascent();
        float textOffset = textHeight / 2 - textPaint.descent();
        canvas.drawText(scoreText, circleRect.centerX(), 
                       circleRect.centerY() + textOffset, textPaint);
    }

    public void setScore(int score) {
        this.score = score;
        invalidate();
    }

    private int getScoreColor(int score) {
        if (score >= 80) {
            return ContextCompat.getColor(getContext(), R.color.score_excellent_start);
        } else if (score >= 60) {
            return ContextCompat.getColor(getContext(), R.color.score_good_start);
        } else if (score >= 40) {
            return ContextCompat.getColor(getContext(), R.color.score_fair_start);
        } else {
            return ContextCompat.getColor(getContext(), R.color.score_poor_start);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
} 