package com.elvotra.clean.presentation.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elvotra.clean.R;
import com.elvotra.clean.presentation.model.PostViewItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsRecyclerAdapter
        extends RecyclerView.Adapter<PostsRecyclerAdapter.ViewHolder> {

    public interface MovieListItemClickListener {

        void onMovieClicked(long movieId);

    }

    private List<PostViewItem> postViewItems;
    private PostsRecyclerAdapter.MovieListItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.list_item_post_user_avatar)
        ImageView userAvatar;
        @BindView(R.id.list_item_post_title)
        TextView postTitle;
        @BindView(R.id.list_item_post_body)
        TextView postBody;
        @BindView(R.id.list_item_post_user)
        TextView postUser;
        @BindView(R.id.list_item_post_comments_count)
        TextView postCommentsCount;

        public ViewHolder(View item) {
            super(item);

            ButterKnife.bind(this, item);
            item.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            listener.onMovieClicked(postViewItems.get(getAdapterPosition()).getId());

        }

    }

    public PostsRecyclerAdapter(List<PostViewItem> postViewItems,
                                PostsRecyclerAdapter.MovieListItemClickListener listener) {

        this.postViewItems = postViewItems;
        this.listener = listener;

    }

    @Override
    public PostsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_post, parent, false);

        PostsRecyclerAdapter.ViewHolder vh = new PostsRecyclerAdapter.ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(PostsRecyclerAdapter.ViewHolder holder, int position) {

        Picasso.get()
                .load(postViewItems.get(position).getAvatar())
                .resize(100, 150)
                .centerCrop()
                .into(holder.userAvatar);
        holder.postTitle.setText(Html.fromHtml(postViewItems.get(position).getTitle()));
        holder.postBody.setText(postViewItems.get(position).getBody());
        holder.postUser.setText(postViewItems.get(position).getUser());
        holder.postCommentsCount.setText("" + postViewItems.get(position).getCommentsCount());

    }

    @Override
    public int getItemCount() {

        return postViewItems.size();

    }

}



