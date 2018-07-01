package com.example.dent.journalapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dent.journalapp.IdlingResource.SimpleIdlingResource;
import com.example.dent.journalapp.Utilities.DrawerUtil;
import com.example.dent.journalapp.database.AppDatabase;
import com.example.dent.journalapp.database.JournalEntry;
import com.viralypatel.sharedpreferenceshelper.lib.SharedPreferencesHelper;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {
    EditText journalTitleEditView;
    EditText journalDetailsEditView;
    TextView updateJournalTextView;
    Button mButton;
    SharedPreferencesHelper sph;
    private final String TITLE = "journal_title";
    private final String DETAILS = "journal_details";
    private final String ID = "journal_ID";
    int id = 0;
    // Extra for the task ID to be received in the intent
    public static final String EXTRA_JOURNAL_ID = "extraJournalId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_JOURNAL_ID = "instanceJournalId";
    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_JOURNAL_ID = -1;
    // Constant for logging
    private static final String TAG = AddJournalActivity.class.getSimpleName();

    private int mJournalId = DEFAULT_JOURNAL_ID;

    // COMPLETED (3) Create AppDatabase member variable for the Database
    // Member variable for the Database
    private AppDatabase mDb;
    @Nullable
    private SimpleIdlingResource mIdlingResource;
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        //Setup tool bar and action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar3);
        Spannable text = new SpannableString(getResources().getString(R.string.nav_add_update));
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        toolbar.setTitle(text);
        setSupportActionBar(toolbar);
        getSupportActionBar (). setDisplayHomeAsUpEnabled (true);

        //activate navigation drawer
        DrawerUtil.getDrawer(this,toolbar, this);

        //initiallize view
        initViews();

        // initialize database
        mDb = AppDatabase.getInstance(getApplicationContext());

        //track the journal id from the saved instance to ensure update or add mode is tracked as such
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_JOURNAL_ID)) {
            mJournalId = savedInstanceState.getInt(INSTANCE_JOURNAL_ID, DEFAULT_JOURNAL_ID);
        }

        //initiate activity intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)) {

            //Preferentially populate database based on the journal id availability
            mButton.setText(R.string.update_button);
            if (mJournalId == DEFAULT_JOURNAL_ID) {
                mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID, DEFAULT_JOURNAL_ID);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final JournalEntry task = mDb.journalDao().loadJournalById(mJournalId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUI(task);
                            }
                        });
                    }
                });
            }
        }

        // Get the IdlingResource instance
        getIdlingResource();
    }

    //add click event listener to the add journal button and execute insert of update database actions
    public void onSaveButtonClicked() {
        final String journalTitle = journalTitleEditView.getText().toString();
        final String journalDetails = journalDetailsEditView.getText().toString();
        final Date date = new Date();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final JournalEntry journalEntry = new JournalEntry(journalTitle, journalDetails, date);
                if (mJournalId == DEFAULT_JOURNAL_ID) {
                    // insert new task
                    mDb.journalDao().insertJournal(journalEntry);
                } else {
                    //update task
                    journalEntry.setId(mJournalId);
                    mDb.journalDao().updateJournal(journalEntry);
                }
                finish();
            }
        });

        //Navigate to the Journalist Activity
        Intent intent = new Intent(this, JournalListActivity.class);
        startActivity(intent);
    }

    //populate UI function
    private void populateUI(JournalEntry journal) {
        if (journal == null) {
            return;
        }
        journalDetailsEditView.setText(journal.getDetails());
        journalTitleEditView.setText(journal.getTitle());
    }

    //initialize view function
    private void initViews() {

        journalTitleEditView = (EditText) findViewById(R.id.et_journal_title);
        journalDetailsEditView = (EditText) findViewById(R.id.et_journal_details);
        updateJournalTextView = (TextView)  findViewById(R.id.tv_update_journal);
        mButton = (Button) findViewById(R.id.add_button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }
}
