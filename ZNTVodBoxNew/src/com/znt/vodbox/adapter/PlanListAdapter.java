package com.znt.vodbox.adapter;

import java.util.List;

import com.znt.vodbox.entity.PlanInfor;
import com.znt.vodbox.holder.BaseViewHolder;
import com.znt.vodbox.holder.PlanItemVH;

import android.content.Context;
import android.view.ViewGroup;
/**
* @Author：yuyan
* @Time：2017年6月18日 下午6:22:10
* @Project：InforStream
* @Des:适配器
*/
public class PlanListAdapter extends RecycleViewAdapter<PlanInfor>
{

	private Context context = null;
	
	public PlanListAdapter(Context context, List<PlanInfor> data) 
	{
		super(context, data);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public BaseViewHolder<PlanInfor> onCreateBaseViewHolder(ViewGroup parent, int viewType) 
	{
		// TODO Auto-generated method stub
		BaseViewHolder<PlanInfor> viewHolder = null;
		if(viewType == 0)// 没有图片
    	{
        	viewHolder = new PlanItemVH(parent);
    	}
        return viewHolder;
	}
	
	@Override
	public void onBindViewHolder(BaseViewHolder<PlanInfor> holder, int pos) 
	{
		// TODO Auto-generated method stub
		super.onBindViewHolder(holder, pos);
		
		final int position = pos - getHeadersCount();
		
		if(mData.size() <= 0 || position >= mData.size() || position < 0)
    		return;
    	PlanInfor bean = (PlanInfor) mData.get(position);
    	holder.setTag(position, bean);
    	holder.bindHolder(bean);
	}
}
