package com.elvotra.clean.presentation.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.circleCropTransform
import com.elvotra.clean.R
import com.elvotra.clean.presentation.model.PostCommentViewItem

class CommentsRecyclerAdapter(private val context: Context, private val commentViewItems: List<PostCommentViewItem>) : RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        var list_item_post_user_avatar: ImageView? = null
        var list_item_comment_body: TextView? = null
        var list_item_comment_user: TextView? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_comment, parent, false)

        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: CommentsRecyclerAdapter.ViewHolder, position: Int) {
        Glide.with(context)
                .load(commentViewItems[position].avatar)
                .apply(circleCropTransform())
                .into(holder.list_item_post_user_avatar!!)
        holder.list_item_comment_body!!.text = commentViewItems[position].body
        holder.list_item_comment_user!!.text = commentViewItems[position].name
    }

    override fun getItemCount(): Int {
        return commentViewItems.size
    }
}



