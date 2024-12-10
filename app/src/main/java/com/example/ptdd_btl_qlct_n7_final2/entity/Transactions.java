package com.example.ptdd_btl_qlct_n7_final2.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = Category.class,
                        parentColumns = "categoryId",
                        childColumns = "categoryId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = LongTermGoal.class,
                        parentColumns = "id",
                        childColumns = "goalId",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class Transactions implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int transactionId;
    private double amount;
    private int categoryId;

    private Date createdAt;

    private String note;

    private String type;

    private Integer goalId;

    public Transactions(double amount, int categoryId, Date createdAt, String note, String type, Integer goalId) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.note = note;
        this.type = type;
        this.goalId = goalId;
    }

}
