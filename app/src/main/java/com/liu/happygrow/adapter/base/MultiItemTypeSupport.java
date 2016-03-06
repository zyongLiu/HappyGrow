package com.liu.happygrow.adapter.base;

/**
 * Created by Liu on 2016/3/5.
 */
public interface MultiItemTypeSupport<T>
{
    int getLayoutId(int position, T t);

    int getViewTypeCount();

    int getItemViewType(int postion, T t);
}
