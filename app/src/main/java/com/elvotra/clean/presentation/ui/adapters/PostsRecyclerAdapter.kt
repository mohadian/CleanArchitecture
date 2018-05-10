package com.elvotra.clean.presentation.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.circleCropTransform
import com.elvotra.clean.R
import com.elvotra.clean.presentation.model.PostViewItem

class PostsRecyclerAdapter(private val context: Context, private val postViewItems: List<PostViewItem>,
                           private val listener: PostsListItemClickListener) : RecyclerView.Adapter<PostsRecyclerAdapter.ViewHolder>() {

    interface PostsListItemClickListener {

        fun onPostClicked(postId: Int)

    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item), View.OnClickListener {

        var list_item_post_user_avatar: ImageView? = null
        var list_item_post_title: TextView? = null
        var list_item_post_body: TextView? = null
        var list_item_post_user: TextView? = null
        var list_item_post_comments_count: TextView? = null

        init {

            item.setOnClickListener(this)

        }

        override fun onClick(view: View) {

            listener.onPostClicked(postViewItems[adapterPosition].id)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_post, parent, false)

        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: PostsRecyclerAdapter.ViewHolder, position: Int) {
        Glide.with(context)
                .load(postViewItems[position].avatar)
                .apply(circleCropTransform())
                .into(holder.list_item_post_user_avatar!!)
        holder.list_item_post_title!!.text = Html.fromHtml(postViewItems[position].title)
        holder.list_item_post_body!!.text = postViewItems[position].body
        holder.list_item_post_user!!.text = postViewItems[position].user
        val commentsCount = postViewItems[position].commentsCount
        val comments = if (TextUtils.isEmpty(commentsCount)) context.getString(R.string.no_comments) else context.getString(R.string.posts_comments, commentsCount)
        holder.list_item_post_comments_count!!.text = comments
    }

    override fun getItemCount(): Int {
        return postViewItems.size
    }
}



