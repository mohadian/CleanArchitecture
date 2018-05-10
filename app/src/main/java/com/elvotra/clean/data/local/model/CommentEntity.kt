package com.elvotra.clean.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "comments")
class CommentEntity(@field:PrimaryKey
                    @field:ColumnInfo(name = "comments_id")
                    val id: Int, @field:ColumnInfo(name = "post_id")
                    val postId: Int, @field:ColumnInfo(name = "name")
                    val name: String, @field:ColumnInfo(name = "email")
                    val email: String, @field:ColumnInfo(name = "body")
                    val body: String) {

    override fun toString(): String {
        return "CommentEntity{" +
                "id=" + id +
                ", postId=" + postId +
                ", name='" + name + '\''.toString() +
                ", email='" + email + '\''.toString() +
                ", body='" + body + '\''.toString() +
                '}'.toString()
    }
}
