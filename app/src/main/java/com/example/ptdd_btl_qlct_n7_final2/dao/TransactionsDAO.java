package com.example.ptdd_btl_qlct_n7_final2.dao;

import androidx.room.Dao;
import androidx.room.Query;


import com.example.ptdd_btl_qlct_n7_final2.dto.DataBarChartDTO;
import com.example.ptdd_btl_qlct_n7_final2.dto.TransactionsDTO;
import com.example.ptdd_btl_qlct_n7_final2.entity.Transactions;

import java.util.List;
import java.util.Map;

@Dao
public interface TransactionsDAO extends DAO<Transactions> {

    @Query(value = "Select * from Transactions")
    List<Transactions> getAll();

    @Query(value = "select * from Transactions where transactionId = :id")
    Transactions findByID(int id);

    @Query(value = "select transactionId,amount,createdAt,note, Category.categoryId,categoryName,iconName " +
            " from Transactions inner join Category on Transactions.categoryId=Category.categoryId " +
            "where Category.isIncome = :isIncome")
    List<TransactionsDTO> getAllTransactionsDtoByIncome(boolean isIncome);

    @Query(value = "select transactionId,amount,createdAt,note, Category.categoryId,categoryName,iconName,isIncome " +
            " from Transactions inner join Category on Transactions.categoryId=Category.categoryId " +
            "where Category.categoryName like ('%'|| :categoryName ||'%')")
    List<TransactionsDTO> getAllTransactionsDtoByCategoryName(String categoryName);


//    strftime('%m', Transactions.createdAt) lay thang trong ngay

    @Query(value = "select strftime('%m', Transactions.createdAt) AS month,sum(Transactions.amount) as totalAmount " +
            "from Transactions inner join Category on Transactions.categoryId=Category.categoryId " +
            "where strftime('%Y', Transactions.createdAt) like :year and Category.categoryName like :categoryName " +
            "group by month")
    List<DataBarChartDTO> getAllAmountInYearByCategoryName(String categoryName, String year);

}
