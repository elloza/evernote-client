package com.lozasolutions.evernoteclient.features.ocr;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Loza on 23/08/2017.
 */
public class OCRManager {

    TessBaseAPI mTess;
    String languagePath;
    String language;
    String datapath;

    public OCRManager(String languagePath, String language, Context context) {



        this.languagePath = languagePath;
        this.language = language;
        datapath = context.getFilesDir()+ "/tesseract/";

        //make sure training data has been copied
        if(!checkFile(new File(datapath + "tessdata/")))
            copyFiles(languagePath,context);

        this.mTess = new TessBaseAPI();
        mTess.init(datapath, language);
        mTess.setDebug(true);
    }

    private boolean checkFile(File dir) {
        if (!dir.exists()&& dir.mkdirs()){
            return false;
        }
        if(dir.exists()) {
            String datafilepath = datapath+  "/"+languagePath;
            File datafile = new File(datafilepath);

            if (!datafile.exists()) {
                return false;
            }
        }

        return true;
    }

    public String  processImage(Bitmap bitmap){

        mTess.clear();
        mTess.setImage(bitmap);
        return mTess.getUTF8Text();
    }


    private void copyFiles(String languagePath,Context context) {
        try {

            String filepath = datapath + "/"+languagePath;
            AssetManager assetManager = context.getAssets();


            //open byte streams for reading/writing
            InputStream instream = assetManager.open(languagePath);
            OutputStream outstream = new FileOutputStream(filepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
