package com.example.flappybird.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.flappybird.Global;
import com.example.flappybird.utils.ScaleUtils;
import com.example.flappybird.utils.SoundUtils;

public class GameLayout extends RelativeLayout implements GameViewListener, OnClickListener {
    
    private Context mContext;
    
    private ImageView mReady;
    private ImageView mTutorial;
    private GameView mGameView;
    private ScoreView mScoreView;
    private ImageView mGameOver;
    private ImageView mPlayButton;
//    private ImageView mBlackMask;
    
    private int score = 0;

    public GameLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }
    
    public GameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }
    
    private void init() {
        score = 0;
        // 加载控件
        initView();
    }
    
    private void initView() {
        RelativeLayout.LayoutParams lp = null;
        // GameView
        mGameView = new GameView(mContext);
        lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mGameView.setLayoutParams(lp);
        addView(mGameView);
        mGameView.setGameViewListener(this);

        mReady = new ImageView(mContext);
        mReady.setImageBitmap(Global.ready);
        lp = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.topMargin = ScaleUtils.dip2px(Global.readyTop);
        mReady.setLayoutParams(lp);
        addView(mReady);

        mTutorial = new ImageView(mContext);
        mTutorial.setImageBitmap(Global.tutorial);
        lp = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.topMargin = ScaleUtils.dip2px(Global.tutorialTop);
        mTutorial.setLayoutParams(lp);
        addView(mTutorial);

        mScoreView = new ScoreView(mContext);
        mScoreView.setNumber(score);
        // 更新位置
        updateScorePos();
        addView(mScoreView);
        mScoreView.setVisibility(View.GONE);
        
        mGameOver = new ImageView(mContext);
        mGameOver.setImageBitmap(Global.gameOver);
        lp = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.topMargin = ScaleUtils.dip2px(Global.gameOverTop);
        mGameOver.setLayoutParams(lp);
        addView(mGameOver);
        mGameOver.setVisibility(View.GONE);
        
        mPlayButton = new ImageView(mContext);
        mPlayButton.setImageBitmap(Global.playButton);
        lp = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.topMargin = ScaleUtils.dip2px(Global.playButtonTop);
        mPlayButton.setLayoutParams(lp);
        addView(mPlayButton);
        mPlayButton.setOnClickListener(this);
        mPlayButton.setVisibility(View.GONE);
        
//        mBlackMask = new ImageView(mContext);
//        mBlackMask.setImageResource(R.drawable.black_mask);
//        lp = new RelativeLayout.LayoutParams(
//                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        mBlackMask.setLayoutParams(lp);
//        addView(mBlackMask);
//        mBlackMask.setVisibility(View.GONE);
        
    }
    
    private void updateScorePos() {
        // 更新位置
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                mScoreView.getScoreWidth(), mScoreView.getScoreHeight());
        lp.leftMargin = (int) ((Global.screenWidth - mScoreView.getScoreWidth()) / 2);
        lp.topMargin = ScaleUtils.dip2px(Global.scoreTop);
        mScoreView.setLayoutParams(lp);
    }

    @Override
    public void onScoreChange() {
        score++;
        mScoreView.setNumber(score);
        // 更新位置
        updateScorePos();
        // 播放声音
        SoundUtils.play(Global.SOUND_POINT);
    }

    @Override
    public void onGameReady() {
        mScoreView.setVisibility(View.GONE);
        mReady.setVisibility(View.VISIBLE);
        mTutorial.setVisibility(View.VISIBLE);
        mGameOver.setVisibility(View.GONE);
        mPlayButton.setVisibility(View.GONE);
    }

    @Override
    public void onGameStart() {
        score = 0;
        mScoreView.setNumber(score);
        // 更新位置
        updateScorePos();
        mScoreView.setVisibility(View.VISIBLE);
        mReady.setVisibility(View.GONE);
        mTutorial.setVisibility(View.GONE);
    }

    @Override
    public void onGameOver() {
        mGameOver.setVisibility(View.VISIBLE);
        mPlayButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        // 播放声音
        SoundUtils.play(Global.SOUND_SWOOSHING);
        mGameView.initGame();
        onGameReady();
    }

}
