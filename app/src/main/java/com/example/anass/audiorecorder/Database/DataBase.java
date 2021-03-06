package com.example.anass.audiorecorder.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.anass.audiorecorder.Database.DAO.ChoiceDao;
import com.example.anass.audiorecorder.Database.DAO.ImportantRecordDao;
import com.example.anass.audiorecorder.Database.DAO.RecordDao;
import com.example.anass.audiorecorder.Models.Choice;
import com.example.anass.audiorecorder.Models.ImportantRecord;
import com.example.anass.audiorecorder.Models.RecordingItem;

@Database(entities = {RecordingItem.class, ImportantRecord.class, Choice.class}, version = 5, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    private static DataBase mInstance;

    public abstract RecordDao recordDao();
    public abstract ImportantRecordDao importantRecordDao();
    public abstract ChoiceDao choiceDao();

    private static final String DB_NAME = "records_database";

    private Database() {}
    
    public static synchronized DataBase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(), DataBase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }

}
