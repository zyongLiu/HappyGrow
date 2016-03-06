package com.liu.happygrow.callback;

import android.accounts.NetworkErrorException;

import com.google.gson.Gson;
import com.liu.happygrow.bean.GanHuo;
import com.liu.happygrow.bean.ResultData;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import okhttp3.Response;

/**
 * Created by Liu on 2016/3/6.
 */
public abstract class GanHuoCallBack extends Callback<List<GanHuo>> {
    @Override
    public List<GanHuo> parseNetworkResponse(Response response) throws Exception {
        String string=response.body().string();
        Gson gson=new Gson();
        ResultData result= gson.fromJson(string, ResultData.class);
        if (result.getError().equals("false"))
            return result.getResults();
        else
            throw new NetworkErrorException("get data fail");
    }
}
