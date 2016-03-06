package com.liu.happygrow.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsy.android.grid.StaggeredGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.liu.happygrow.R;
import com.liu.happygrow.adapter.base.CommonAdapter;
import com.liu.happygrow.adapter.base.ViewHolder;
import com.liu.happygrow.bean.GanHuo;
import com.liu.happygrow.callback.GanHuoCallBack;
import com.liu.happygrow.constant.System_constant;
import com.liu.happygrow.utils.LogUtils;
import com.liu.happygrow.view.PullToRefreshStaggeredGridView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Liu on 2016/3/6.
 */
public class VelfareFragment extends Fragment {
    private View view;
    private PullToRefreshStaggeredGridView gv_ptr_velfare;
    private int PAGER_COUNT=10;
    private int pager_num=1;
    private String url="";

    private CommonAdapter<GanHuo> ganHuoCommonAdapter;

    private List<GanHuo> ganhuo;

    public static VelfareFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        VelfareFragment fragment = new VelfareFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_velfare,container,false);
        gv_ptr_velfare= (PullToRefreshStaggeredGridView) view.findViewById(R.id.gv_ptr_velfare);
        setData();
        return view;
    }

    /**
     * 设置数据，分页，分类型，分条数
     */
    protected void setData(){

        url= System_constant.IP+"data/"+getArguments().getString("type")+"/"+PAGER_COUNT+"/"+pager_num;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new GanHuoCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(List<GanHuo> response) {
                        LogUtils.i(response.toString());

                        gv_ptr_velfare.getRefreshableView().setAdapter(


                        ganHuoCommonAdapter = new CommonAdapter<GanHuo>(getActivity(),
                                ganhuo = response, R.layout.item_velfare) {
                            @Override
                            public void convert(ViewHolder holder, GanHuo ganHuo) {
                                holder.setImageBitmap(getActivity(),R.id.iv_velfare,ganHuo.getUrl());
                            }
                        });
                    }
                });
    }

}
