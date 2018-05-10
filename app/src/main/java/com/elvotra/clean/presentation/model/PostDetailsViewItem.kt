package com.elvotra.clean.presentation.model

data class PostDetailsViewItem(val id: Int, val user: String, val useremail: String, val avatar: String, val title: String, val body: String, val comments: List<PostCommentViewItem>)