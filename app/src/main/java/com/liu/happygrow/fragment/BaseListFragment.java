package com.liu.happygrow.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.liu.happygrow.R;
import com.liu.happygrow.activity.WebViewActivity;
import com.liu.happygrow.adapter.base.CommonAdapter;
import com.liu.happygrow.adapter.base.ViewHolder;
import com.liu.happygrow.bean.GanHuo;
import com.liu.happygrow.callback.GanHuoCallBack;
import com.liu.happygrow.constant.System_constant;
import com.liu.happygrow.utils.LogUtils;
import com.liu.happygrow.utils.PreferenceUtil;
import com.liu.happygrow.view.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Liu on 2016/3/5.
 */
public class BaseListFragment extends Fragment implements XListView.IXListViewListener{

    private View view;
    private XListView xlv_content;

    private int PAGER_COUNT=20;
    private int pager_num=1;
    private String url="";

    private CommonAdapter<GanHuo> ganHuoCommonAdapter;

    private List<GanHuo> ganhuo;

    private boolean isRefresh=false;
    private boolean isLoadingMore=false;

    private ProgressBar pgb;

    public static BaseListFragment newInstance(String type) {
        BaseListFragment f=new BaseListFragment();
        Bundle args=new Bundle();
        args.putString("type", type);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_android,container,false);
        xlv_content= (XListView) view.findViewById(R.id.xlv_content);
        pgb= (ProgressBar) view.findViewById(R.id.pgb);
        pgb.setVisibility(View.VISIBLE);
        xlv_content.setPullLoadEnable(true);
        xlv_content.setXListViewListener(this);
        setData();
        initEvent();
        return view;
    }

    private void initEvent() {
        xlv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", ganhuo.get(position - 1).getUrl());
                startActivity(intent);
            }
        });
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
                        pgb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(List<GanHuo> response) {
                        LogUtils.i(response.toString());
                        pgb.setVisibility(View.GONE);
                        if (isRefresh) {
                            ganhuo.clear();
                            isRefresh = false;
                            onLoad();
                        } else if (isLoadingMore) {
                            ganhuo.addAll(response);
                            isLoadingMore = false;
                            onLoad();
                            ganHuoCommonAdapter.notifyDataSetChanged();
                            return;
                        }
                            xlv_content.setAdapter(ganHuoCommonAdapter = new CommonAdapter<GanHuo>(getActivity(), ganhuo = response, R.layout.item_android) {
                                @Override
                                public void convert(ViewHolder holder, GanHuo ganHuo) {
                                    holder.setText(R.id.tv_ganhuo_title, ganHuo.getDesc());
                                    holder.setText(R.id.tv_ganhuo_from, ganHuo.getSource());
                                    holder.setText(R.id.tv_ganhuo_time, getDateDiff(ganHuo.getPublishedAt()));
                                }
                            });
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    private void onLoad() {
        xlv_content.stopRefresh();
        xlv_content.stopLoadMore();
        setUpdateTime();
        xlv_content.setRefreshTime(getUpdateTime());
    }

    @Override
    public void onRefresh() {
        pager_num=1;
        isRefresh=true;
        setData();
    }

    @Override
    public void onLoadMore() {
        pager_num++;
        isLoadingMore=true;
        setData();
    }

    /**
     * 设置上次更新时间
     */
    private void setUpdateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日    HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);

        PreferenceUtil.put(getActivity(), "UpdateTime", str);
    }

    /**
     * 获取上次更新时间
     */
    private String getUpdateTime() {
        return (String) PreferenceUtil.get(getActivity(),"UpdateTime",null);
    }

    /**
     * 获取发布时间距今多久
     * @param string 发布时间:2016-03-04T12:44:51.926Z
     * @return
     * @throws ParseException
     */
    private String getDateDiff(String string)  {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date publishDate= null;
        try {
            publishDate = format.parse(string.replace("T"," ").replace("Z"," "));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        Date newDate= new Date(System.currentTimeMillis());
        //相隔毫秒数转化为天数
        long day=newDate.getTime()-publishDate.getTime();

        SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat("yyyy");
        if (!sDateFormat.format(newDate).equals(sDateFormat.format(publishDate))){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            String str=sdf.format(publishDate);
            return str;
        }
        else if(day/(1000*60*60*24)>30){
            SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日 HH:mm");
            String str=sdf.format(publishDate);
            return str;
        }
        else if(day/(1000*60*60*24)>1){
            return (int)((day/(1000*60*60*24))-0.5)+"天前";
        }else if (day/(1000*60*60)>1){
            return (int)((day/(1000*60*60))-0.5)+"小时前";
        }else if (day/(1000*60)>1){
            return (int)((day/(1000*60))-0.5)+"分钟前";
        }else {
            return "刚刚";
        }
    }
}
