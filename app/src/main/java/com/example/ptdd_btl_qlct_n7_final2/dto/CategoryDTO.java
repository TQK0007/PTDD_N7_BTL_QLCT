package com.example.ptdd_btl_qlct_n7_final2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private String categoryName;

    private String iconName;

    private boolean isIncome;
    private double amount;
}
