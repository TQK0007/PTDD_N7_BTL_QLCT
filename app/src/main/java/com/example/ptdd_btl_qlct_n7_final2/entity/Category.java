package com.example.ptdd_btl_qlct_n7_final2.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int categoryId;

    private String categoryName;

    private String iconName;

    private boolean isIncome;
}
