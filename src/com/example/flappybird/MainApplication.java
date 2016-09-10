package com.example.flappybird;

import static com.example.flappybird.Global.bgDay;
import static com.example.flappybird.Global.birdYellow;
import static com.example.flappybird.Global.birdYellowDown;
import static com.example.flappybird.Global.birdYellowUp;
import static com.example.flappybird.Global.gameOver;
import static com.example.flappybird.Global.ground;
import static com.example.flappybird.Global.number;
import static com.example.flappybird.Global.pipeDown;
import static com.example.flappybird.Global.pipeUp;
import static com.example.flappybird.Global.playButton;
import static com.example.flappybird.Global.ready;
import static com.example.flappybird.Global.title;
import static com.example.flappybird.Global.tutorial;

import java.util.HashMap;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

import com.example.flappybird.utils.SoundUtils;

public class MainApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        getScales();
        loadBitmap();
        loadSound();
    }
    
    private void getScales() {
        // 获取屏幕分辨率
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Global.screenWidth = dm.widthPixels;
        Global.screenHeight = dm.heightPixels;
        // 获取背景图片长宽
        Resources res = getResources();
        Bitmap background = BitmapFactory.decodeResource(res, R.drawable.bg_day);
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        // 记录缩放大小
        Global.scaleX = Global.screenWidth / bgWidth;
        Global.scaleY = Global.screenHeight / bgHeight;
        // 保证等比例放大
//        if (Global.scaleX < Global.scaleY)
//            Global.scaleX = Global.scaleY;
//        else
//            Global.scaleY = Global.scaleX;            
    }

    private void loadBitmap() {
        // 加载图片
        Resources res = getResources();
        title = BitmapFactory.decodeResource(res, R.drawable.title);
        playButton = BitmapFactory.decodeResource(res, R.drawable.button_play);
        bgDay = BitmapFactory.decodeResource(res, R.drawable.bg_day);
        birdYellow = BitmapFactory.decodeResource(res, R.drawable.bird0_1);
        birdYellowUp = BitmapFactory.decodeResource(res, R.drawable.bird0_0);
        birdYellowDown = BitmapFactory.decodeResource(res, R.drawable.bird0_2);
        pipeUp = BitmapFactory.decodeResource(res, R.drawable.pipe_up);
        pipeDown = BitmapFactory.decodeResource(res, R.drawable.pipe_down);
        ground = BitmapFactory.decodeResource(res, R.drawable.land);
        ready = BitmapFactory.decodeResource(res, R.drawable.text_ready);
        tutorial = BitmapFactory.decodeResource(res, R.drawable.tutorial);
        gameOver = BitmapFactory.decodeResource(res, R.drawable.text_game_over);
        number = new Bitmap[10];
        number[0] = BitmapFactory.decodeResource(res, R.drawable.font_048);
        number[1] = BitmapFactory.decodeResource(res, R.drawable.font_049);
        number[2] = BitmapFactory.decodeResource(res, R.drawable.font_050);
        number[3] = BitmapFactory.decodeResource(res, R.drawable.font_051);
        number[4] = BitmapFactory.decodeResource(res, R.drawable.font_052);
        number[5] = BitmapFactory.decodeResource(res, R.drawable.font_053);
        number[6] = BitmapFactory.decodeResource(res, R.drawable.font_054);
        number[7] = BitmapFactory.decodeResource(res, R.drawable.font_055);
        number[8] = BitmapFactory.decodeResource(res, R.drawable.font_056);
        number[9] = BitmapFactory.decodeResource(res, R.drawable.font_057);
        // 缩放图片以适应屏幕
        Matrix matrix = new Matrix();
        matrix.postScale(Global.scaleX, Global.scaleY);
        title = scaleBitmap(title, matrix);
        playButton = scaleBitmap(playButton, matrix);
        bgDay = scaleBitmap(bgDay, matrix);
        birdYellow = scaleBitmap(birdYellow, matrix);
        birdYellowUp = scaleBitmap(birdYellowUp, matrix);
        birdYellowDown = scaleBitmap(birdYellowDown, matrix);
        pipeUp = scaleBitmap(pipeUp, matrix);
        pipeDown = scaleBitmap(pipeDown, matrix);
        ground = scaleBitmap(ground, matrix);
        ready = scaleBitmap(ready, matrix);
        tutorial = scaleBitmap(tutorial, matrix);
        gameOver = scaleBitmap(gameOver, matrix);
        for (int i = 0; i < number.length; i++) {
            number[i] = scaleBitmap(number[i], matrix);
        }
        // 获得地的高度
        Global.groundTop = Global.screenHeight - ground.getHeight();
        // 管子
        Global.pipeWidth = pipeUp.getWidth();
        Global.pipeHeight = pipeUp.getHeight();
        // 鸟
        Global.birdWidth = birdYellow.getWidth();
        Global.birdHeight = birdYellow.getHeight();
    }
    
    private Bitmap scaleBitmap(Bitmap bitmap, Matrix matrix) {
        Bitmap scalebitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return scalebitmap;
    }
    
    private void loadSound() {
        Global.soundPoolMap = new HashMap<>();
        Global.soundPoolMap.put(Global.SOUND_DIE, SoundUtils.load(R.raw.sfx_die)); 
        Global.soundPoolMap.put(Global.SOUND_HIT, SoundUtils.load(R.raw.sfx_hit)); 
        Global.soundPoolMap.put(Global.SOUND_POINT, SoundUtils.load(R.raw.sfx_point)); 
        Global.soundPoolMap.put(Global.SOUND_SWOOSHING, SoundUtils.load(R.raw.sfx_swooshing)); 
        Global.soundPoolMap.put(Global.SOUND_WING, SoundUtils.load(R.raw.sfx_wing)); 
    }

    public static Context getContext() {
        return context;
    }
}
