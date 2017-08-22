package com.lozasolutions.evernoteclient.features.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.evernote.edam.type.Note;
import com.evernote.edam.type.Resource;
import com.lozasolutions.evernoteclient.R;
import com.lozasolutions.evernoteclient.features.base.BaseActivity;
import com.lozasolutions.evernoteclient.features.common.ErrorView;
import com.lozasolutions.evernoteclient.injection.component.ActivityComponent;
import com.lozasolutions.evernoteclient.util.EvernoteUtil;

import javax.inject.Inject;

import butterknife.BindView;
import timber.log.Timber;

public class DetailActivity extends BaseActivity implements DetailMvpView, ErrorView.ErrorListener {

    public static final String EXTRA_NOTE_GUID = "EXTRA_NOTE_GUID";
    public static final String EXTRA_NOTE_TITLE = "EXTRA_NOTE_TITLE";

    @Inject
    DetailPresenter detailPresenter;

    @BindView(R.id.view_error)
    ErrorView errorView;

    @BindView(R.id.image_note)
    ImageView imageNote;

    @BindView(R.id.progress)
    ProgressBar progress;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.layout_content)
    View contentLayout;

    @BindView(R.id.webView)
    WebView webView;

    private String noteGuid;
    private String noteTitle;
    private String mHtml = "";

    public static Intent getStartIntent(Context context, String GUID, String title) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_NOTE_GUID, GUID);
        intent.putExtra(EXTRA_NOTE_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteGuid = getIntent().getStringExtra(EXTRA_NOTE_GUID);

        if (noteGuid == null) {
            throw new IllegalArgumentException("Detail Activity need GUID");
        }

        noteTitle = getIntent().getStringExtra(EXTRA_NOTE_TITLE);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(noteTitle);

        if (savedInstanceState == null) {
            webView.setWebViewClient(new WebViewClient());
        }

        errorView.setErrorListener(this);
        detailPresenter.getNoteComplete(noteGuid);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        detailPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        detailPresenter.detachView();
    }

    @Override
    public void showNoteInformation(Note note) {

        Resource resource = EvernoteUtil.getFirstImageResource(note);

        //TODO

        //Content

        if(resource != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(resource.getData().getBody(), 0, resource.getData().getSize());
            // TODO load asynchrously this Glide.with(this).load(bitmap).into(imageNote);
            imageNote.setImageBitmap(bitmap);
            imageNote.setVisibility(View.VISIBLE);
        }else{
            imageNote.setVisibility(View.GONE);
        }

        contentLayout.setVisibility(View.VISIBLE);

        mHtml = note.getContent();

        String data = "<html><head></head><body>" + mHtml + "</body></html>";

        webView.loadDataWithBaseURL("", data, "text/html", "UTF-8", null);

    }


    @Override
    public void showProgress(boolean show) {
        errorView.setVisibility(View.GONE);
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(Throwable error) {
        contentLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        Timber.e(error, "There was a problem retrieving the note...");
    }


    @Override
    public void onReloadData() {
        detailPresenter.getNoteComplete(noteGuid);
    }
}
