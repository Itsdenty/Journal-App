package com.example.dent.journalapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

import com.example.dent.journalapp.IdlingResource.SimpleIdlingResource;
import com.example.dent.journalapp.Utilities.DrawerUtil;
import com.example.dent.journalapp.database.AppDatabase;
import com.example.dent.journalapp.database.JournalEntry;

import java.util.List;

public class JournalListActivity extends AppCompatActivity implements JournalAdapter.ItemClickListener {
    // Member variables for the adapter and RecyclerView
    RecyclerView mRecyclerView;
    private JournalAdapter mAdapter;

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
    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();

    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_journal_list);

        //Configure toolbar color and title text
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        Spannable text = new SpannableString(getResources().getString(R.string.nav_entries));
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        toolbar.setTitle(text);

        //setup action bar and navigation drawer
        setSupportActionBar(toolbar);
        getSupportActionBar (). setDisplayHomeAsUpEnabled (true);
        DrawerUtil.getDrawer(this,toolbar, this);

        //Initialize and configure recycler view
        mRecyclerView = (RecyclerView) this.findViewById(R.id.rv_journal_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new JournalAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        mButton = (Button) findViewById(R.id.read_button);

        // Get the IdlingResource instance
        getIdlingResource();
        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                    // Here is where you'll implement swipe to delete
                    // COMPLETED (1) Get the diskIO Executor from the instance of AppExecutors and
                    // call the diskIO execute method with a new Runnable and implement its run method
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            // COMPLETED (3) get the position from the viewHolder parameter
                            int position = viewHolder.getAdapterPosition();
                            List<JournalEntry> journals = mAdapter.getJournals();
                            // COMPLETED (4) Call deleteTask in the taskDao with the task at that position
                            mDb.journalDao().deleteJournal(journals.get(position));
                            // COMPLETED (6) Call retrieveTasks method to refresh the UI
                            retrieveJournals();
                        }
                    });
            }
        }).attachToRecyclerView(mRecyclerView);

        mDb = AppDatabase.getInstance(getApplicationContext());

    }
    @Override
    protected void onResume() {
        super.onResume();
        // COMPLETED (3) Call the adapter's setTasks method using the result
        // of the loadAllTasks method from the taskDao
//        mAdapter.setJournals(mDb.journalDao().loadAllJournals());
        retrieveJournals();
    }

    public void retrieveJournals(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                // Extract the list of tasks to a final variable
                final List<JournalEntry> journals = mDb.journalDao().loadAllJournals();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setJournals(journals);
                    }
                });
            }
        });
    }
    //method for processing input data before passing it to addMedication method
    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(this, SingleJournalActivity.class);
        intent.putExtra("Id", itemId);
        startActivity(intent);
        // Launch AddTaskActivity adding the itemId as an extra in the intent
    }
}
