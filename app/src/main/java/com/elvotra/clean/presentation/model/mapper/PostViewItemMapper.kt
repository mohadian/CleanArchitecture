package com.elvotra.clean.presentation.model.mapper

import com.elvotra.clean.domain.model.Post
import com.elvotra.clean.domain.model.User
import com.elvotra.clean.presentation.model.PostViewItem
import com.elvotra.clean.utils.Constants
import java.util.*

class PostViewItemMapper private constructor() {

    fun transform(posts: List<Post>): List<PostViewItem> {
        val postViewItems = ArrayList<PostViewItem>(posts.size)
        for (i in posts.indices) {
            val post = posts[i]
            postViewItems.add(transform(post, post.user))
        }
        return postViewItems
    }

    fun transform(post: Post, user: User?): PostViewItem {
        val username = user?.name ?: "Unknown"
        val useremail = if (user != null && user.email != null) user.email else ""
        val avatar = Constants.AVATAR_BASE_URL + if (user != null && user.email != null) user.email else ""
        val comments = if (post.comments != null && post.comments.size > 0) post.comments.size.toString() else ""

        return PostViewItem(post.id, username, useremail, avatar, post.title, post.body, comments)
    }

    companion object {
        private var INSTANCE: PostViewItemMapper? = null

        fun instance(): PostViewItemMapper {
            return PostViewItemMapper.INSTANCE ?: PostViewItemMapper()
                    .apply { PostViewItemMapper.INSTANCE = this }
        }
    }

}
