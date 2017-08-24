package com.lozasolutions.evernoteclient.features.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.evernote.edam.type.Note;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.lozasolutions.evernoteclient.R;
import com.lozasolutions.evernoteclient.features.base.BaseActivity;
import com.lozasolutions.evernoteclient.features.common.ErrorView;
import com.lozasolutions.evernoteclient.features.detail.DetailActivity;
import com.lozasolutions.evernoteclient.features.handwrittennote.CreateHandwrittenNoteActivity;
import com.lozasolutions.evernoteclient.features.login.LoginActivity;
import com.lozasolutions.evernoteclient.injection.component.ActivityComponent;
import com.lozasolutions.evernoteclient.util.ViewUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainMvpView, ErrorView.ErrorListener, NoteAdapter.OnNoteClickListener, CreateNoteDialogFragment.CreateNormalNoteListener {

    private static final int NOTE_MAX = 3;

    NoteAdapter noteAdapter;

    @Inject
    MainPresenter mainPresenter;

    @BindView(R.id.view_error)
    ErrorView errorView;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.recycler_notes)
    RecyclerView noteRecyclerView;

    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.floatingButtonMenu)
    FloatingActionsMenu floatingButtonMenu;

    private EndlessRecyclerViewScrollListener scrollListener;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static Intent getStartIntent(Context context,boolean clearTop) {
        Intent intent = new Intent(context, MainActivity.class);
        if(clearTop)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.primary);
        swipeRefreshLayout.setColorSchemeResources(R.color.white);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            mainPresenter.getNotes(NOTE_MAX, 0, true);
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        noteRecyclerView.setLayoutManager(linearLayoutManager);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(totalItemsCount);
            }
        };
        // Adds the scroll listener to RecyclerView
        noteRecyclerView.addOnScrollListener(scrollListener);


        noteAdapter = new NoteAdapter(this);
        noteRecyclerView.setAdapter(noteAdapter);
        errorView.setErrorListener(this);


        mainPresenter.getNotes(NOTE_MAX, 0, false);
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int totalItemsCount) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        mainPresenter.getMoreNotes(NOTE_MAX, totalItemsCount, true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //setRecyclerViewPosition(0);

        switch (item.getItemId()) {

            case R.id.action_logout:
                mainPresenter.logout();
                startActivity(LoginActivity.getStartIntent(this));
                finish();
                return true;

            case R.id.action_sort_alphabetically:
                mainPresenter.getNotesSortedByTitle(NOTE_MAX, 0);
                return true;

            case R.id.action_sort_date_creation:
                mainPresenter.getNotesSortedByDateCreation(NOTE_MAX, 0);
                return true;

            case R.id.action_sort_date_update:
                mainPresenter.getNotesSortedByDateUpdated(NOTE_MAX, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.createNoteHandWritten)
    public void onClickCreateNewHandWrittenNote(View v) {

        startActivity(CreateHandwrittenNoteActivity.getStartIntent(this));
    }


    @OnClick(R.id.createNoteNormal)
    public void onClickCreateNewNormalNote(View v) {
        new CreateNoteDialogFragment().show(getSupportFragmentManager(), CreateNoteDialogFragment.TAG);
        floatingButtonMenu.collapse();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        mainPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        mainPresenter.detachView();
    }

    @Override
    public void showNoteList(List<Note> noteList, int offset) {

        if (offset == 0) {
            noteAdapter.setNoteList(noteList);
            // 3. Reset endless scroll listener when performing a new search
            scrollListener.resetState();
        }

        noteAdapter.notifyItemRangeInserted(offset, noteList.size() - 1);
        noteRecyclerView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            if (noteRecyclerView.getVisibility() == View.VISIBLE
                    && noteAdapter.getItemCount() > 0) {
                swipeRefreshLayout.setRefreshing(true);
            } else {
                progressBar.setVisibility(View.VISIBLE);

                noteRecyclerView.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }

            errorView.setVisibility(View.GONE);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(Throwable error) {
        noteRecyclerView.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorView.setTextError(getString(R.string.error_message_notes));
        Timber.e(error, getString(R.string.error_message_notes));
    }

    @Override
    public void showNoteCreationError(Throwable error) {
        ViewUtil.showSnackbar(swipeRefreshLayout, getString(R.string.error_creating_note));
    }

    @Override
    public void showNoteCreationSuccessfully() {
        ViewUtil.showSnackbar(swipeRefreshLayout, R.string.note_created_successfully);
    }

    @Override
    public void onReloadData() {
        mainPresenter.getNotes(NOTE_MAX, 0, true);
    }

    @Override
    public void onNoteClicked(Note note) {
        startActivity(DetailActivity.getStartIntent(this, note.getGuid(), note.getTitle()));
    }

    @Override
    public void onNoteCreated(Note note) {
        mainPresenter.createNote(note);
    }
}
