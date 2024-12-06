package com.example.ptdd_btl_qlct_n7_final2.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LongTermGoal {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private double target;

    private String deadline;

    private double progress;

    private boolean isDefault;

    public LongTermGoal(String name, double target, String deadline, double progress, boolean isDefault) {
        this.name = name;
        this.target = target;
        this.deadline = deadline;
        this.progress = progress;
        this.isDefault = isDefault;
    }
}
