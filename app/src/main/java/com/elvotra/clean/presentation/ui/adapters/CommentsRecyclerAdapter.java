package com.elvotra.clean.presentation.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elvotra.clean.R;
import com.elvotra.clean.presentation.model.PostCommentViewItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.request.RequestOptions.circleCropTransform;

public class CommentsRecyclerAdapter
        extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    private List<PostCommentViewItem> commentViewItems;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item_post_user_avatar)
        ImageView userAvatar;
        @BindView(R.id.list_item_comment_body)
        TextView commentBody;
        @BindView(R.id.list_item_comment_user)
        TextView commentUser;

        public ViewHolder(View item) {
            super(item);

            ButterKnife.bind(this, item);
        }
    }

    public CommentsRecyclerAdapter(Context context, List<PostCommentViewItem> postViewItems) {
        this.context = context;
        this.commentViewItems = postViewItems;

    }

    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_post, parent, false);

        CommentsRecyclerAdapter.ViewHolder vh = new CommentsRecyclerAdapter.ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(CommentsRecyclerAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(commentViewItems.get(position).getAvatar())
                .apply(circleCropTransform())
                .into(holder.userAvatar);
        holder.commentBody.setText(commentViewItems.get(position).getBody());
        holder.commentUser.setText(commentViewItems.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return commentViewItems.size();
    }

}



