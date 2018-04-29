package com.elvotra.clean.presentation.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elvotra.clean.R;
import com.elvotra.clean.data.local.TypicodeDatabase;
import com.elvotra.clean.data.local.TypicodeLocalDataSource;
import com.elvotra.clean.data.remote.TypicodeRemoteDataSource;
import com.elvotra.clean.data.repository.PostsRepositoryImp;
import com.elvotra.clean.domain.executor.impl.ThreadExecutor;
import com.elvotra.clean.presentation.model.PostDetailsViewItem;
import com.elvotra.clean.presentation.presenter.PostDetailsPresenter;
import com.elvotra.clean.presentation.presenter.imp.PostDetailsPresenterImp;
import com.elvotra.clean.threading.AppExecutors;
import com.elvotra.clean.threading.MainThreadImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetailsActivity extends AppCompatActivity implements PostDetailsPresenter.View {
    public static final String POST_ID = "POST_ID";

    private PostDetailsPresenter presenter;

    @BindView(R.id.act_movie_details_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.act_movie_details_toolbar_image)
    ImageView mImageToolbar;
    @BindView(R.id.act_movie_details_lbl_title)
    TextView mLblTitle;
    @BindView(R.id.act_movie_details_lbl_original_title)
    TextView mLblOriginalTitle;
    @BindView(R.id.act_movie_details_lbl_genres)
    TextView mLblGenres;
    @BindView(R.id.act_movie_details_lbl_tagline)
    TextView mLblTagline;
    @BindView(R.id.act_movie_details_lbl_overview)
    TextView mLblOverview;
    @BindView(R.id.act_movie_details_lbl_runtime)
    TextView mLblRuntime;
    @BindView(R.id.act_movie_details_lbl_release_date)
    TextView mLblReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_activity);

        ButterKnife.bind(this);

        int postId = getIntent().getIntExtra(POST_ID, -1);

        setUpToolbar();

        presenter = new PostDetailsPresenterImp(postId,
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                PostDetailsActivity.this,
                PostsRepositoryImp.getInstance(TypicodeRemoteDataSource.getInstance(),
                        TypicodeLocalDataSource.getInstance(new AppExecutors(), TypicodeDatabase.getInstance(PostDetailsActivity.this).typicodeDao())));

        presenter.loadPost(postId);

    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void setPresenter(PostDetailsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPostDetails(PostDetailsViewItem postDetailsViewItem) {
        Glide.with(getApplicationContext())
                .load(postDetailsViewItem.getAvatar())
                .into(mImageToolbar);
        mLblTitle.setText(postDetailsViewItem.getTitle());

        mLblOverview.setText(postDetailsViewItem.getBody());

    }

    @Override
    public void showNoResults() {

    }

    private void setUpToolbar() {


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Para que se ejecute la animación de forma inversa al cerrar la Activity
                supportFinishAfterTransition();
            }
        });

        // Eliminamos el Titulo de la Toolbar
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.act_movie_details_toolbar_layout);
        toolbarLayout.setTitle(" ");

    }


}
