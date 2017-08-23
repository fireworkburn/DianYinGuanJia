package com.znt.vodbox.mvp.p;

import java.util.HashMap;
import java.util.Map;

import com.znt.vodbox.R;
import com.znt.vodbox.entity.LocalDataEntity;
import com.znt.vodbox.http.HttpRequestID;
import com.znt.vodbox.mvp.m.GetPlanListModel;
import com.znt.vodbox.mvp.v.IGetPlanListView;
import com.znt.vodbox.utils.SystemUtils;

import android.content.Context;
import android.text.TextUtils;

public class GetPlanListPresenter 
{
	private Context context = null;
	private IGetPlanListView iGetPlanListView = null;
	private GetPlanListModel getPlanListModel = null;
	public GetPlanListPresenter(Context context, IGetPlanListView iGetPlanListView)
	{
		this.context = context;
		this.iGetPlanListView = iGetPlanListView;
		getPlanListModel = new GetPlanListModel(iGetPlanListView);
	}
	
	public void getCurPlan(String uid, String status, int pageNo)
	{
		if(!SystemUtils.isNetConnected(context)
				&& !SystemUtils.is3gConnected(context))
		{
			iGetPlanListView.requestFailed(HttpRequestID.GET_SPEAKER_PLAN_LIST, context.getString(R.string.network_error));
			return;
		}
		
		if(TextUtils.isEmpty(uid))
			uid = LocalDataEntity.newInstance(context).getUserInfor().getUserId();
		if(TextUtils.isEmpty(uid))
			return;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("memberId", uid);
		params.put("status", status);
		params.put("pageSize", "25");
		params.put("pageNo", pageNo + "");
		
		getPlanListModel.getPlanList(params);
	}
	public void getBoxPlan(String status, int pageNo, String terminalId)
	{
		if(!SystemUtils.isNetConnected(context)
				&& !SystemUtils.is3gConnected(context))
		{
			iGetPlanListView.requestFailed(HttpRequestID.GET_SPEAKER_PLAN_LIST, context.getString(R.string.network_error));
			return;
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("terminalId", terminalId);
		params.put("status", status);
		params.put("pageSize", "25");
		params.put("pageNo", pageNo + "");
		
		getPlanListModel.getBoxPlan(params);
	}
}
