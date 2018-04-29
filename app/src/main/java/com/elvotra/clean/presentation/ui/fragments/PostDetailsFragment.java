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
import com.elvotra.clean.presentation.presenter.IPostDetailsPresenter;
import com.elvotra.clean.presentation.ui.adapters.CommentsRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetailsFragment extends Fragment implements IPostDetailsPresenter.View {

    public interface PostDetailsToolbarCallback {
        void updateToolbar(String avatarUrl, String username);
    }

    private IPostDetailsPresenter postDetailsPresenter;

    private CommentsRecyclerAdapter commentsRecyclerAdapter;

    @BindView(R.id.post_details_title)
    TextView postTitle;
    @BindView(R.id.post_details_body)
    TextView mLblOverview;

    @BindView(R.id.fragment_comments_list_recycler_view)
    RecyclerView postsRecyclerView;
    @BindView(R.id.fragment_post_details_message)
    TextView errorMessage;

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
        View rootView = inflater.inflate(R.layout.fragment_post_details, container, false);

        ButterKnife.bind(this, rootView);

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
    public void setPresenter(IPostDetailsPresenter presenter) {
        postDetailsPresenter = presenter;
    }

    @Override
    public void showPostDetails(PostDetailsViewItem postDetailsViewItem) {
        postTitle.setText(postDetailsViewItem.getTitle());

        mLblOverview.setText(postDetailsViewItem.getBody());

        errorMessage.setVisibility(View.GONE);

        postsRecyclerView.setVisibility(View.VISIBLE);

        commentsRecyclerAdapter = new CommentsRecyclerAdapter(getContext(), postDetailsViewItem.getComments());

        postsRecyclerView.setAdapter(commentsRecyclerAdapter);

        updateAvatar(postDetailsViewItem.getAvatar(), postDetailsViewItem.getUser());
    }

    private void updateAvatar(String avatar, String username) {
        if (getActivity() instanceof PostDetailsToolbarCallback) {
            ((PostDetailsToolbarCallback) getActivity()).updateToolbar(avatar, username);
        }
    }

    @Override
    public void showNoResults() {
        postsRecyclerView.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(getString(R.string.no_posts));
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String message) {

        postsRecyclerView.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);

    }
}
