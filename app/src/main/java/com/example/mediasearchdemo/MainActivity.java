package com.example.mediasearchdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] strings = new String[]{MediaStoreUtils.WORD};

        MediaStoreUtils.INSTANCE.loadMedia(this, strings, new Function1<List<? extends MediaFolderBean>, Unit>() {
            @Override
            public Unit invoke(List<? extends MediaFolderBean> mediaFolderBeans) {
                //查询出所有word文件
                List<? extends MediaFolderBean> a = mediaFolderBeans;


                return null;
            }
        });
    }
}
