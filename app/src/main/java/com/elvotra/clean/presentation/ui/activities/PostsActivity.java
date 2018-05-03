package com.elvotra.clean.presentation.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.elvotra.clean.R;
import com.elvotra.clean.presentation.di.Injector;
import com.elvotra.clean.presentation.presenter.PostsPresenter;
import com.elvotra.clean.presentation.ui.fragments.PostsListFragment;
import com.elvotra.clean.utils.ActivityUtils;

import butterknife.ButterKnife;

public class PostsActivity extends AppCompatActivity {

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

        new PostsPresenter(postsListFragment, Injector.provideGetPostsUseCase(this), Injector.provideUseCaseHandler());
    }
}
