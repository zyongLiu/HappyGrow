package com.liu.happygrow.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.liu.happygrow.R;
import com.liu.happygrow.activity.ImageActivity;
import com.liu.happygrow.adapter.GirlAdapter;
import com.liu.happygrow.adapter.base.CommonAdapter;
import com.liu.happygrow.adapter.base.ViewHolder;
import com.liu.happygrow.bean.GanHuo;
import com.liu.happygrow.callback.GanHuoCallBack;
import com.liu.happygrow.constant.System_constant;
import com.liu.happygrow.swiperefreshendless.EndlessRecyclerOnScrollListener;
import com.liu.happygrow.swiperefreshendless.GridSpacingItemDecoration;
import com.liu.happygrow.swiperefreshendless.HeaderViewRecyclerAdapter;
import com.liu.happygrow.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;

/**
 * Created by Liu on 2016/3/6.
 */
public class VelfareFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,GirlAdapter.OnItemActionListener{
    private View view;
    private int PAGER_COUNT=10;
    private int pager_num=1;
    private String url="";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private HeaderViewRecyclerAdapter stringAdapter;

    private CommonAdapter<GanHuo> ganHuoCommonAdapter;

    private List<GanHuo> ganhuo;

    private GirlAdapter refreshAdapter;

    private ProgressBar pgb;

    public static VelfareFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        VelfareFragment fragment = new VelfareFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_velfare,container,false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        pgb= (ProgressBar) view.findViewById(R.id.pgb);
        pgb.setVisibility(View.VISIBLE);

        swipeRefreshLayout.setColorSchemeResources(
                R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow
        );
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        int spacingInPixels = 26;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));
        setData();


        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return ((stringAdapter.getItemCount() - 1) == position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                simulateLoadMoreData();
            }
        });



        return view;
    }

    private boolean isLoading=false;

    private void simulateLoadMoreData() {
//        Toast.makeText(getActivity(),"simulateLoadMoreData",Toast.LENGTH_SHORT).show();
//        stringAdapter.notifyDataSetChanged();
        pager_num++;
        isLoading=true;
        setData();
    }


    private void createLoadMoreView() {
        View loadMoreView = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.view_load_more, mRecyclerView, false);
        stringAdapter.addFooterView(loadMoreView);
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
                        pgb.setVisibility(View.GONE);
                        e.printStackTrace();
                        if (isRefresh){
                            Toast.makeText(getActivity(),"刷新失败",Toast.LENGTH_SHORT).show();
                            isRefresh=false;
                            swipeRefreshLayout.setRefreshing(false);
                        }else if (isLoading){
                            isLoading=false;
                            pager_num--;
                            Toast.makeText(getActivity(),"加载失败",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(List<GanHuo> response) {
                        pgb.setVisibility(View.GONE);
                        LogUtils.i(response.toString());
                        if (isRefresh){
                            ganhuo=response;
                            swipeRefreshLayout.setRefreshing(false);
                            stringAdapter.notifyDataSetChanged();
                            isRefresh=false;
                        }else if (isLoading){
                            if (response==null||response.size()==0){
                                pager_num--;
                                Toast.makeText(getActivity(),"无更多资源",Toast.LENGTH_SHORT).show();
                            }else {
                                ganhuo.addAll(response);
                                stringAdapter.notifyDataSetChanged();
                            }
                            isLoading=false;
                        }
                        else {
                        refreshAdapter = new GirlAdapter(ganhuo=response, getActivity());
                            refreshAdapter.setOnItemActionListener(VelfareFragment.this);
                        stringAdapter = new HeaderViewRecyclerAdapter(refreshAdapter);
                        mRecyclerView.setAdapter(stringAdapter);
                            createLoadMoreView();
                        }
                    }
                });
    }

    private boolean isRefresh=false;

    @Override
    public void onRefresh() {
//        Toast.makeText(getActivity(),"Refresh",Toast.LENGTH_SHORT).show();
        isRefresh=true;
        setData();
        pager_num=0;
    }


    @Override
    public void onItemClickListener(View v, int pos) {
        Toast.makeText(getActivity(),"onItemClickListener"+pos,Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getActivity(), ImageActivity.class);
        intent.putExtra("url",ganhuo.get(pos).getUrl());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClickListener(View v, int pos) {
        Toast.makeText(getActivity(),"onItemLongClickListener"+pos,Toast.LENGTH_SHORT).show();
        return false;
    }
}
