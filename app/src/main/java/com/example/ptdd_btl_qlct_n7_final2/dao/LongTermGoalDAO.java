package com.example.ptdd_btl_qlct_n7_final2.dao;

import androidx.room.Dao;
import androidx.room.Query;


import com.example.ptdd_btl_qlct_n7_final2.entity.LongTermGoal;

import java.util.List;

@Dao
public interface LongTermGoalDAO extends DAO<LongTermGoal> {

    @Query(value = "Select * from LongTermGoal")
    List<LongTermGoal> getAll();

    @Query(value = "select * from LongTermGoal where id =:id")
    LongTermGoal findByID(int id);

    @Query(value = "select name from LongTermGoal")
    List<String> getAllLongTermGoalName();

    @Query(value = "select id from LongTermGoal where name = :name")
    int getLongTermGoalByName(String name);



//    update
    @Query("SELECT * FROM LongTermGoal ORDER BY (progress / target) ASC")
    List<LongTermGoal> getAllSorted();

//    // Thêm một kế hoạch dài hạn với hành động kiểm tra isDefault
//    @Insert
//    void add(LongTermGoal goal);

    @Query("UPDATE LongTermGoal SET isDefault = 0 WHERE isDefault = 1")
    void resetAllDefaults();

    // Phương thức để thêm LongTermGoal mới và reset các isDefault
    default void addWithDefaultCheck(LongTermGoal goal) {
        if (goal.isDefault()) {
            resetAllDefaults();  // Reset tất cả các mục tiêu có isDefault = true thành false
        }
        add(goal);  // Thêm mục tiêu mới vào cơ sở dữ liệu
    }

}
