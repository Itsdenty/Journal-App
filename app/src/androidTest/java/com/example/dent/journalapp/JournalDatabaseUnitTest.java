package com.example.dent.journalapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.dent.journalapp.database.AppDatabase;
import com.example.dent.journalapp.database.JournalDao;
import com.example.dent.journalapp.database.JournalEntry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by dent4 on 6/30/2018.
 */
@RunWith(AndroidJUnit4.class)
public class JournalDatabaseUnitTest {
    private JournalDao mJournalDao;
    private AppDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mJournalDao = mDb.journalDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void TestJournalDao() throws Exception {
        //load a new date
        Date date = new Date();

        //initialize a journal Entry
        JournalEntry journalEntry = new JournalEntry("Test Journal", "This is a sample detail", date);

        //insert a journal entry into the database
        mDb.journalDao().insertJournal(journalEntry);

        //initiate another journal Entry
        JournalEntry journalEntry2 = new JournalEntry("Test Journal2", "This is a sample detail2", date);

        //insert a second journal entry into the database
        mDb.journalDao().insertJournal(journalEntry2);

        //load all journal entries
        List<JournalEntry> journals = mJournalDao.loadAllJournals();

        //get the first journal Entry
        JournalEntry sampleEntry = journals.get(0);
//        assertEquals(sampleEntry, journalEntry);
        //Extract the first journal entry
        int id = sampleEntry.getId();


        //Test that the correct number of data was inserted into the database
        assertTrue(journals.size() == 2);

        //Load the first entry in the database by id
        JournalEntry jEntry = mDb.journalDao().loadJournalById(id);
        String name = jEntry.getTitle();

        //Test that it is actually the saved Journal that is returned
        assertTrue("Test Journal" == "Test Journal");
        jEntry.setTitle("Example Journal");

        //test the update function
        mDb.journalDao().updateJournal(jEntry);
        String updatedName = jEntry.getTitle();
        assertTrue(updatedName == "Example Journal");

        //Test the delete function
        mDb.journalDao().deleteJournal(jEntry);
        List<JournalEntry> newJournals = mDb.journalDao().loadAllJournals();
        assertTrue(newJournals.size() == 1);

    }
}
