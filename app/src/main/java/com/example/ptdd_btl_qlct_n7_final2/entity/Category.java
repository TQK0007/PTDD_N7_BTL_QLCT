package com.example.ptdd_btl_qlct_n7_final2.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int categoryId;

    private String categoryName;

    private String iconName;

    private boolean isIncome;

    public Category(String categoryName, String iconName, boolean isIncome) {
        this.categoryName = categoryName;
        this.iconName = iconName;
        this.isIncome = isIncome;
    }
}
