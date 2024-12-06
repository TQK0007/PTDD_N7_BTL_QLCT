package com.example.ptdd_btl_qlct_n7_final2.dao;

import androidx.room.Dao;
import androidx.room.Query;


import com.example.ptdd_btl_qlct_n7_final2.entity.LongTermGoal;

import java.util.List;

@Dao
public interface LongTermGoalDAO extends DAO<LongTermGoal> {

    @Query(value = "Select * from LongTermGoal")
    List<LongTermGoal> getAll();

}
