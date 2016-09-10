package com.example.flappybird;

import com.example.flappybird.utils.SoundUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);                
    }
    
    @Override
    public void onBackPressed() {
        // 音效
        SoundUtils.play(Global.SOUND_SWOOSHING);
        // 切换到StartActivity
        Intent intent = new Intent(GameActivity.this, StartActivity.class);
        startActivity(intent);
        // 切换动画
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out); 
        super.onBackPressed();
    }

}
