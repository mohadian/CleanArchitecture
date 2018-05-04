package com.elvotra.clean.presentation.ui.activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.elvotra.clean.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.splash_animation)
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        setupSplashAnimation();
    }

    private void setupSplashAnimation() {
        lottieAnimationView.setRepeatCount(1);
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                startPostsActivity();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                startPostsActivity();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void startPostsActivity() {
        Intent intent = new Intent(SplashActivity.this, PostsActivity.class);
        startActivity(intent);
        finish();
    }
}
