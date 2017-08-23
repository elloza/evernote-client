package com.lozasolutions.evernoteclient.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rm.freedrawview.FreeDrawSerializableState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by Riccardo on 23/05/2017.
 */

public class FileHelper {

    private static final String FILE_NAME = "draw_state.ser";

    public static void saveStateIntoFile(
            final Context context, final FreeDrawSerializableState state,
            final StateSaveInterface listener) {

        if (context != null && state != null) {

            new Thread(new StateSaveRunnable(context, listener, state)).start();
        } else {

            if (listener != null) {
                listener.onStateSaveError();
            }
        }
    }

    @Nullable
    public static void getSavedStoreFromFile(
            final Context context, StateExtractorInterface listener) {

        if (context != null) {

            new Thread(new StateExtractorRunnable(context, listener)).start();
        } else {

            if (listener != null) {
                listener.onStateExtractionError();
            }
        }
    }

    public static void deleteSavedStateFile(Context context) {

        if (context != null) {

            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();

                if (fos != null) {

                    try {
                        fos.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }


    // Runnable that extracts the FreeDrawSerializableState from a file
    private static class StateExtractorRunnable implements Runnable {

        private Context mContext;
        private StateExtractorInterface mListener;

        public StateExtractorRunnable(Context context, StateExtractorInterface listener) {
            mContext = context;
            mListener = listener;
        }

        @Override
        public void run() {
            FileInputStream fis = null;
            try {

                fis = mContext.openFileInput(FILE_NAME);
                ObjectInputStream is = new ObjectInputStream(fis);

                final FreeDrawSerializableState state = (FreeDrawSerializableState) is.readObject();

                fis.close();
                is.close();

                if (mListener != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onStateExtracted(state);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();

                if (fis != null) {

                    try {
                        fis.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

                if (mListener != null) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onStateExtractionError();
                        }
                    });
                }
            }
        }
    }


    // Runnable that save a FreeDrawSerializableState inside a file
    private static class StateSaveRunnable implements Runnable {

        private final Context mContext;
        private final StateSaveInterface mListener;
        private final FreeDrawSerializableState mState;

        public StateSaveRunnable(
                Context context, StateSaveInterface listener, FreeDrawSerializableState state) {

            mContext = context;
            mListener = listener;
            mState = state;
        }

        @Override
        public void run() {
            FileOutputStream fos = null;
            try {
                fos = mContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(mState);
                os.flush();
                fos.flush();
                os.close();
                fos.close();

                if (mListener != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onStateSaved();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();

                if (fos != null) {

                    try {
                        fos.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onStateSaveError();
                    }
                });
            }
        }
    }


    // Listener for file creation
    public interface StateSaveInterface {
        void onStateSaved();

        void onStateSaveError();
    }

    // Listener for file data extraction
    public interface StateExtractorInterface {
        void onStateExtracted(FreeDrawSerializableState state);

        void onStateExtractionError();
    }


    // Shortcut method to run on uiThread a runnable
    private static void runOnUiThread(Runnable runnable) {

        new Handler(Looper.getMainLooper()).post(runnable);
    }


    public static File createTempFileFromBitmap(Bitmap mBitmap,Context context){

        File f3=new File(Environment.getExternalStorageDirectory()+"/inpaint/");
        if(!f3.exists())
            f3.mkdirs();


        OutputStream outStream = null;
        File outputFile = null;
        try {
            File outputDir = context.getCacheDir(); // context being the Activity pointer
            outputFile = File.createTempFile("tempImage", "png", outputDir);
            outStream = new FileOutputStream(outputFile);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 85, outStream);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputFile;
    }

    public static File savebitmap(String filename) {
        String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        OutputStream outStream = null;

        File file = new File(filename + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, filename + ".png");
            Log.e("file exist", "" + file + ",Bitmap= " + filename);
        }
        try {
            // make a new bitmap from your file
            Bitmap bitmap = BitmapFactory.decodeFile(file.getName());

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }


}
