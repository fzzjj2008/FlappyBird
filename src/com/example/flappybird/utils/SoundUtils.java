package com.example.flappybird.utils;

import com.example.flappybird.Global;
import com.example.flappybird.MainApplication;

import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundUtils {
    
    private static SoundPool mPool;
    
    @SuppressLint("NewApi")
    public static void initSoundPool() {
        if (mPool != null) {
            return;
        }
        /**
         * 21版本后，SoundPool的创建发生很大改变
         */
        //判断系统sdk版本，如果版本超过21，调用第一种
        if(Build.VERSION.SDK_INT>=21){
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(2);//传入音频数量
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);//设置音频流的合适的属性
            builder.setAudioAttributes(attrBuilder.build());//加载一个AudioAttributes
            mPool = builder.build();
        }else{
            // 创建了一个最多支持2个流同时播放的，类型标记为音乐的SoundPool
            mPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }
    }
    
    public static Integer load(int resId) {
        if (mPool == null) {
            initSoundPool();
        }
        return mPool.load(MainApplication.getContext(), resId, 1);
    }
    
    public static int play(int key) {
        if (mPool == null) {
            initSoundPool();
        }
        return mPool.play(Global.soundPoolMap.get(key), 1, 1, 0, 0, 1);
    }

}
