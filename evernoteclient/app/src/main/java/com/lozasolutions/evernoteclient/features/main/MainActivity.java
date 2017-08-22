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
import com.lozasolutions.evernoteclient.features.login.LoginActivity;
import com.lozasolutions.evernoteclient.injection.component.ActivityComponent;
import com.lozasolutions.evernoteclient.util.ViewUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainMvpView, ErrorView.ErrorListener, NoteAdapter.OnNoteClickListener, CreateNoteDialogFragment.CreateNormalNoteListener {

    private static final int NOTE_MAX = 20;

    NoteAdapter noteAdapter;

    @Inject
    MainPresenter mainPresenter;

    @BindView(R.id.view_error)
    ErrorView errorView;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.recycler_notes)
    RecyclerView pokemonRecycler;

    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.floatingButtonMenu)
    FloatingActionsMenu floatingButtonMenu;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.primary);
        swipeRefreshLayout.setColorSchemeResources(R.color.white);
        swipeRefreshLayout.setOnRefreshListener(() -> mainPresenter.getNotes(NOTE_MAX,true));

        pokemonRecycler.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(this);
        pokemonRecycler.setAdapter(noteAdapter);
        errorView.setErrorListener(this);


        mainPresenter.getNotes(NOTE_MAX,false);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @OnClick(R.id.createNoteHandWritten)
    public void onClickCreateNewHandWrittenNote(View v){


    }


    @OnClick(R.id.createNoteNormal)
    public void onClickCreateNewNormalNote(View v){

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
    public void showNoteList(List<Note> pokemon) {
        noteAdapter.setNoteList(pokemon);
        pokemonRecycler.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            if (pokemonRecycler.getVisibility() == View.VISIBLE
                    && noteAdapter.getItemCount() > 0) {
                swipeRefreshLayout.setRefreshing(true);
            } else {
                progressBar.setVisibility(View.VISIBLE);

                pokemonRecycler.setVisibility(View.GONE);
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
        pokemonRecycler.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        Timber.e(error, "There was an error retrieving the pokemon");
    }

    @Override
    public void showNoteCreationError(Throwable error) {
        ViewUtil.showSnackbar(swipeRefreshLayout, "Create note failed");
    }

    @Override
    public void showNoteCreationSuccessfully() {
        ViewUtil.showSnackbar(swipeRefreshLayout, "Create note success");
    }

    @Override
    public void onReloadData() {
        mainPresenter.getNotes(NOTE_MAX,true);
    }

    @Override
    public void onNoteClicked(Note note) {
        startActivity(DetailActivity.getStartIntent(this, note.getGuid(),note.getTitle()));
    }

    @Override
    public void onNoteCreated(Note note) {
        mainPresenter.createNote(note);
    }
}
