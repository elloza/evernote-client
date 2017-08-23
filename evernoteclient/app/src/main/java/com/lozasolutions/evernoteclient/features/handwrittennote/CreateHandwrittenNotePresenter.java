package com.lozasolutions.evernoteclient.features.handwrittennote;

import android.graphics.Bitmap;

import com.evernote.edam.type.Note;
import com.lozasolutions.evernoteclient.data.local.NoteCreator;
import com.lozasolutions.evernoteclient.data.remote.EvernoteAPI;
import com.lozasolutions.evernoteclient.features.base.BasePresenter;
import com.lozasolutions.evernoteclient.features.ocr.OCRManager;
import com.lozasolutions.evernoteclient.injection.ConfigPersistent;
import com.lozasolutions.evernoteclient.util.EvernoteUtil;
import com.lozasolutions.evernoteclient.util.rx.scheduler.SchedulerUtils;
import com.snatik.storage.Storage;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@ConfigPersistent
public class CreateHandwrittenNotePresenter extends BasePresenter<CreateHandwrittenNoteMvpView> {

    private final EvernoteAPI evernoteAPI;

    PublishSubject<Bitmap> bitMapSource = PublishSubject.create();

    private Note note;
    OCRManager ocrManager;
    Storage storage;



    Bitmap bitmap;



    String ocrContent = "";


    @Inject
    public CreateHandwrittenNotePresenter(EvernoteAPI evernoteAPI, OCRManager ocrManager, Storage storage) {
        this.evernoteAPI = evernoteAPI;
        this.ocrManager = ocrManager;
        this.storage = storage;
    }

    @Override
    public void attachView(CreateHandwrittenNoteMvpView mvpView) {
        super.attachView(mvpView);

        Disposable disposable = ocrProcessResult().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if(isViewAttached()){
                    ocrContent = s;
                    getView().showOcrResultRealTime(s);
                }
            }
        });

        addDisposable(disposable);
    }


    private Observable<String> ocrProcessResult(){

        return bitMapSource.switchMap(bitmap -> processBitmap(bitmap).toObservable());

    }


    private Flowable<String> processBitmap(Bitmap bitmap){

        return Flowable.create((FlowableEmitter<String> emitter) -> {
            //DO work
            emitter.onNext(ocrManager.processImage(bitmap));

        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io());

    }


    public String getOcrContent() {
        return ocrContent;
    }


    public void check(String GUID) {
        checkViewAttached();
    }


    @Override
    public void detachView() {
        super.detachView();
    }

    public void processImage(Bitmap bitmap){
        this.bitmap = bitmap;
        bitMapSource.onNext(bitmap);
    }

    public void createNote(String title){

        Note note = null;
        try {

            final File file = createFileFromBitmap(bitmap);

            note = NoteCreator.createNote(title,ocrContent,bitmap != null ? new NoteCreator.ImageData(file.getAbsolutePath(),file.getName(), EvernoteUtil.MIME_PNG):null,null,null);
            checkViewAttached();
            getView().showProgress(true);

            evernoteAPI.addNote(note, null).compose(SchedulerUtils.ioToMain()).subscribe(
                    createdNote -> {
                        isViewAttached();
                        getView().showProgress(false);
                        getView().noteCreatedSuccessfully();
                        if(file != null)
                            file.delete();
                    },
                    throwable -> {
                        isViewAttached();
                        getView().showProgress(false);
                        getView().showError(throwable);
                        if(file != null)
                            file.delete();
                    });
        } catch (IOException e) {
            e.printStackTrace();
            isViewAttached();
            getView().showProgress(false);
            getView().showError(e);
        }

    }


    private File createFileFromBitmap(Bitmap bitmap){

        File f = null;

        if(Storage.isExternalWritable()){
            String external = storage.getExternalStorageDirectory();
            String temp = external+"/temp/";
            storage.createDirectory(temp);
            storage.createFile(temp+"temp.png", bitmap);
            f = new File(temp+"temp.png");
        }

        return f;

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }



}
