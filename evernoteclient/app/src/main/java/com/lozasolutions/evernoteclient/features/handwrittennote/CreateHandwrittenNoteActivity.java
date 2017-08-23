package com.lozasolutions.evernoteclient.features.handwrittennote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.lozasolutions.evernoteclient.R;
import com.lozasolutions.evernoteclient.features.base.BaseActivity;
import com.lozasolutions.evernoteclient.features.main.MainActivity;
import com.lozasolutions.evernoteclient.injection.component.ActivityComponent;
import com.lozasolutions.evernoteclient.util.FileHelper;
import com.rm.freedrawview.FreeDrawSerializableState;
import com.rm.freedrawview.FreeDrawView;
import com.rm.freedrawview.PathDrawnListener;
import com.rm.freedrawview.PathRedoUndoCountChangeListener;
import com.rm.freedrawview.ResizeBehaviour;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class CreateHandwrittenNoteActivity extends BaseActivity implements CreateHandwrittenNoteMvpView,SeekBar.OnSeekBarChangeListener,
        PathRedoUndoCountChangeListener, FreeDrawView.DrawCreatorListener, PathDrawnListener, TitleDialogFragment.DialogTitleListener {

    private static final int THICKNESS_STEP = 2;
    private static final int THICKNESS_MAX = 80;
    private static final int THICKNESS_MIN = 15;

    private static final int ALPHA_STEP = 1;
    private static final int ALPHA_MAX = 255;
    private static final int ALPHA_MIN = 0;

    private static final String COLOR = "COLOR";


    @BindView(R.id.slider_thickness)
    SeekBar mThicknessBar;

    @BindView(R.id.slider_alpha)
    SeekBar mAlphaBar;

    @BindView(R.id.progress)
    RelativeLayout progress;

    @Inject
    CreateHandwrittenNotePresenter createHandwrittenNotePresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.change_color)
    FloatingActionButton changeColor;

    @BindView(R.id.resultOCR)
    TextView resultOCR;


    @BindView(R.id.free_draw_view)
    FreeDrawView mFreeDrawView;

    int currentColor = Color.BLACK;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, CreateHandwrittenNoteActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.new_note));

        mFreeDrawView.setResizeBehaviour(ResizeBehaviour.FIT_XY);
        mFreeDrawView.setOnPathDrawnListener(this);
        mFreeDrawView.setPathRedoUndoCountChangeListener(this);

        if (savedInstanceState != null) {
            currentColor = savedInstanceState.getInt(COLOR);
            mFreeDrawView.setPaintColor(currentColor);
            changeColor.setColorNormal(currentColor);

            // Restore the previous saved state
            FileHelper.getSavedStoreFromFile(this,
                    new FileHelper.StateExtractorInterface() {
                        @Override
                        public void onStateExtracted(FreeDrawSerializableState state) {
                            if (state != null) {
                                mFreeDrawView.restoreStateFromSerializable(state);
                            }

                        }

                        @Override
                        public void onStateExtractionError() {

                        }
                    });
        }

        mAlphaBar.setMax((ALPHA_MAX - ALPHA_MIN) / ALPHA_STEP);
        int alphaProgress = ((mFreeDrawView.getPaintAlpha() - ALPHA_MIN) / ALPHA_STEP);
        mAlphaBar.setProgress(alphaProgress);
        mAlphaBar.setOnSeekBarChangeListener(this);

        mThicknessBar.setMax((THICKNESS_MAX - THICKNESS_MIN) / THICKNESS_STEP);
        int thicknessProgress = (int)
                ((mFreeDrawView.getPaintWidth() - THICKNESS_MIN) / THICKNESS_STEP);
        mThicknessBar.setProgress(thicknessProgress);
        mThicknessBar.setOnSeekBarChangeListener(this);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(COLOR, currentColor);
        super.onSaveInstanceState(outState);
    }


    @Override
    public int getLayout() {
        return R.layout.activity_create_handwritten_note;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                new TitleDialogFragment().show(getSupportFragmentManager(), TitleDialogFragment.TAG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        FileHelper.saveStateIntoFile(this, mFreeDrawView.getCurrentViewStateAsSerializable(), null);
    }


    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        createHandwrittenNotePresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        createHandwrittenNotePresenter.detachView();
    }


    @Override
    public void showOcrResultRealTime(String result) {
        resultOCR.setText(result);
    }

    @Override
    public void showError(Throwable error) {
        Timber.e(error, "There was a problem retrieving the ocr result...");
    }

    @Override
    public void noteCreatedSuccessfully() {
        startActivity(MainActivity.getStartIntent(this,true));
        finish();
    }

    @Override
    public void showProgress(boolean visible) {
        if(visible){
            progress.setVisibility(View.VISIBLE);
        }else{
            progress.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.undo)
    public void onClickUndo(View v){

        mFreeDrawView.undoLast();

    }

    @OnClick(R.id.redo)
    public void onClickRedo(View v){

        mFreeDrawView.redoLast();

    }

    @OnClick(R.id.clear_all)
    public void onClickClear(View v){
        mFreeDrawView.undoAll();
    }

    @OnClick(R.id.change_color)
    public void onChangeColor(View v){

        ColorPickerDialogBuilder
                .with(v.getContext())
                .setTitle("Choose color")
                .initialColor(mFreeDrawView.getPaintColor())
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        currentColor = selectedColor;
                        mFreeDrawView.setPaintColor(selectedColor);
                        changeColor.setColorNormal(currentColor);

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == mThicknessBar.getId()) {
            mFreeDrawView.setPaintWidthPx(THICKNESS_MIN + (progress * THICKNESS_STEP));
        } else {
            mFreeDrawView.setPaintAlpha(ALPHA_MIN + (progress * ALPHA_STEP));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onDrawCreated(Bitmap draw) {

        Timber.d("BITMAP CREATED");
        createHandwrittenNotePresenter.processImage(draw);
    }

    @Override
    public void onDrawCreationError() {

        Timber.e("ERROR CREATING BITMAP");

    }

    @Override
    public void onPathStart() {

    }

    @Override
    public void onNewPathDrawn() {

    }

    @Override
    public void onUndoCountChanged(int undoCount) {

        mFreeDrawView.getDrawScreenshot(this);
    }

    @Override
    public void onRedoCountChanged(int redoCount) {
    }

    @Override
    public void onTitleNoteEntered(String titleNote) {
        createHandwrittenNotePresenter.createNote(titleNote);
    }
}
