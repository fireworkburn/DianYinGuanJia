package com.znt.vodbox.holder;

import java.util.ArrayList;
import java.util.List;

import com.zhy.http.okhttp.request.GetRequest;
import com.znt.vodbox.R;
import com.znt.vodbox.activity.PlanDetailActivity;
import com.znt.vodbox.entity.PlanInfor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class PlanItemVH extends BaseViewHolder<PlanInfor>
{
	
	private Context context = null;
	
	TextView tvName = null;
	TextView tvTime = null;
	TextView tvTypeTime = null;
	TextView tvTypeShop = null;
	View viewUsePlan = null;
	View viewDeletePlan = null;
	View viewOperation = null;
	private String status = "0";
	
	private PlanInfor planInfor = null;
	private List<PlanInfor> planList = new ArrayList<PlanInfor>();
	
	public PlanItemVH(ViewGroup parent)
	{
		super(parent, R.layout.view_parent_plan_item);
		// TODO Auto-generated constructor stub
		this.context = parent.getContext();
		onInitializeView();
	}
	
	@Override
    public void onInitializeView()
    {
		tvName = (TextView)findViewById(R.id.tv_parent_plan_item_name);
		tvTime = (TextView)findViewById(R.id.tv_parent_plan_item_time);
		tvTypeTime = (TextView)findViewById(R.id.tv_parent_plan_item_tpye_shop);
		tvTypeShop = (TextView)findViewById(R.id.tv_parent_plan_item_type_time);
		viewUsePlan = findViewById(R.id.view_parent_plan_item_use);
		viewDeletePlan = findViewById(R.id.view_parent_plan_item_delete);
		viewOperation = findViewById(R.id.view_parent_plan_item_right);
		
		viewUsePlan.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				int pos = (Integer) itemView.getTag();
				PlanInfor tempInfor = planList.get(pos);
				//httpFactory.startOldPlan(tempInfor.getPlanId());
			}
		});
		viewDeletePlan.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				int pos = (Integer) itemView.getTag();
				PlanInfor tempInfor = planList.get(pos);
				//httpFactory.deleteOldPlan(tempInfor.getPlanId());
			}
		});
		
		itemView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				intent.setClass(context, PlanDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("PlanInfor", planInfor);
				bundle.putBoolean("IS_EDIT", true);
				intent.putExtras(bundle);
				//context.startActivityForResult(intent, 2);
				context.startActivity(intent);
			}
		});
    }

	@Override
	public void bindHolder(final PlanInfor infor) 
	{
		// TODO Auto-generated method stub
		this.planInfor = infor;
		if(status.equals("0"))
			viewOperation.setVisibility(View.GONE);
		else
			viewOperation.setVisibility(View.VISIBLE);
		
		tvName.setText(infor.getPlanName());
		tvTime.setText(context.getResources().getString(R.string.plan_create_time) + infor.getPublishTimeFormat());
		
		
		String planFlag = infor.getPlanFlag();
		if(planFlag.equals("0"))
		{
			tvTypeTime.setText(context.getResources().getString(R.string.plan_application_type_all));
		}
		else
		{
			tvTypeTime.setText(context.getResources().getString(R.string.plan_application_type) + infor.getDeviceList().size() + " ¸ö");
		}
		
		String planType = infor.getPlanType();
		if(planType.equals("0"))
		{
			tvTypeShop.setText(context.getResources().getString(R.string.plan_time_type) + infor.getStartDate() + "~" + infor.getEndDate());
		}
		else
		{
			tvTypeShop.setText(context.getResources().getString(R.string.plan_time_all));
		}
	}

	@Override
	public void setTag(int pos, Object bean) {
		// TODO Auto-generated method stub
		super.setTag(pos, bean);
	}
	
	@Override
	public View getOperationView() 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
