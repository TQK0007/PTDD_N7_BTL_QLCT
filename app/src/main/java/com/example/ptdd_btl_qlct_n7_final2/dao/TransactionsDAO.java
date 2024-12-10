package com.example.ptdd_btl_qlct_n7_final2.dao;

import androidx.room.Dao;
import androidx.room.Query;


import com.example.ptdd_btl_qlct_n7_final2.dto.TransactionsDTO;
import com.example.ptdd_btl_qlct_n7_final2.entity.Transactions;

import java.util.List;

@Dao
public interface TransactionsDAO extends DAO<Transactions> {

    @Query(value = "Select * from Transactions")
    List<Transactions> getAll();

    @Query(value = "select * from Transactions where transactionId = :id")
    Transactions findByID(int id);

    @Query(value = "select transactionId,amount,createdAt, Category.categoryId,categoryName,iconName " +
            " from Transactions inner join Category on Transactions.categoryId=Category.categoryId " +
            "where Category.isIncome = :isIncome")
    List<TransactionsDTO> getAllTransactionsDtoByIncome(boolean isIncome);

}
