package com.elvotra.clean.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "posts")
class PostEntity(@field:PrimaryKey
                 @field:ColumnInfo(name = "posts_id")
                 val id: Int, @field:ColumnInfo(name = "user_id")
                 val userId: Int, @field:ColumnInfo(name = "title")
                 val title: String, @field:ColumnInfo(name = "body")
                 val body: String) {

    override fun toString(): String {
        return "PostEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\''.toString() +
                ", body='" + body + '\''.toString() +
                '}'.toString()
    }
}
