package com.example.ptdd_btl_qlct_n7_final2.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface DAO <T>{
    @Insert
    public void add(T t);

    @Update
    public void update(T t);

    @Delete
    public void delete(T t);
}
