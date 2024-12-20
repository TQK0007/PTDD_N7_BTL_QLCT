package com.example.ptdd_btl_qlct_n7_final2.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LongTermGoal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private double target;

    private Date deadline;

    private double progress;

    private boolean isDefault;

    public LongTermGoal(String name, double target, Date deadline, double progress, boolean isDefault) {
        this.name = name;
        this.target = target;
        this.deadline = deadline;
        this.progress = progress;
        this.isDefault = isDefault;
    }
}
