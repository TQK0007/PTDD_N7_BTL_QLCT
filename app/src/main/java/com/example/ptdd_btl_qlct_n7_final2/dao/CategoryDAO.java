package com.example.ptdd_btl_qlct_n7_final2.dao;

import androidx.room.Dao;
import androidx.room.Query;


import com.example.ptdd_btl_qlct_n7_final2.dto.CategoryDTO;
import com.example.ptdd_btl_qlct_n7_final2.entity.Category;

import java.util.Date;
import java.util.List;

@Dao
public interface CategoryDAO extends DAO<Category>{

    @Query(value = "Select * from Category")
    List<Category> getAll();

    @Query(value = "Select * from Category where isIncome = :isIncone")
    List<Category> getAllByIsIncome(boolean isIncone);


    @Query(value = "Select categoryName,iconName,isIncome,Transactions.amount " +
            "from Category inner join Transactions on Category.categoryId = Transactions.categoryId " +
            "where strftime('%Y-%m', Transactions.createdAt) like :date " +
            "group by categoryName")
    List<CategoryDTO> getAllByDate(String date);
}
