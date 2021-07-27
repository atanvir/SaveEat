package com.saveeat.repository.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RestaurantEntity.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RestaurantDao dao();
}