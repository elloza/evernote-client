package com.lozasolutions.evernoteclient.features.handwrittennote;

import android.graphics.Bitmap;

import com.evernote.edam.type.Note;
import com.lozasolutions.evernoteclient.data.remote.EvernoteAPI;
import com.lozasolutions.evernoteclient.features.base.BasePresenter;
import com.lozasolutions.evernoteclient.features.ocr.OCRManager;
import com.lozasolutions.evernoteclient.injection.ConfigPersistent;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@ConfigPersistent
public class CreateHandwrittenNotePresenter extends BasePresenter<CreateHandwrittenNoteMvpView> {

    private final EvernoteAPI evernoteAPI;

    PublishSubject<Bitmap> bitMapSource = PublishSubject.create();

    private Note note;
    OCRManager ocrManager;


    @Inject
    public CreateHandwrittenNotePresenter(EvernoteAPI evernoteAPI, OCRManager ocrManager) {
        this.evernoteAPI = evernoteAPI;
        this.ocrManager = ocrManager;
    }

    @Override
    public void attachView(CreateHandwrittenNoteMvpView mvpView) {
        super.attachView(mvpView);

        Disposable disposable = ocrProcessResult().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if(isViewAttached()){
                    getView().showOcrResultRealTime(s);
                }
            }
        });

        addDisposable(disposable);
    }


    private Observable<String> ocrProcessResult(){

        return bitMapSource.switchMap(new Function<Bitmap, ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> apply(Bitmap bitmap) throws Exception {
                return processBitmap(bitmap).toObservable();
            }
        });

    }


    private Flowable<String> processBitmap(Bitmap bitmap){

        return Flowable.create((FlowableEmitter<String> emitter) -> {
            //DO work
            emitter.onNext(ocrManager.processImage(bitmap));

        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io());

    }



    public void check(String GUID) {
        checkViewAttached();
    }


    @Override
    public void detachView() {
        super.detachView();
    }

    public void processImage(Bitmap bitmap){

        bitMapSource.onNext(bitmap);

    }


}
