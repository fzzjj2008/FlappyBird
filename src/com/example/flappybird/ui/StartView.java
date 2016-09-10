package com.example.flappybird.ui;

import static com.example.flappybird.Global.bgDay;
import static com.example.flappybird.Global.ground;
import static com.example.flappybird.Global.title;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.example.flappybird.Global;

public class StartView extends View {
    
    private float groundLeft;
    private float groundLeft2;
    private boolean groundmove = false;
    
    private class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            StartView.this.update();
            StartView.this.invalidate();
        }
        public void sleep(long delayMillis) {
            this.removeMessages(0);
            this.sendEmptyMessageDelayed(0, delayMillis);
        }
    }
    private RefreshHandler mRefreshHandler = new RefreshHandler();
    private long mLastMove; // 上一次移动的时刻 

    public StartView(Context context) {
        this(context, null);
    }
    
    public StartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        groundLeft = 0;
        groundLeft2 = ground.getWidth();
        groundmove = true;
        update();
//        invalidate();
    }
    
    public boolean getGroundMove() {
        return this.groundmove;
    }
    
    public void setGroundMove(boolean move) {
        this.groundmove = move;
        if (move) {
            update();
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bgDay, 0, 0, null);
        canvas.drawBitmap(title, (Global.screenWidth - title.getWidth()) / 2,
                Global.titleTop * Global.scaleY, null);
        drawGround(canvas);
    }
    
    private void drawGround(Canvas canvas) {
        canvas.drawBitmap(ground, groundLeft, Global.groundTop, null);
        canvas.drawBitmap(ground, groundLeft2, Global.groundTop, null);
    }
    
    private void update() {
        long now = System.currentTimeMillis();
        if (now - mLastMove >= Global.moveDelayed) {
            updateGround();
            mLastMove = now;  
        }
        if (groundmove) {
            mRefreshHandler.sleep(Global.moveDelayed);
        }
    }
    
    private void updateGround() {
        groundLeft -= Global.groundSpeed * Global.scaleX;
        groundLeft2 -= Global.groundSpeed * Global.scaleX;
        if (groundLeft < -ground.getWidth())
            groundLeft = groundLeft2 + ground.getWidth();
        else if (groundLeft2 < -ground.getWidth())
            groundLeft2 = groundLeft + ground.getWidth();
    }

}
