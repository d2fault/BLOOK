package com.example.sunbr.blookfront;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2017-02-14.
 */

public class FindFromGall extends AppCompatActivity{
    String dir, filename;
    File file;
    private AsyncHttpClient asyncHttpClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findfromgall);
        Intent intent = getIntent();
        byte[] arr = getIntent().getByteArrayExtra("image");
        Bitmap image = BitmapFactory.decodeByteArray(arr,0,arr.length);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageBitmap(image);
        dir = saveImage(image);

        RequestParams params = new RequestParams();
        params.setContentEncoding("utf-8");
        try{
            params.put("imageName",filename);
            params.put("file",file);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        asyncHttpClient.post(getApplicationContext(), getString(R.string.default_url) + "write", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(getApplicationContext(), "Success for Sending.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Failed for Sending.", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private String saveImage(Bitmap photo) {

        final File saveDir = new File(getApplicationContext().getFilesDir(), "blook");

        filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()));

        if (!saveDir.exists() && !saveDir.isDirectory()) {
            saveDir.mkdir();/* Create Directory "MIPMAP" */
        }

        file = new File(saveDir + File.separator + filename + ".jpg");
        Log.e("dir : ", saveDir + File.separator + filename + ".jpg");
        OutputStream out = null;

        try {
            file.createNewFile();
            out = new FileOutputStream(file);
            photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return saveDir + File.separator + filename;
    }
}
