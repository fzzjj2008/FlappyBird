package com.example.flappybird.ui;

import static com.example.flappybird.Global.bgDay;
import static com.example.flappybird.Global.birdYellow;
import static com.example.flappybird.Global.birdYellowDown;
import static com.example.flappybird.Global.birdYellowUp;
import static com.example.flappybird.Global.ground;
import static com.example.flappybird.Global.pipeDown;
import static com.example.flappybird.Global.pipeUp;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.flappybird.Global;
import com.example.flappybird.elements.Bird;
import com.example.flappybird.elements.Bird.Wing;
import com.example.flappybird.elements.Pipe;
import com.example.flappybird.utils.RandomUtils;
import com.example.flappybird.utils.SoundUtils;

public class GameView extends View {

    private Paint mPaint;
    
    private Bird mBird;
    private Pipe mPipes[];
    private float groundLeft;
    private float groundLeft2;
    private boolean birdFlyingFlag;
    
    public enum GameState {
        READY, PLAYING, DIE
    }
    public GameState mGameState = GameState.READY;
    
    // 这里是程序最巧妙的地方，参考贪吃蛇游戏的实现
    // 利用了Handler的发送消息机制实现了反复的延迟刷新
    private class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 0:
                GameView.this.update();
                break;
            case 1:
                GameView.this.updateWing();
                break;
            }
            GameView.this.invalidate();
        }
        public void sleep(int what, long delayMillis) {
            this.removeMessages(what);
            this.sendEmptyMessageDelayed(what, delayMillis);
        }
    }
    private RefreshHandler mRefreshHandler = new RefreshHandler();
    private long mLastMove; // 上一次移动的时刻 
    private long mWingLastMove;
    
    // 监听器
    private GameViewListener mListener;
    
    // 构造函数
    public GameView(Context context) {
        super(context);
        init();
    }

    // 构造函数
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        // 画笔
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Style.STROKE);
        
        // 初始化游戏数据
        mBird = new Bird();
        mPipes = new Pipe[Global.pipeNum];
        initGame();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        drawBackGround(canvas);
        drawPipe(canvas);
        drawBird(canvas);
        drawGround(canvas);
    }
    
    private void drawBackGround(Canvas canvas) {
        canvas.drawBitmap(bgDay, 0, 0, null);
    }
    
    private void drawBird(Canvas canvas) {
        Bitmap bird = birdYellow;
        switch (mBird.getWing()) {
        case MID:
        case MID2:
            bird = birdYellow;
            break;
        case UP:
            bird = birdYellowUp;
            break;
        case DOWN:
            bird = birdYellowDown;
            break;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(mBird.getRotate(), (float) bird.getWidth() / 2, (float) bird.getHeight() / 2);
        Bitmap rotateBird = Bitmap.createBitmap(bird, 0, 0,
                bird.getWidth(), bird.getHeight(), matrix, true);
        canvas.drawBitmap(rotateBird, mBird.getCenterX() - rotateBird.getWidth() / 2,
                mBird.getCenterY() - rotateBird.getHeight() / 2, null);
        if (Global.assist) {
            canvas.drawCircle(mBird.getCenterX(), mBird.getCenterY(),
                    Global.birdRadius * Global.scaleY, mPaint);
        }
    }
    
    private void drawGround(Canvas canvas) {
        canvas.drawBitmap(ground, groundLeft, Global.groundTop, null);
        canvas.drawBitmap(ground, groundLeft2, Global.groundTop, null);
        if (Global.assist) {
            canvas.drawLine(0, Global.groundTop, Global.screenWidth, Global.groundTop, mPaint);
        }
    }
    
    private void drawPipe(Canvas canvas) {
        for (int i = 0; i < mPipes.length; i++) {
            canvas.drawBitmap(pipeDown, mPipes[i].getCenterX() - Global.pipeWidth / 2,
                    -pipeDown.getHeight() + mPipes[i].getCenterY() - mPipes[i].getSpace() / 2, null);
            canvas.drawBitmap(pipeUp, mPipes[i].getCenterX() - Global.pipeWidth / 2,
                    mPipes[i].getCenterY() + mPipes[i].getSpace() / 2, null);
            if (Global.assist) {
                canvas.drawRect(mPipes[i].getCenterX() - Global.pipeWidth / 2, 0,
                        mPipes[i].getCenterX() + Global.pipeWidth / 2,
                        mPipes[i].getCenterY() - mPipes[i].getSpace() / 2, mPaint);
                canvas.drawRect(mPipes[i].getCenterX() - Global.pipeWidth / 2,
                        mPipes[i].getCenterY() + mPipes[i].getSpace() / 2,
                        mPipes[i].getCenterX() + Global.pipeWidth / 2,
                        Global.groundTop, mPaint);
            }
        }
    }
    
    public void initGame() {
        mGameState = GameState.READY;
        // bird
        mBird.setCenterX(Global.screenWidth / 3f);
        mBird.setCenterY(Global.groundTop / 1.6f);
        mBird.setSpeed(0);
        mBird.setRotate(0);
        mBird.setWing(Wing.MID);
        mBird.setWingCount(0);
        // pipes
        for (int i = 0; i < mPipes.length; i++) {
            mPipes[i] = new Pipe();
            mPipes[i].setCenterX((Global.pipeFirstPos + i * Global.pipeInterval) * Global.scaleX);
            mPipes[i].setCenterY(RandomUtils.getPipeCenterY());
            mPipes[i].setSpace(Global.pipeSpace * Global.scaleY);
        }
        // ground
        groundLeft = 0;
        groundLeft2 = ground.getWidth();
    }
    
    private void update() {
        long now = System.currentTimeMillis();
        if (now - mLastMove >= Global.moveDelayed) {
            updatePipes();
            updateBird();
            updateGround();
            if (checkCrash()) {
                mGameState = GameState.DIE;
                SoundUtils.play(Global.SOUND_HIT);
                // TODO
                // 小鸟落下动画
                // 闪屏
                // 分数版
                if (mListener != null) {
                    mListener.onGameOver();
                }
            }
            mLastMove = now;  
        }
        if (mGameState != GameState.DIE)
            mRefreshHandler.sleep(0, Global.moveDelayed);
    }
    
    private void updateWing() {
        long now = System.currentTimeMillis();
        if (now - mWingLastMove >= Global.birdWingDelayed) {
            updateBirdWing();
            mWingLastMove = now;  
        }
        mRefreshHandler.sleep(1, Global.birdWingDelayed);
    }
    
    private void updateBird() {
        float speed = mBird.getSpeed();
        float centerY = mBird.getCenterY();
        float rotate = mBird.getRotate();
        speed += Global.birdGravity * Global.scaleY;
        mBird.setSpeed(speed);
        mBird.setCenterY(centerY + speed);
        if (birdFlyingFlag) {
            rotate -= 8;
        }
        if (speed > 10) {
            birdFlyingFlag = false;
            rotate += 5;
        }
        if (rotate < Global.birdFlyingRotate) {
            rotate = Global.birdFlyingRotate;
        }            
        if (rotate > 90) {
            rotate = 90;
        }
        mBird.setRotate(rotate);
    }
    
    private void updateBirdWing() {
        Wing wing = mBird.getWing();
        int wingCount = mBird.getWingCount();
        // 向上飞只扇2次翅膀
        if (wingCount < 2) {
            int ordinal = wing.ordinal();
            ordinal = (ordinal + 1) % Wing.values().length;
            wing = Wing.values()[ordinal];
            mBird.setWing(wing);
            // 回到MID状态表示扇了一次翅膀
            if (Wing.MID == wing) {
                wingCount++;
                mBird.setWingCount(wingCount);
            }
        }
    }
    
    private void updatePipes() {
        for (int i = 0; i < mPipes.length; i++) {
            float centerX = mPipes[i].getCenterX();
            centerX -= Global.groundSpeed * Global.scaleX;
            // 计数器+1
            float dBirdPipe = mBird.getCenterX() - centerX;
            float interval = Global.groundSpeed * Global.scaleX / 2;
            if (dBirdPipe > -interval && dBirdPipe <= interval) {
                mListener.onScoreChange();
            }
            // 如果管子看不见，移到最右边
            if (centerX + Global.pipeWidth < 0) {
                centerX += mPipes.length * Global.pipeInterval * Global.scaleX;
                float centerY = RandomUtils.getPipeCenterY();
                mPipes[i].setCenterY(centerY);             
            }
            mPipes[i].setCenterX(centerX);
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
    
    private boolean checkCrash() {
        // 两种情况
        // 鸟碰到地板
        if (mBird.getCenterY() + Global.birdRadius * Global.scaleY >= Global.groundTop)
            return true;
        // 鸟碰到管子
        // 寻找距离最近的管子
        int pipeIndex = 0;
        float pipeDistance = Math.abs(mBird.getCenterX() - mPipes[0].getCenterX());
        float minPipeDistance = pipeDistance;
        for (int i = 1; i < mPipes.length; i++) {
            pipeDistance = Math.abs(mBird.getCenterX() - mPipes[i].getCenterX());
            if (pipeDistance < minPipeDistance) {
                pipeIndex = i;
                minPipeDistance = pipeDistance;
            }
        }
        // 上管子
        float pipeCenterY = -Global.pipeHeight / 2 + mPipes[pipeIndex].getCenterY() - mPipes[pipeIndex].getSpace() / 2;
        if (crashDetect(mBird.getCenterX(), mBird.getCenterY(), Global.birdRadius * Global.scaleY,
                mPipes[pipeIndex].getCenterX(), pipeCenterY, Global.pipeWidth, Global.pipeHeight)) {
            return true;
        }
        // 下管子
        pipeCenterY = Global.pipeHeight / 2 + mPipes[pipeIndex].getCenterY() + mPipes[pipeIndex].getSpace() / 2;
        if (crashDetect(mBird.getCenterX(), mBird.getCenterY(), Global.birdRadius * Global.scaleY,
                mPipes[pipeIndex].getCenterX(), pipeCenterY, Global.pipeWidth, Global.pipeHeight)) {
            return true;
        }
        return false;
    }
    
    // 核心：检测圆形和矩形碰撞
    // 参考：http://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
    private boolean crashDetect(float roundX, float roundY, float radius,
            float rectCenterX, float rectCenterY, float rectWidth, float rectHeight) {
        // 把矩形中心设置为原点，只需要考虑第一象限
        float distanceX = Math.abs(roundX - rectCenterX);
        float distanceY = Math.abs(roundY - rectCenterY);
        // 距离超过外围矩形边界，不相交
        if (distanceX > rectWidth / 2 + radius)     return false;
        if (distanceY > rectHeight / 2 + radius)    return false;
        // 剩下的都在外围矩形边界内
        // 排除所有情况，仅剩下右上角角落
        if (distanceX < rectWidth / 2)      return true;
        if (distanceY < rectHeight / 2)     return true;
        // 最后判断在右上角角落的情况
        float cornerDistance = (distanceX - rectWidth / 2) * (distanceX - rectWidth / 2)
                + (distanceY - rectHeight / 2) * (distanceY - rectHeight / 2);
        return (cornerDistance < (radius * radius));
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (GameState.READY == mGameState) {
                mGameState = GameState.PLAYING;
                birdFlyingFlag = true;
                // 保证小鸟一开始就跳
                mBird.setSpeed(Global.birdJumpSpeed);
                mBird.setWing(Wing.MID);
                mBird.setWingCount(0);
                update();
                updateWing();
                SoundUtils.play(Global.SOUND_WING);
                if (mListener != null) {
                    mListener.onGameStart();
                }
            } else if (GameState.PLAYING == mGameState) {
                // 飞到太高的地方不响应ACTION_DOWN
                if (mBird.getCenterY() >= 0) {
                    birdFlyingFlag = true;
                    mBird.setSpeed(Global.birdJumpSpeed);
                    mBird.setWing(Wing.MID);
                    mBird.setWingCount(0);
                    update();
                    updateWing();
                    SoundUtils.play(Global.SOUND_WING);
                }
            } else if (GameState.DIE == mGameState) {
                // do nothing
            }
            break;
        }
        return true;
    }
    
    public void setGameViewListener(GameViewListener l) {
        this.mListener = l;
    }
}
