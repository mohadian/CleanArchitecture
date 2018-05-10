package com.elvotra.clean.presentation.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.elvotra.clean.R;
import com.elvotra.clean.presentation.contract.PostDetailsContract;
import com.elvotra.clean.presentation.model.PostDetailsViewItem;
import com.elvotra.clean.presentation.ui.adapters.CommentsRecyclerAdapter;

import timber.log.Timber;

public class PostDetailsFragment extends Fragment implements PostDetailsContract.View {

    public interface PostDetailsToolbarCallback {
        void updateToolbar(String avatarUrl, String username);
    }

    private PostDetailsContract.IPostDetailsPresenter postDetailsPresenter;

    View post_details_container;
    LottieAnimationView post_details_progress;
    TextView post_details_title;
    TextView post_details_body;

    RecyclerView fragment_comments_list_recycler_view;
    TextView fragment_post_details_message;

    public PostDetailsFragment() {
    }

    public static PostDetailsFragment newInstance(int postId) {
        PostDetailsFragment fragment = new PostDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(com.elvotra.clean.presentation.ui.activities.PostDetailsActivity.Companion.getPOST_ID(), postId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_details, container, false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        fragment_comments_list_recycler_view.setLayoutManager(mLayoutManager);

        Bundle args = getArguments();
        int postId = -1;
        if (args != null) {
            postId = args.getInt(com.elvotra.clean.presentation.ui.activities.PostDetailsActivity.Companion.getPOST_ID());
        }

        setupProgressAnimation();

        postDetailsPresenter.loadPost(postId);

        return rootView;
    }

    private void setupProgressAnimation() {
        post_details_progress.setAnimation(R.raw.loading);
    }

    @Override
    public void onResume() {
        super.onResume();
        postDetailsPresenter.resume();
    }

    @Override
    public void setPresenter(PostDetailsContract.IPostDetailsPresenter presenter) {
        postDetailsPresenter = presenter;
    }

    @Override
    public PostDetailsContract.IPostDetailsPresenter getPresenter(){
        return postDetailsPresenter;
    }

    @Override
    public void showPostDetails(PostDetailsViewItem postDetailsViewItem) {
        Timber.d("Received the post details data");

        post_details_container.setVisibility(View.VISIBLE);
        post_details_title.setText(postDetailsViewItem.getTitle());
        post_details_body.setText(postDetailsViewItem.getBody());
        fragment_post_details_message.setVisibility(View.GONE);
        fragment_comments_list_recycler_view.setVisibility(View.VISIBLE);

        CommentsRecyclerAdapter commentsRecyclerAdapter = new CommentsRecyclerAdapter(getContext(), postDetailsViewItem.getComments());

        fragment_comments_list_recycler_view.setAdapter(commentsRecyclerAdapter);

        updateAvatar(postDetailsViewItem.getAvatar(), postDetailsViewItem.getUser());
    }

    private void updateAvatar(String avatar, String username) {
        if (getActivity() instanceof PostDetailsToolbarCallback) {
            ((PostDetailsToolbarCallback) getActivity()).updateToolbar(avatar, username);
        }
    }

    @Override
    public void showNoResults() {
        fragment_comments_list_recycler_view.setVisibility(View.GONE);
        fragment_post_details_message.setVisibility(View.VISIBLE);
        post_details_container.setVisibility(View.GONE);
        fragment_post_details_message.setText(getString(R.string.no_posts));
    }

    @Override
    public void showProgress() {
        post_details_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        post_details_progress.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        fragment_comments_list_recycler_view.setVisibility(View.GONE);
        fragment_post_details_message.setVisibility(View.VISIBLE);
        fragment_post_details_message.setText(message);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


}
