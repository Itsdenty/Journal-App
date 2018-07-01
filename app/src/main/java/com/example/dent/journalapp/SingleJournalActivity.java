package com.example.dent.journalapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dent.journalapp.Utilities.DrawerUtil;
import com.example.dent.journalapp.database.AppDatabase;
import com.example.dent.journalapp.database.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SingleJournalActivity extends AppCompatActivity {
    Toolbar toolbar;
    private AppDatabase mDb;
    TextView titleTextView;
    TextView detailsTextView;
    TextView updatedAtTextView;
    int id;
    private static final String DATE_FORMAT = "dd/MM/yyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    public static final String EXTRA_JOURNAL_ID = "extraJournalId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_journal);

        //configure toolbar and actionbar
        toolbar = (Toolbar) findViewById(R.id.my_toolbar6);
        Spannable text = new SpannableString(getResources().getString(R.string.nav_details));
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        toolbar.setTitle(text);
        setSupportActionBar(toolbar);
        getSupportActionBar (). setDisplayHomeAsUpEnabled (true);

        //Activate Navigation drawer for the view
        DrawerUtil.getDrawer(this,toolbar, this);

        //get activity intent
        Intent intent = getIntent();
        id = intent.getIntExtra("Id", 0);

        //initialize database
        mDb = AppDatabase.getInstance(getApplicationContext());

        //initialize view
        titleTextView = (TextView) findViewById(R.id.tv_single_journal_title);
        detailsTextView = (TextView) findViewById(R.id.tv_single_journal_details);
        updatedAtTextView = (TextView) findViewById(R.id.tv_single_journal_updated_at);

        //retrieve journal from database
        retrieveJournal(id);
    }

    public void onSubmit(View view){
        //retrieve the valued entered in the edittext views
//        id = sph.getInt(ID);
        Intent intent = new Intent(this, AddJournalActivity.class);
        intent.putExtra(EXTRA_JOURNAL_ID, id);
        Log.d("my-id", String.valueOf(id));
        startActivity(intent);
    }

    //retrieve journal from database function
    public void retrieveJournal(final int id){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final JournalEntry journal = mDb.journalDao().loadJournalById(id);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        populateUI(journal);
                    }
                });
            }
        });
    }

    //function to populate the ui
    public void populateUI(JournalEntry journal){
        if (journal == null) {
            return;
        }
        detailsTextView.setText(journal.getDetails());
        titleTextView.setText(journal.getTitle());
        updatedAtTextView.setText(dateFormat.format(journal.getUpdatedAt()));
    }
}
