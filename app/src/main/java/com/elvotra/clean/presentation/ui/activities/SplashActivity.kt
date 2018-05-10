package com.elvotra.clean.presentation.ui.activities

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.airbnb.lottie.LottieAnimationView
import com.elvotra.clean.R

class SplashActivity : AppCompatActivity() {
    var lottieAnimationView: LottieAnimationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lottieAnimationView = findViewById(R.id.splash_animation)
        setupSplashAnimation()
    }

    private fun setupSplashAnimation() {
        lottieAnimationView!!.repeatCount = 1
        lottieAnimationView!!.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                startPostsActivity()
            }

            override fun onAnimationCancel(animator: Animator) {
                startPostsActivity()
            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
    }

    private fun startPostsActivity() {
        val intent = Intent(this@SplashActivity, PostsActivity::class.java)
        startActivity(intent)
        finish()
    }
}
