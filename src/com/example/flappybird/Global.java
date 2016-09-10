package com.example.flappybird;

import java.util.HashMap;

import android.graphics.Bitmap;

public class Global {
    
    // bitmap
    public static Bitmap title;
    public static Bitmap playButton;
    public static Bitmap bgDay;
    public static Bitmap birdYellow;
    public static Bitmap birdYellowUp;
    public static Bitmap birdYellowDown;
    public static Bitmap pipeUp;
    public static Bitmap pipeDown;
    public static Bitmap ground;
    public static Bitmap ready;
    public static Bitmap tutorial;
    public static Bitmap gameOver;
    public static Bitmap[] number;
    
    // sound
    public static HashMap<Integer, Integer> soundPoolMap;
    public static final int SOUND_DIE = 1;
    public static final int SOUND_HIT = 2;
    public static final int SOUND_POINT = 3;
    public static final int SOUND_SWOOSHING = 4;
    public static final int SOUND_WING = 5;

    public static float screenWidth;
    public static float screenHeight;
    public static float groundTop;
    public static float pipeWidth;
    public static float pipeHeight;
    public static float birdWidth;
    public static float birdHeight;
    public static float scaleX = 1.0f;
    public static float scaleY = 1.0f;
    
    // StartView
    public static final float titleTop = 250f;
    
    // GameLayout
    public static final float scoreTop = 100f;
    public static final float readyTop = 150f;
    public static final float tutorialTop = 250f;
    public static final float gameOverTop = 220f;
    public static final float playButtonTop = 350f;

    // GameView
    public static final boolean assist = false;
    public static final long moveDelayed = 10;
    public static final float birdJumpSpeed = -25f;
    public static final float birdGravity = 0.8f;
    public static final float birdFlyingRotate = -20f;
    public static final long birdWingDelayed = 50;
    public static final float birdRadius = 25f;
    public static final int pipeNum = 3;
    public static final float pipeInterval = 350f;
    public static final float pipeSpace = 225f;
    public static final float pipeFirstPos = 800f;
    public static final float pipeMinLength = 100f;
    public static final float groundSpeed = 4f;
    
}
