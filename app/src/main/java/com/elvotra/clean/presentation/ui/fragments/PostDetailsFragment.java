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

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.elvotra.clean.presentation.ui.activities.PostDetailsActivity.POST_ID;

public class PostDetailsFragment extends Fragment implements PostDetailsContract.View {

    public interface PostDetailsToolbarCallback {
        void updateToolbar(String avatarUrl, String username);
    }

    private PostDetailsContract.IPostDetailsPresenter postDetailsPresenter;

    @BindView(R.id.post_details_container)
    View detailsContainer;
    @BindView(R.id.post_details_progress)
    LottieAnimationView postDetailsProgress;
    @BindView(R.id.post_details_title)
    TextView postTitle;
    @BindView(R.id.post_details_body)
    TextView postBody;

    @BindView(R.id.fragment_comments_list_recycler_view)
    RecyclerView postsRecyclerView;
    @BindView(R.id.fragment_post_details_message)
    TextView errorMessage;

    public PostDetailsFragment() {
    }

    public static PostDetailsFragment newInstance(int postId) {
        PostDetailsFragment fragment = new PostDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POST_ID, postId);
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

        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        postsRecyclerView.setLayoutManager(mLayoutManager);

        Bundle args = getArguments();
        int postId = -1;
        if (args != null) {
            postId = args.getInt(POST_ID);
        }

        setupProgressAnimation();

        postDetailsPresenter.loadPost(postId);

        return rootView;
    }

    private void setupProgressAnimation() {
        postDetailsProgress.setAnimation(R.raw.loading);
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
    public void showPostDetails(PostDetailsViewItem postDetailsViewItem) {
        Timber.d("Received the post details data");

        detailsContainer.setVisibility(View.VISIBLE);
        postTitle.setText(postDetailsViewItem.getTitle());
        postBody.setText(postDetailsViewItem.getBody());
        errorMessage.setVisibility(View.GONE);
        postsRecyclerView.setVisibility(View.VISIBLE);

        CommentsRecyclerAdapter commentsRecyclerAdapter = new CommentsRecyclerAdapter(getContext(), postDetailsViewItem.getComments());

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
        detailsContainer.setVisibility(View.GONE);
        errorMessage.setText(getString(R.string.no_posts));
    }

    @Override
    public void showProgress() {
        postDetailsProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        postDetailsProgress.setVisibility(View.GONE);
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
