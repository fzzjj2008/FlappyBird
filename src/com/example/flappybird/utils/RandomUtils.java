package com.example.flappybird.utils;

import java.util.Random;

import com.example.flappybird.Global;

public class RandomUtils {

    private static Random rand = null;
    
    public static float getFloat(float a, float b) {
        if (rand == null) {
            rand = new Random();
        }
        return a + (b - a) * rand.nextFloat();
    }
    
    public static float getPipeCenterY() {
        float minMargin = (Global.pipeMinLength + Global.pipeSpace / 2 ) * Global.scaleY;
        return getFloat(minMargin, Global.groundTop - minMargin);
    }
    
}
