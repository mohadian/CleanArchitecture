package com.elvotra.clean.presentation.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.elvotra.clean.R;
import com.elvotra.clean.data.local.TypicodeDatabase;
import com.elvotra.clean.data.local.TypicodeLocalDataSource;
import com.elvotra.clean.data.remote.TypicodeRemoteDataSource;
import com.elvotra.clean.data.repository.PostsRepositoryImp;
import com.elvotra.clean.domain.executor.impl.ThreadExecutor;
import com.elvotra.clean.presentation.presenter.imp.PostsPresenterImp;
import com.elvotra.clean.presentation.ui.fragments.PostsListFragment;
import com.elvotra.clean.threading.MainThreadImpl;
import com.elvotra.clean.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsActivity extends AppCompatActivity {

    @BindView(R.id.act_posts_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        ButterKnife.bind(this);

        PostsListFragment postsListFragment =
                (PostsListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (postsListFragment == null) {
            // Create the fragment
            postsListFragment = PostsListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), postsListFragment, R.id.contentFrame);
        }

        new PostsPresenterImp(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                postsListFragment,
                PostsRepositoryImp.getInstance(TypicodeRemoteDataSource.getInstance(),
                        TypicodeLocalDataSource.getInstance(TypicodeDatabase.getInstance(PostsActivity.this).typicodeDao())));

    }
}
