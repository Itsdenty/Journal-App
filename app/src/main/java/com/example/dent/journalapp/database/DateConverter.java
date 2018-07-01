package com.example.dent.journalapp.database;

import android.arch.persistence.room.TypeConverter;
import java.util.Date;

/**
 * Created by dent4 on 6/26/2018.
 */

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp){
        return timestamp == null ? null : new Date(timestamp);
    }
    @TypeConverter
    public static Long toTimestamp(Date date){
        return date == null ? null : date.getTime();
    }
}
