package com.elvotra.clean.presentation.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elvotra.clean.R;
import com.elvotra.clean.presentation.model.PostViewItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.request.RequestOptions.circleCropTransform;

public class PostsRecyclerAdapter
        extends RecyclerView.Adapter<PostsRecyclerAdapter.ViewHolder> {

    public interface PostsListItemClickListener {

        void onPostClicked(int postId);

    }

    private List<PostViewItem> postViewItems;
    private PostsListItemClickListener listener;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder
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

            listener.onPostClicked(postViewItems.get(getAdapterPosition()).getId());

        }

    }

    public PostsRecyclerAdapter(Context context, List<PostViewItem> postViewItems,
                                PostsListItemClickListener listener) {
        this.context = context;
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
        Glide.with(context)
                .load(postViewItems.get(position).getAvatar())
                .apply(circleCropTransform())
                .into(holder.userAvatar);
        holder.postTitle.setText(Html.fromHtml(postViewItems.get(position).getTitle()));
        holder.postBody.setText(postViewItems.get(position).getBody());
        holder.postUser.setText(postViewItems.get(position).getUser());
        String commentsCount = postViewItems.get(position).getCommentsCount();
        String comments = (TextUtils.isEmpty(commentsCount)) ? context.getString(R.string.no_comments) : context.getString(R.string.posts_comments, commentsCount);
        holder.postCommentsCount.setText(comments);
    }

    @Override
    public int getItemCount() {

        return postViewItems.size();

    }

}



