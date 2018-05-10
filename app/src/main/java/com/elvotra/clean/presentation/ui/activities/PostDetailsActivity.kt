package com.elvotra.clean.presentation.ui.activities

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.elvotra.clean.R
import com.elvotra.clean.presentation.di.Injector
import com.elvotra.clean.presentation.presenter.PostDetailsPresenter
import com.elvotra.clean.presentation.ui.fragments.PostDetailsFragment
import com.elvotra.clean.utils.ActivityUtils
import com.elvotra.clean.utils.PaletteUtils
import timber.log.Timber

class PostDetailsActivity : AppCompatActivity(), PostDetailsFragment.PostDetailsToolbarCallback, AppBarLayout.OnOffsetChangedListener {

    private var mIsTheTitleVisible = true
    private var mIsTheTitleContainerVisible = true

    internal var post_details_toolbar: Toolbar? = null
    internal var post_details_toolbar_layout: CollapsingToolbarLayout? = null
    internal var post_details_toolbar_image: ImageView? = null
    internal var post_details_toolbar_title: TextView? = null
    internal var post_details_post_user: TextView? = null
    internal var posts_appbar: AppBarLayout? = null
    internal var post_details_title_container: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        val postId = intent.getIntExtra(POST_ID, -1)

        setUpToolbar()

        var postsListFragment: PostDetailsFragment? = supportFragmentManager.findFragmentById(R.id.contentPostDetailsFrame) as PostDetailsFragment

        if (postsListFragment == null) {
            // Create the fragment
            postsListFragment = PostDetailsFragment.newInstance(postId)
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, postsListFragment!!, R.id.contentPostDetailsFrame)
        }

        PostDetailsPresenter(postsListFragment, Injector.provideGetPostUseCase(this), Injector.provideUseCaseHandler())

        posts_appbar!!.addOnOffsetChangedListener(this)

        startAlphaAnimation(post_details_toolbar_title!!, 0, View.INVISIBLE)

    }

    private fun setUpToolbar() {
        setSupportActionBar(post_details_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        post_details_toolbar!!.setNavigationOnClickListener { supportFinishAfterTransition() }
    }

    override fun updateToolbar(avatarUrl: String, username: String) {
        Timber.d("Trying to update the toolbar data")

        Glide.with(this)
                .asBitmap()
                .load(avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        setToolbarColor(resource)
                        post_details_toolbar_image!!.visibility = View.VISIBLE
                        post_details_toolbar_image!!.setImageBitmap(resource)
                    }
                })
        post_details_toolbar_layout!!.title = username
        post_details_post_user!!.text = username
        post_details_toolbar_title!!.text = username
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        handleOffsetChanged(appBarLayout, offset)
    }

    private fun handleOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(post_details_toolbar_title!!, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                mIsTheTitleVisible = true
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(post_details_toolbar_title!!, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                mIsTheTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(post_details_title_container!!, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                mIsTheTitleContainerVisible = false
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(post_details_title_container!!, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                mIsTheTitleContainerVisible = true
            }
        }
    }

    fun setToolbarColor(bitmap: Bitmap) {
        Timber.d("Trying to update the toolbar color")
        val p = PaletteUtils.createPaletteSync(bitmap)
        val lightVibrantSwatch = p.vibrantSwatch
        val darkVibrantSwatch = p.mutedSwatch

        if (lightVibrantSwatch != null) {
            post_details_toolbar!!.setBackgroundColor(lightVibrantSwatch.rgb)
            post_details_toolbar!!.setTitleTextColor(lightVibrantSwatch.titleTextColor)
        } else {
            val backgroundColor = ContextCompat.getColor(this, R.color.colorPrimary)
            val textColor = ContextCompat.getColor(this, R.color.colorAccent)

            post_details_toolbar!!.setBackgroundColor(backgroundColor)
            post_details_toolbar!!.setTitleTextColor(textColor)
        }

        if (darkVibrantSwatch != null) {
            posts_appbar!!.setBackgroundColor(darkVibrantSwatch.rgb)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = darkVibrantSwatch.rgb
            }
        }

    }

    companion object {
        val POST_ID = "POST_ID"

        private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
        private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
        private val ALPHA_ANIMATIONS_DURATION = 200

        fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
            val alphaAnimation = if (visibility == View.VISIBLE)
                AlphaAnimation(0f, 1f)
            else
                AlphaAnimation(1f, 0f)

            alphaAnimation.duration = duration
            alphaAnimation.fillAfter = true
            v.startAnimation(alphaAnimation)
        }
    }
}
