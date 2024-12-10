package com.example.ptdd_btl_qlct_n7_final2.dto;

import com.example.ptdd_btl_qlct_n7_final2.entity.Category;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsDTO {
    private int transactionId;
    private double amount;
    private Date createdAt;

    private int categoryId;

    private String categoryName;

    private String iconName;


}
