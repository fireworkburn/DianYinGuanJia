package com.znt.vodbox.adapter;

import java.util.List;

import com.znt.diange.mina.cmd.DeviceInfor;
import com.znt.vodbox.holder.BaseViewHolder;
import com.znt.vodbox.holder.NormalDeviceVH;

import android.content.Context;
import android.view.ViewGroup;
/**
* @Author：yuyan
* @Time：2017年6月18日 下午6:22:10
* @Project：InforStream
* @Des:适配器
*/
public class DeviceInforAdapter extends RecycleViewAdapter<DeviceInfor>
{

	private Context context = null;
	
	public DeviceInforAdapter(Context context, List<DeviceInfor> data) 
	{
		super(context, data);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public BaseViewHolder<DeviceInfor> onCreateBaseViewHolder(ViewGroup parent, int viewType) 
	{
		// TODO Auto-generated method stub
		BaseViewHolder<DeviceInfor> viewHolder = null;
		if(viewType == 0)// 没有图片
    	{
        	viewHolder = new NormalDeviceVH(parent);
    	}
        return viewHolder;
	}
	
	@Override
	public void onBindViewHolder(BaseViewHolder<DeviceInfor> holder, int pos) 
	{
		// TODO Auto-generated method stub
		super.onBindViewHolder(holder, pos);
		
		final int position = pos - getHeadersCount();
		
		if(mData.size() <= 0 || position >= mData.size() || position < 0)
    		return;
    	DeviceInfor bean = (DeviceInfor) mData.get(position);
    	holder.setTag(position, bean);
    	holder.bindHolder(bean);
	}
}
