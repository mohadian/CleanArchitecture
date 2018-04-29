package com.elvotra.clean.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.elvotra.clean.data.local.model.CommentEntity;
import com.elvotra.clean.data.local.model.PostEntity;
import com.elvotra.clean.data.local.model.UserEntity;

@Database(entities = {PostEntity.class, UserEntity.class, CommentEntity.class}, version = 2)
public abstract class TypicodeDatabase extends RoomDatabase {

    private static TypicodeDatabase INSTANCE;

    public abstract TypicodeDao typicodeDao();

    private static final Object lock = new Object();

    public static TypicodeDatabase getInstance(Context context) {
        synchronized (lock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        TypicodeDatabase.class, "Typicode.db")
                        .build();
            }
            return INSTANCE;
        }
    }

}
