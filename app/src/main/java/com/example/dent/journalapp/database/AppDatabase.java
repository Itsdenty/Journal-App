package com.example.dent.journalapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

/**
 * Created by dent4 on 6/25/2018.
 */
@Database(entities = {JournalEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "journals";
    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new database Instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME).build();
            }
        }
            Log.d(LOG_TAG, "Getting the database Instance");
            return sInstance;
    }
    public abstract JournalDao journalDao();
}


