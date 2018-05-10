package com.elvotra.clean.presentation.model.mapper

import com.elvotra.clean.domain.model.Comment
import com.elvotra.clean.domain.model.Post
import com.elvotra.clean.domain.model.User
import com.elvotra.clean.presentation.model.PostCommentViewItem
import com.elvotra.clean.presentation.model.PostDetailsViewItem
import com.elvotra.clean.utils.Constants
import java.util.*

class PostDetailsViewItemMapper private constructor() {

    fun transform(post: Post): PostDetailsViewItem {
        val comments = post.comments
        val postCommentViewItems = ArrayList<PostCommentViewItem>()
        comments?.forEach { comment: Comment ->
            val (_, id, name, email, body) = comment
            val avatar = Constants.AVATAR_BASE_URL + email
            val commentViewItem = PostCommentViewItem(id, name, email, avatar, body)
            postCommentViewItems.add(commentViewItem)
        }
        val user = post.user
        return transform(post, user, postCommentViewItems)
    }

    fun transform(post: Post, user: User?, commentViewItems: List<PostCommentViewItem>): PostDetailsViewItem {
        val username = user?.name ?: "Unknown"
        val useremail = if (user != null && user.email != null) user.email else ""
        val avatar = Constants.AVATAR_BASE_URL + if (user != null && user.email != null) user.email else ""

        return PostDetailsViewItem(post.id, username, useremail, avatar, post.title, post.body, commentViewItems)
    }

    companion object {

        private var INSTANCE: PostDetailsViewItemMapper? = null

        fun instance(): PostDetailsViewItemMapper {
            return PostDetailsViewItemMapper.INSTANCE ?: PostDetailsViewItemMapper()
                    .apply { PostDetailsViewItemMapper.INSTANCE = this }
        }
    }
}
