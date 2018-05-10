package com.elvotra.clean.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "users")
class UserEntity(@field:PrimaryKey
                 @field:ColumnInfo(name = "users_id")
                 val id: Int, @field:ColumnInfo(name = "name")
                 val name: String, @field:ColumnInfo(name = "username")
                 val username: String, @field:ColumnInfo(name = "email")
                 val email: String) {

    override fun toString(): String {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\''.toString() +
                ", username='" + username + '\''.toString() +
                ", email='" + email + '\''.toString() +
                '}'.toString()
    }
}
