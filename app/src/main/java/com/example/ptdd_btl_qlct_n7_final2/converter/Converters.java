package com.example.ptdd_btl_qlct_n7_final2.converter;

import android.annotation.SuppressLint;

import androidx.room.TypeConverter;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Converters {

    @TypeConverter
    public static Date dateToDateTime(String value)
    {
        if(value == null)
        {
            return null;
        }
        try
        {
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return  dateFormat.parse(value);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
//    Phải để là static để RoomDB tự động gọi phương thức.
    public static String dateTimeToDate(Date value)
    {
        if(value == null) return null;
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(value);
    }
}
