package com.znt.vodbox.holder;

import com.znt.diange.mina.cmd.DeviceInfor;
import com.znt.vodbox.R;
import com.znt.vodbox.activity.ShopDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NormalDeviceVH extends BaseViewHolder<DeviceInfor>
{
	
	private Context context = null;
	
	private TextView tvShopName = null;
	private TextView tvPlaySong = null;
	private TextView tvShopAddr = null;
	private TextView tvNetStatus = null;
	private ImageView ivNetStatus = null;
	private ImageView ivLargePic = null;
	
	private DeviceInfor deviceInfor = null;
	
	public NormalDeviceVH(ViewGroup parent)
	{
		super(parent, R.layout.view_item_device_infor);
		// TODO Auto-generated constructor stub
		this.context = parent.getContext();
		onInitializeView();
	}
	
	@Override
    public void onInitializeView()
    {
		tvShopName = (TextView)itemView.findViewById(R.id.tv_item_device_infor_name);
		tvPlaySong = (TextView)itemView.findViewById(R.id.tv_item_device_infor_song);
		tvShopAddr = (TextView)itemView.findViewById(R.id.tv_item_device_infor_addr);
		tvNetStatus = (TextView)itemView.findViewById(R.id.tv_device_item_infor_shop_net_status);
		ivNetStatus = (ImageView)itemView.findViewById(R.id.iv_device_item_infor_shop_net_status);
		
		itemView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, ShopDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("DeviceInfor", deviceInfor);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
    }

	@Override
	public void bindHolder(final DeviceInfor infor) 
	{
		// TODO Auto-generated method stub
		this.deviceInfor = infor;
		tvShopName.setText(infor.getName());
		tvShopAddr.setText(infor.getAddr());
		if(infor.isOnline())
		{
			if(TextUtils.isEmpty(infor.getCurPlaySong()))
				tvPlaySong.setText(context.getResources().getString(R.string.no_song_play));
			else
				tvPlaySong.setText(infor.getCurPlaySong());
			
			tvNetStatus.setText(context.getResources().getString(R.string.net_status_online));
			tvNetStatus.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
			ivNetStatus.setImageResource(R.drawable.icon_net_status_online);
		}
		else
		{
			if(TextUtils.isEmpty(infor.getCurPlaySong()))
				tvPlaySong.setText(context.getResources().getString(R.string.no_song_play_offline));
			else
				tvPlaySong.setText(infor.getCurPlaySong());
			tvNetStatus.setTextColor(context.getResources().getColor(R.color.red));
			tvNetStatus.setText(context.getResources().getString(R.string.net_status_offline));
			ivNetStatus.setImageResource(R.drawable.icon_net_status_offline);
		}
	}

	@Override
	public View getOperationView() 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
