package com.example.ptdd_btl_qlct_n7_final2.dao;

import androidx.room.Dao;
import androidx.room.Query;


import com.example.ptdd_btl_qlct_n7_final2.entity.Transactions;

import java.util.List;

@Dao
public interface TransactionsDAO extends DAO<Transactions> {

    @Query(value = "Select * from Transactions")
    List<Transactions> getAll();

}
