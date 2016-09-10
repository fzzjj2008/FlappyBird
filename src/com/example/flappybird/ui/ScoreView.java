package com.example.flappybird.ui;

import static com.example.flappybird.Global.number;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class ScoreView extends View {

    private static final int MAX_DIGIT = 5;
    private static final int MAX_NUM = (int) Math.pow(10, MAX_DIGIT) - 1;

    private int[] mNums;

    public ScoreView(Context context) {
        this(context, null);
    }

    public ScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 数字最高为99999
        mNums = new int[MAX_DIGIT + 1];
    }

    public void setNumber(int n) {
        // 限制范围0-99999
        if (n < 0) {
            n = 0;
        }
        if (n > MAX_NUM) {
            n = MAX_NUM;
        }
        // 数字转数组
        if (n == 0) {
            mNums[0] = 1;
            for (int i = 1; i <= MAX_DIGIT; i++)
                mNums[i] = 0;
        } else {
            int i = MAX_DIGIT;
            while (n > 0) {
                mNums[i] = n % 10;
                n /= 10;
                i--;
            }
            // 第一位存一共有多少位
            mNums[0] = MAX_DIGIT - i;
        }
        // 重绘
        invalidate();
    }
    
    public int getScoreWidth() {
        int result = 0;
        for (int i = MAX_DIGIT - mNums[0] + 1; i <= MAX_DIGIT; i++) {
            int digit = mNums[i];
            result += number[digit].getWidth();
        }
        return result;
    }
    
    public int getScoreHeight() {
        return number[0].getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float numberLeft = 0.0f;
        for (int i = MAX_DIGIT - mNums[0] + 1; i <= MAX_DIGIT; i++) {
            int digit = mNums[i];
            canvas.drawBitmap(number[digit], numberLeft, 0, null);
            numberLeft += number[digit].getWidth();
        }
    }
/*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // int heightSize = MeasureSpec.getSize(heightMeasureSpec);

//        mWidth = 0;
//        for (int i = MAX_DIGIT - mNums[0] + 1; i <= MAX_DIGIT; i++) {
//            int digit = mNums[i];
//            mWidth += number[digit].getWidth();
//        }
//        mHeight = number[0].getHeight();
//        mWidth = 200;
//
//        setMeasuredDimension(mWidth, mHeight);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0; // 结果
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
        case MeasureSpec.AT_MOST: // 子容器可以是声明大小内的任意大小
            result = specSize;
            break;
        case MeasureSpec.EXACTLY: // 父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间.
                                  // 比如EditTextView中的DrawLeft
            result = specSize;
            break;
        case MeasureSpec.UNSPECIFIED: // 父容器对于子容器没有任何限制,子容器想要多大就多大.
                                      // 所以完全取决于子view的大小
            result = 1500;
            break;
        default:
            break;
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0; // 结果
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
        case MeasureSpec.AT_MOST: // 子容器可以是声明大小内的任意大小
            result = specSize;
            break;
        case MeasureSpec.EXACTLY: // 父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间.
                                  // 比如EditTextView中的DrawLeft
            result = specSize;
            break;
        case MeasureSpec.UNSPECIFIED: // 父容器对于子容器没有任何限制,子容器想要多大就多大.
                                      // 所以完全取决于子view的大小
            result = 1500;
            break;
        default:
            break;
        }
        return result;
    }

*/    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, 0, 0, 300, 200);
        
    }
}
