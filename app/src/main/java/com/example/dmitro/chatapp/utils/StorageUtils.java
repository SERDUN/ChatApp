package com.example.dmitro.chatapp.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.dmitro.chatapp.ChatApp;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by dmitro on 17.10.17.
 */

public class StorageUtils {
    public static String saveToInternalStorage(byte[] bytes) {
        ContextWrapper cw = new ContextWrapper(ChatApp.getInstance().getBaseContext());
        File directory = cw.getDir("audioDir", Context.MODE_PRIVATE);
        String fileName = System.currentTimeMillis() + ".mp3";
        File mypath = new File(directory, fileName);


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath() + "/" + fileName;

    }


    public static byte[] loadByteArrayFromStorage(String path) {
        try {
            File f = new File(path);
            return IOUtils.toByteArray(new FileInputStream(f));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(ChatApp.getInstance().getBaseContext());
        File directory = cw.getDir("image", Context.MODE_PRIVATE);
        String fileName = System.currentTimeMillis() + ".png";
        File mypath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath() + "/" + fileName;
    }

    public static Bitmap loadImageFromStorage(String path) {

        try {
            File f = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(f));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
