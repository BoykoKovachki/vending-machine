package com.example.vendingmachine.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RowItemsDao {

    @Insert
    void insert(RowItems items);

    @Update
    void update(RowItems items);

    @Delete
    void delete(RowItems items);

    @Query("SELECT * FROM items_table ORDER BY id ASC")
    LiveData<List<RowItems>> getAllItems();

}
