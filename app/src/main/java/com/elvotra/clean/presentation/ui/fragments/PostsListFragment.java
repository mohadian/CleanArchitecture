package com.elvotra.clean.presentation.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elvotra.clean.R;
import com.elvotra.clean.presentation.model.PostViewItem;
import com.elvotra.clean.presentation.presenter.PostsPresenter;
import com.elvotra.clean.presentation.ui.adapters.PostsRecyclerAdapter;
import com.elvotra.clean.presentation.ui.widgets.ScrollChildSwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsListFragment extends Fragment implements PostsPresenter.View {

    private PostsPresenter postsPresenter;

    private PostsRecyclerAdapter postsRecyclerAdapter;

    @BindView(R.id.fragment_posts_list_recycler_view)
    RecyclerView postsRecyclerView;
    @BindView(R.id.fragment_posts_list_message)
    TextView mLblMessage;
    @BindView(R.id.refresh_layout)
    ScrollChildSwipeRefreshLayout swipeRefreshLayout;

    public PostsListFragment() {
        // Required empty public constructor
    }

    public static PostsListFragment newInstance() {
        PostsListFragment fragment = new PostsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_posts_list, container, false);

        ButterKnife.bind(this, rootView);

        // Setear layout de la lista
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        postsRecyclerView.setLayoutManager(mLayoutManager);

        // Set up progress indicator
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(postsRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postsPresenter.loadPosts();
            }
        });

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        postsPresenter.resume();
    }

    @Override
    public void setPresenter(PostsPresenter presenter) {
        postsPresenter = presenter;
    }

    @Override
    public void showPostsList(List<PostViewItem> postViewItems) {
        mLblMessage.setVisibility(View.GONE);

        postsRecyclerView.setVisibility(View.VISIBLE);

        // Especificamos un adaptador (y tambien oyente para evento OnClick de cada elemento)
        postsRecyclerAdapter = new PostsRecyclerAdapter(postViewItems,
                new PostsRecyclerAdapter.MovieListItemClickListener() {

                    @Override
                    public void onMovieClicked(long movieId) {
                        postsPresenter.openPostDetails(movieId);
                    }

                });


        // Setear el adaptador al RecyclerView
        postsRecyclerView.setAdapter(postsRecyclerAdapter);
    }

    @Override
    public void showPostDetails(long postId) {

    }

    @Override
    public void showNoResults() {
        postsRecyclerView.setVisibility(View.GONE);
        mLblMessage.setVisibility(View.VISIBLE);
        mLblMessage.setText(getString(R.string.no_posts));
    }

    @Override
    public void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {

        postsRecyclerView.setVisibility(View.GONE);
        mLblMessage.setVisibility(View.VISIBLE);
        mLblMessage.setText(message);

    }
}
