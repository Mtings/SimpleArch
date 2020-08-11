package com.song.sakura.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.song.sakura.R;

public class FlashFrameView extends FrameLayout {

    private View shadowView;
    private FlashTextView flashTextView;
    private CountDownTimer countDownTimer;
    private String childText;
    private int childTextType;

    //动画间隔时长
    private final static int DURATION = 200;

    public FlashFrameView(@NonNull Context context) {
        super(context);
    }

    public FlashFrameView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlashFrameView);
        childText = ta.getString(R.styleable.FlashFrameView_child_text);
        if (ta.hasValue(R.styleable.FlashFrameView_child_textType)) {
            childTextType = ta.getInt(R.styleable.FlashFrameView_child_textType, 0);
        }
        ta.recycle();
        init();
    }



    private void init() {
        inflate(getContext(), R.layout.view_flash_text, this);
        shadowView = this.findViewById(R.id.shadow);
        flashTextView = this.findViewById(R.id.flash);
        flashTextView.changeText(childText);
        flashTextView.changeType(childTextType);

        flashTextView.setOnPauseListener(isPause -> {
            shadowView.setVisibility(View.GONE);
            shadowView.clearAnimation();
        });
    }

    public void changeChildTextAndType(String childText) {
        flashTextView.changeTextAndType(childText);
    }

    public void startAnim() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            shadowView.clearAnimation();
            flashTextView.setCurrentIndex(0);
        }
        if (flashTextView.getTargetIndex() == -1) {
            return;
        }
        shadowView.setVisibility(VISIBLE);
        flashTextView.setPause(false);
        countDownTimer = new CountDownTimer(getMillisInFuture(), DURATION) {
            @Override
            public void onTick(long millisUntilFinished) {
                startTestRotate(shadowView);
                flashTextView.postInvalidate();
            }

            @Override
            public void onFinish() {
                resetCountdownTimer();
            }
        };
        countDownTimer.start();
    }

    private long getMillisInFuture() {
        return ((flashTextView.getTargetIndex() + 1) * DURATION) + (20 * (flashTextView.getTargetIndex() + 1));
    }

    public void resetCountdownTimer() {
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }
        shadowView.setVisibility(View.GONE);
        shadowView.clearAnimation();
        if (flashTextView.getTargetIndex() != -1) {
            flashTextView.setCurrentIndex(flashTextView.getTargetIndex());
            flashTextView.postInvalidate();
        }
    }


    private void startTestRotate(View view) {
        float start = 0f;
        float end = 180f;

        // 旋转中心点
        final float centerX = view.getWidth() / 2.0f;
        final float centerY = view.getHeight();

        //参数（上下文，开始角度，结束角度，x轴中心点，y轴中心点，深度，是否扭曲）
        final Rotate3dAnimation rotation = new Rotate3dAnimation(getContext(), start, end, centerX, centerY, 0f, true);

        rotation.setAxis(1);
        rotation.setDuration(DURATION);
        rotation.setInterpolator(new LinearInterpolator());
        view.startAnimation(rotation);
    }

    private void startTranslation(View view) {
        Animation animation = new TranslateAnimation(0f, 0f, 0f, getHeight() / 2f);
        animation.setDuration(DURATION);
        animation.setInterpolator(new LinearInterpolator());
        view.clearAnimation();
        view.startAnimation(animation);
    }

}
