package com.just.stone.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.just.stone.ApplicationEx;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Zac on 2016/8/16.
 */

public class ImageDownload {
    private static final String IMAGE_NAME = "dog.jpg";
    private static final String DEST_FILE = Environment.getExternalStorageDirectory() + "/dog2.jpg";
    private static final String IMAGE_URL = "http://192.168.0.200/php/images/dog.jpg";

    public static void download(){
        try
        {
            URL url = new URL(IMAGE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            FileOutputStream stream = new FileOutputStream(DEST_FILE);

            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
            byte[] byteArray = outstream.toByteArray();

            stream.write(byteArray);
            stream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
