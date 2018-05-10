package com.elvotra.clean.data.remote.model.mapper

import com.elvotra.clean.data.remote.model.PostCommentData
import com.elvotra.clean.data.remote.model.PostData
import com.elvotra.clean.data.remote.model.UserData
import com.elvotra.clean.domain.model.Comment
import com.elvotra.clean.domain.model.Post
import com.elvotra.clean.domain.model.User
import java.util.*

class PostsResponseMapper private constructor() {

    //region Data to Domain models
    fun transform(response: ArrayList<PostData>?, userData: ArrayList<UserData>?, postCommentData: ArrayList<PostCommentData>?): ArrayList<Post> {
        val result = ArrayList<Post>()
        val userDataMap = createMap(userData)
        val postCommentsCount = createPostCommentsMap(postCommentData)

        response?.forEach { postData: PostData ->
            val (userId, id, title, body) = postData
            val user = userDataMap.get(userId)
            val comments = postCommentsCount[id]
            val post = Post(id, user, title, body, comments)
            result.add(post)
        }

        return result
    }

    private fun createPostCommentsMap(postCommentDataList: ArrayList<PostCommentData>?): Map<Int, ArrayList<Comment>?> {
        val commentsDataMap = HashMap<Int, ArrayList<Comment>?>()
        var comments: ArrayList<Comment>?

        postCommentDataList?.forEach { postCommentData: PostCommentData ->
            val (postId, id, name, email, body) = postCommentData
            comments = if (commentsDataMap.containsKey(postId)) commentsDataMap.get(postId) else ArrayList()
            comments!!.add(Comment(postId, id, name, email, body))
            commentsDataMap.put(postId, comments)
        }

        return commentsDataMap
    }

    private fun createMap(usersData: ArrayList<UserData>?): Map<Int, User> {
        val userDataMap = HashMap<Int, User>()

        usersData?.forEach { userData: UserData ->
            val (id, name, username, email) = userData
            val user = User(id, name, username, email)
            userDataMap[id] = user
        }

        return userDataMap
    }

    companion object {
        private var INSTANCE: PostsResponseMapper = PostsResponseMapper()

        val instance: PostsResponseMapper
            get() {
                return INSTANCE
            }
    }

    //endregion
}
