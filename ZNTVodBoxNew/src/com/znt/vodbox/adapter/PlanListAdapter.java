package com.znt.vodbox.adapter;

import java.util.List;

import com.znt.vodbox.entity.PlanInfor;
import com.znt.vodbox.holder.BaseViewHolder;
import com.znt.vodbox.holder.PlanItemVH;
import com.znt.vodbox.holder.PlanItemVH.OnItemOperClickListener;

import android.content.Context;
import android.view.ViewGroup;
/**
* @Author��yuyan
* @Time��2017��6��18�� ����6:22:10
* @Project��InforStream
* @Des:������
*/
public class PlanListAdapter extends RecycleViewAdapter<PlanInfor> 
{

	private Context context = null;
	private String status = "0";
	private OnItemOperClickListener onItemOperClickListener = null;
	public PlanListAdapter(Context context, List<PlanInfor> data, OnItemOperClickListener onItemOperClickListener) 
	{
		super(context, data);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.onItemOperClickListener = onItemOperClickListener;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
	
	@Override
	public BaseViewHolder<PlanInfor> onCreateBaseViewHolder(ViewGroup parent, int viewType) 
	{
		// TODO Auto-generated method stub
		BaseViewHolder<PlanInfor> viewHolder = null;
		if(viewType == 0)// û��ͼƬ
    	{
        	viewHolder = new PlanItemVH(parent, onItemOperClickListener, status);
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
