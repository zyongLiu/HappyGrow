package com.liu.happygrow.fragment;


import android.os.Bundle;

import com.liu.happygrow.bean.GanHuo;
import com.liu.happygrow.callback.GanHuoCallBack;
import com.liu.happygrow.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

/**
 * Created by Liu on 2016/3/5.
 */
public class AndroidFragment extends BaseListFragment {
    public static AndroidFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type",type);
        AndroidFragment fragment = new AndroidFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
