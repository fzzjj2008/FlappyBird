package com.example.flappybird;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.flappybird.ui.StartView;
import com.example.flappybird.utils.SoundUtils;

public class StartActivity extends Activity {

    private StartView mStartView;
    private ImageView mPlayButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        mStartView = (StartView) findViewById(R.id.start_view);
        mPlayButton = (ImageView) findViewById(R.id.start_play_button);
        mPlayButton.setImageBitmap(Global.playButton);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mStartView.setGroundMove(true);  
    }
    
    @Override
    protected void onPause() {
        mStartView.setGroundMove(false);
        super.onPause();
    }
    
    public void playEvent(View view) {
        // 音效
        SoundUtils.play(Global.SOUND_SWOOSHING);
        // 切换到GameActivity
        Intent intent = new Intent(StartActivity.this, GameActivity.class);
        startActivity(intent);
        // 切换动画
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out); 
    }
}
