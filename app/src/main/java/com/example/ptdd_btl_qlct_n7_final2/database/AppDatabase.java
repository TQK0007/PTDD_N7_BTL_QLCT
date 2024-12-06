package com.example.ptdd_btl_qlct_n7_final2.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.ptdd_btl_qlct_n7_final2.converter.Converters;
import com.example.ptdd_btl_qlct_n7_final2.dao.*;
import com.example.ptdd_btl_qlct_n7_final2.entity.*;


// cau hinh cac bang trong db ( moi class la mot bang )
@Database(entities = {Category.class, Transactions.class, LongTermGoal.class},version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static  AppDatabase appDatabase;
    private final static String DATABASE_NAME = "FinanceManager_final.db";
    public static AppDatabase getInstance(Context context)
    {
        if(appDatabase==null)
        {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return  appDatabase;
    }

// các phương thức trả về lớp implement của interface
    public abstract CategoryDAO categoryDAO();
    public abstract TransactionsDAO transactionsDAO();
    public abstract LongTermGoalDAO longTermGoalDAO();

}
