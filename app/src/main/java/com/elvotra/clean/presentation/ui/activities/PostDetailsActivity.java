package com.elvotra.clean.presentation.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetailsActivity extends AppCompatActivity implements PostDetailsFragment.PostDetailsAvatarCallback {
    public static final String POST_ID = "POST_ID";

    @BindView(R.id.post_details_toolbar)
    Toolbar toolbar;
    @BindView(R.id.post_details_toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.post_details_toolbar_image)
    ImageView imageToolbar;

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

    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });

        toolbarLayout.setTitle(" ");

    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Glide.with(this)
                .load(avatarUrl)
                .into(imageToolbar);
    }
}
