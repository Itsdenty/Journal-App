package com.example.dent.journalapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

// COMPLETED (2) Annotate the class with Entity. Use "task" for the table name
@Entity(tableName = "journals")
public class JournalEntry {

    // COMPLETED (3) Annotate the id as PrimaryKey. Set autoGenerate to true.
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String details;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    // COMPLETED (4) Use the Ignore annotation so Room knows that it has to use the other constructor instead
    @Ignore
    public JournalEntry(String title, String details, Date updatedAt) {
        this.title = title;
        this.details = details;
        this.updatedAt = updatedAt;
    }

    public JournalEntry(int id, String title, String details, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}