package com.elvotra.clean.domain.model

data class Post(val id: Int, val user: User?, val title: String, val body: String, val comments: List<Comment>?)