package com.elvotra.clean.presentation.ui.activities;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.elvotra.clean.R;
import com.elvotra.clean.data.local.TypicodeDatabase;
import com.elvotra.clean.data.local.TypicodeLocalDataSource;
import com.elvotra.clean.data.remote.TypicodeRemoteDataSource;
import com.elvotra.clean.data.repository.PostsRepositoryImp;
import com.elvotra.clean.domain.executor.impl.ThreadExecutor;
import com.elvotra.clean.presentation.presenter.PostDetailsPresenter;
import com.elvotra.clean.presentation.presenter.imp.PostDetailsPresenterImp;
import com.elvotra.clean.presentation.ui.fragments.PostDetailsFragment;
import com.elvotra.clean.threading.AppExecutors;
import com.elvotra.clean.threading.MainThreadImpl;
import com.elvotra.clean.utils.ActivityUtils;
import com.elvotra.clean.utils.PaletteUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetailsActivity extends AppCompatActivity implements PostDetailsFragment.PostDetailsToolbarCallback
        , AppBarLayout.OnOffsetChangedListener {
    public static final String POST_ID = "POST_ID";

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = true;
    private boolean mIsTheTitleContainerVisible = true;

    @BindView(R.id.post_details_toolbar)
    Toolbar toolbar;
    @BindView(R.id.post_details_toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.post_details_toolbar_image)
    ImageView imageToolbar;
    @BindView(R.id.post_details_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.post_details_post_user)
    TextView postUser;
    @BindView(R.id.posts_appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.post_details_title_container)
    LinearLayout titleContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        ButterKnife.bind(this);

        int postId = getIntent().getIntExtra(POST_ID, -1);

        setUpToolbar();

        PostDetailsFragment postsListFragment =
                (PostDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentPostDetailsFrame);

        if (postsListFragment == null) {
            // Create the fragment
            postsListFragment = PostDetailsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), postsListFragment, R.id.contentPostDetailsFrame);
        }

        PostDetailsPresenter presenter = new PostDetailsPresenterImp(postId,
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                postsListFragment,
                PostsRepositoryImp.getInstance(TypicodeRemoteDataSource.getInstance(),
                        TypicodeLocalDataSource.getInstance(new AppExecutors(), TypicodeDatabase.getInstance(PostDetailsActivity.this).typicodeDao())));

        presenter.loadPost(postId);
        //handleOffsetChanged(appBarLayout, 0);
        appBarLayout.addOnOffsetChangedListener(this);

        startAlphaAnimation(toolbarTitle, 0, View.INVISIBLE);

    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });
    }

    @Override
    public void updateToolbar(String avatarUrl, String username) {
        Glide.with(this)
        .asBitmap()
                .load(avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        setToolbarColor(resource);
                        imageToolbar.setImageBitmap(resource);
                    }
                });
        toolbarLayout.setTitle(username);
        postUser.setText(username);
        toolbarTitle.setText(username);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        handleOffsetChanged(appBarLayout, offset);
    }

    private void handleOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(toolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(toolbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    public void setToolbarColor(Bitmap bitmap) {
        Palette p = PaletteUtils.createPaletteSync(bitmap);
        Palette.Swatch lightVibrantSwatch = p.getVibrantSwatch();
        Palette.Swatch darkVibrantSwatch = p.getMutedSwatch();

        if (lightVibrantSwatch != null) {
            toolbar.setBackgroundColor(lightVibrantSwatch.getRgb());
            toolbar.setTitleTextColor(lightVibrantSwatch.getTitleTextColor());
        } else {
            int backgroundColor = ContextCompat.getColor(this, R.color.colorPrimary);
            int textColor = ContextCompat.getColor(this, R.color.colorAccent);

            toolbar.setBackgroundColor(backgroundColor);
            toolbar.setTitleTextColor(textColor);
        }

        if (darkVibrantSwatch != null) {
            appBarLayout.setBackgroundColor(darkVibrantSwatch.getRgb());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(darkVibrantSwatch.getRgb());
            }
        }

    }
}
