package com.example.ptdd_btl_qlct_n7_final2.dao;

import androidx.room.Dao;
import androidx.room.Query;


import com.example.ptdd_btl_qlct_n7_final2.entity.Category;

import java.util.List;

@Dao
public interface CategoryDAO extends DAO<Category>{

    @Query(value = "Select * from Category")
    List<Category> getAll();

}
