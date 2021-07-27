package com.saveeat.base;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@Dao
public interface BaseDao<T>  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T t);

    @Update
    void update(T t);

    @Delete
    void delete(T t);
}