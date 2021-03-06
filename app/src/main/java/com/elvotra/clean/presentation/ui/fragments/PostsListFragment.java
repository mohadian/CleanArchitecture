package com.elvotra.clean.presentation.ui.fragments;

import android.content.Intent;
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
import com.elvotra.clean.presentation.contract.PostsContract;
import com.elvotra.clean.presentation.model.PostViewItem;
import com.elvotra.clean.presentation.ui.activities.PostDetailsActivity;
import com.elvotra.clean.presentation.ui.adapters.PostsRecyclerAdapter;
import com.elvotra.clean.presentation.ui.widgets.ScrollChildSwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.elvotra.clean.presentation.ui.activities.PostDetailsActivity.POST_ID;

public class PostsListFragment extends Fragment implements PostsContract.View {

    private PostsContract.IPostsPresenter postsPresenter;

    private PostsRecyclerAdapter postsRecyclerAdapter;

    @BindView(R.id.fragment_posts_list_recycler_view)
    RecyclerView postsRecyclerView;
    @BindView(R.id.fragment_posts_list_message)
    TextView errorMessage;
    @BindView(R.id.refresh_layout)
    ScrollChildSwipeRefreshLayout swipeRefreshLayout;

    public PostsListFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_posts_list, container, false);

        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        postsRecyclerView.setLayoutManager(mLayoutManager);

        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        swipeRefreshLayout.setScrollUpChild(postsRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postsPresenter.loadPosts(true);
            }
        });
        postsPresenter.loadPosts(true);

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(PostsContract.IPostsPresenter presenter) {
        postsPresenter = presenter;
    }

    @Override
    public void showPostsList(List<PostViewItem> postViewItems) {
        errorMessage.setVisibility(View.GONE);

        postsRecyclerView.setVisibility(View.VISIBLE);

        postsRecyclerAdapter = new PostsRecyclerAdapter(getContext(), postViewItems,
                new PostsRecyclerAdapter.PostsListItemClickListener() {

                    @Override
                    public void onPostClicked(int postId) {
                        postsPresenter.openPostDetails(postId);
                    }

                });

        postsRecyclerView.setAdapter(postsRecyclerAdapter);
    }

    @Override
    public void showPostDetails(int postId) {
        Intent i = new Intent(getActivity(), PostDetailsActivity.class);
        i.putExtra(POST_ID, postId);
        startActivity(i);
    }

    @Override
    public void showNoResults() {
        postsRecyclerView.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(getString(R.string.no_posts));
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
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
