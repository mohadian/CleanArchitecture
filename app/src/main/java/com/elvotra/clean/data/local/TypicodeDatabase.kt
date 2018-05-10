package com.elvotra.clean.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

import com.elvotra.clean.data.local.model.CommentEntity
import com.elvotra.clean.data.local.model.PostEntity
import com.elvotra.clean.data.local.model.UserEntity

@Database(entities = arrayOf(PostEntity::class, UserEntity::class, CommentEntity::class), version = 2, exportSchema = false)
abstract class TypicodeDatabase : RoomDatabase() {

    abstract fun typicodeDao(): TypicodeDao

    companion object {

        private var INSTANCE: TypicodeDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): TypicodeDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            TypicodeDatabase::class.java, "Typicode.db")
                            .build()
                }
                return INSTANCE!!
            }
        }
    }

}
