package com.liu.happygrow.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.liu.happygrow.R;
import com.liu.happygrow.colorUi.util.SharedPreferencesMgr;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

/**
 * Created by Liu on 2016/3/8.
 */
public class ImageActivity extends AppCompatActivity{
    private SubsamplingScaleImageView imageview;
    private String url;

    private ProgressBar pgb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SharedPreferencesMgr.getInt("theme", 0) == 1) {
            SharedPreferencesMgr.setInt("theme", 0);
            setTheme(R.style.theme_1);
        } else {
            SharedPreferencesMgr.setInt("theme", 1);
            setTheme(R.style.theme_2);
        }

        setContentView(R.layout.activity_image_display);
        url=getIntent().getStringExtra("url");
        imageview= (SubsamplingScaleImageView) findViewById(R.id.imageView);
        pgb= (ProgressBar) findViewById(R.id.pgb);
        pgb.setVisibility(View.VISIBLE);


        String[] names=url.split("/");

        OkHttpUtils.get().url(url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath()+"/meizi",
                names[names.length-1]
                ) {
            @Override
            public void inProgress(float progress) {

            }

            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(getApplicationContext(),"下载失败",Toast.LENGTH_SHORT).show();
                pgb.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(File response) {
                pgb.setVisibility(View.GONE);
                        imageview.setImage(ImageSource.bitmap((BitmapFactory.decodeFile(response.getAbsolutePath()))));
            }
        });
    }
}
