package com.elvotra.clean.presentation.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elvotra.clean.R;
import com.elvotra.clean.presentation.model.PostDetailsViewItem;
import com.elvotra.clean.presentation.presenter.PostDetailsPresenter;
import com.elvotra.clean.presentation.ui.adapters.CommentsRecyclerAdapter;
import com.elvotra.clean.presentation.ui.widgets.ScrollChildSwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetailsFragment extends Fragment implements PostDetailsPresenter.View {

    private PostDetailsPresenter postDetailsPresenter;

    private CommentsRecyclerAdapter commentsRecyclerAdapter;

    @BindView(R.id.fragment_posts_list_recycler_view)
    RecyclerView postsRecyclerView;
    @BindView(R.id.fragment_posts_list_message)
    TextView mLblMessage;
    @BindView(R.id.refresh_layout)
    ScrollChildSwipeRefreshLayout swipeRefreshLayout;

    public PostDetailsFragment() {
    }

    public static PostDetailsFragment newInstance() {
        PostDetailsFragment fragment = new PostDetailsFragment();
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

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        postDetailsPresenter.resume();
    }

    @Override
    public void setPresenter(PostDetailsPresenter presenter) {
        postDetailsPresenter = presenter;
    }

    @Override
    public void showPostDetails(PostDetailsViewItem postDetailsViewItem) {
        mLblMessage.setVisibility(View.GONE);

        postsRecyclerView.setVisibility(View.VISIBLE);

        commentsRecyclerAdapter = new CommentsRecyclerAdapter(getContext(), postDetailsViewItem.getComments());

        postsRecyclerView.setAdapter(commentsRecyclerAdapter);
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
