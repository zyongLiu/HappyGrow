package com.liu.happygrow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.happygrow.R;
import com.liu.happygrow.bean.GanHuo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Liu on 2016/3/8.
 */
public class GirlAdapter extends RecyclerView.Adapter<GirlAdapter.ViewHolder>{
    private List<GanHuo> mGanHuoList;
    private Context context;

    private OnItemActionListener mOnItemActionListener;
    /**********定义点击事件**********/
    public   interface OnItemActionListener
    {
        public   void onItemClickListener(View v,int pos);
        public   boolean onItemLongClickListener(View v,int pos);
    }
    public void setOnItemActionListener(OnItemActionListener onItemActionListener) {
        this.mOnItemActionListener = onItemActionListener;
    }

    public GirlAdapter(List<GanHuo> mGanHuoList, Context context) {
        this.mGanHuoList = mGanHuoList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_velfare,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tv_velfare.setText(mGanHuoList.get(position).getPublishedAt().split("T")[0]);
        holder.tv_velfare.setVisibility(View.GONE);
        Picasso.with(context).load(mGanHuoList.get(position).getUrl())
                .resize(500, 800)
                .centerCrop()
                .into(holder.iv_velfare);
        if(mOnItemActionListener!=null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //注意这里必须使用viewHolder.getPosition()而不能用i，因为为了保证动画，没有使用NotifyDatasetChanged更新位置数据
                    mOnItemActionListener.onItemClickListener(v,holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //注意这里必须使用viewHolder.getPosition()而不能用i，因为为了保证动画，没有使用NotifyDatasetChanged更新位置数据
                    return mOnItemActionListener.onItemLongClickListener(v, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mGanHuoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_velfare;
        ImageView iv_velfare;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_velfare= (TextView) itemView.findViewById(R.id.tv_velfare);
            iv_velfare= (ImageView) itemView.findViewById(R.id.iv_velfare);
        }
    }
}
