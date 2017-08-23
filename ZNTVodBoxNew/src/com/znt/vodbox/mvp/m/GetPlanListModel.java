package com.znt.vodbox.mvp.m;

import java.util.List;
import java.util.Map;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.znt.vodbox.entity.PlanInfor;
import com.znt.vodbox.factory.IJsonParse;
import com.znt.vodbox.factory.JsonParseFactory;
import com.znt.vodbox.http.HttpAPI;
import com.znt.vodbox.http.HttpRequestID;
import com.znt.vodbox.http.HttpResult;
import com.znt.vodbox.mvp.v.IGetPlanListView;

import okhttp3.Call;
import okhttp3.Request;

public class GetPlanListModel extends HttpAPI
{
	
	private boolean isRuning = false;
	private IGetPlanListView iGetPlanListView = null;
	
	public GetPlanListModel(IGetPlanListView iGetPlanListView)
	{
		this.iGetPlanListView = iGetPlanListView;
	}
	
	/**
	 * 
	 * @param params
	 */
	public void getPlanList(Map<String, String> params)
    {
		int requestId = HttpRequestID.GET_SPEAKER_PLAN_LIST;
		if(isRuning)
			return;
		isRuning = true;
		
		OkHttpUtils//
    	.get()//
    	.url(GET_CUR_PLAN)//
    	.id(requestId)
    	.params(params)//
    	.build()//
    	.execute(new StringCallback() 
    	{
			@Override
			public void onResponse(String response, int requestId) 
			{
				// TODO Auto-generated method stub
				isRuning = false;
				
				IJsonParse jsonParse = new JsonParseFactory();
				HttpResult httpResult = jsonParse.parsePlanList(response);
				if(httpResult.isSuccess())
					iGetPlanListView.requestSuccess(requestId, (List<PlanInfor>)httpResult.getReuslt(), httpResult.getTotal());
				else
					iGetPlanListView.requestFailed(requestId, httpResult.getError());
			}
			
			@Override
			public void onError(Call call, Exception e, int requestId) 
			{
				// TODO Auto-generated method stub
				isRuning = false;
				iGetPlanListView.requestFailed(requestId, e.getMessage());
			}
			
			@Override
			public void onBefore(Request request, int requestId) {
				// TODO Auto-generated method stub
				super.onBefore(request, requestId);
			}
			@Override
			public void onAfter(int requestId) {
				// TODO Auto-generated method stub
				super.onAfter(requestId);
				isRuning = false;
			}
		});
    }
	/**
	 * 
	 * @param params
	 */
	public void getBoxPlan(Map<String, String> params)
	{
		int requestId = HttpRequestID.GET_SPEAKER_PLAN_LIST;
		if(isRuning)
			return;
		isRuning = true;
		
		OkHttpUtils//
		.get()//
		.url(GET_BOX_PLAN)//
		.id(requestId)
		.params(params)//
		.build()//
		.execute(new StringCallback() 
		{
			@Override
			public void onResponse(String response, int requestId) 
			{
				// TODO Auto-generated method stub
				isRuning = false;
				
				IJsonParse jsonParse = new JsonParseFactory();
				HttpResult httpResult = jsonParse.parseBoxPlanList(response);
				if(httpResult.isSuccess())
					iGetPlanListView.requestSuccess(requestId, (List<PlanInfor>)httpResult.getReuslt(), httpResult.getTotal());
				else
					iGetPlanListView.requestFailed(requestId, httpResult.getError());
			}
			
			@Override
			public void onError(Call call, Exception e, int requestId) 
			{
				// TODO Auto-generated method stub
				isRuning = false;
				iGetPlanListView.requestFailed(requestId, e.getMessage());
			}
			
			@Override
			public void onBefore(Request request, int requestId) {
				// TODO Auto-generated method stub
				super.onBefore(request, requestId);
			}
			@Override
			public void onAfter(int requestId) {
				// TODO Auto-generated method stub
				super.onAfter(requestId);
				isRuning = false;
			}
		});
	}
	public void deletePlan(Map<String, String> params)
	{
		int requestId = HttpRequestID.GET_SPEAKER_PLAN_LIST;
		if(isRuning)
			return;
		isRuning = true;
		
		OkHttpUtils//
		.get()//
		.url(GET_CUR_PLAN)//
		.id(requestId)
		.params(params)//
		.build()//
		.execute(new StringCallback() 
		{
			@Override
			public void onResponse(String response, int requestId) 
			{
				// TODO Auto-generated method stub
				isRuning = false;
				
				IJsonParse jsonParse = new JsonParseFactory();
				HttpResult httpResult = jsonParse.parsePlanList(response);
				if(httpResult.isSuccess())
					iGetPlanListView.requestSuccess(requestId, (List<PlanInfor>)httpResult.getReuslt(), httpResult.getTotal());
				else
					iGetPlanListView.requestFailed(requestId, httpResult.getError());
			}
			
			@Override
			public void onError(Call call, Exception e, int requestId) 
			{
				// TODO Auto-generated method stub
				isRuning = false;
				iGetPlanListView.requestFailed(requestId, e.getMessage());
			}
			
			@Override
			public void onBefore(Request request, int requestId) {
				// TODO Auto-generated method stub
				super.onBefore(request, requestId);
			}
			@Override
			public void onAfter(int requestId) {
				// TODO Auto-generated method stub
				super.onAfter(requestId);
				isRuning = false;
			}
		});
	}
	public void createPlan(Map<String, String> params)
	{
		int requestId = HttpRequestID.GET_SPEAKER_PLAN_LIST;
		if(isRuning)
			return;
		isRuning = true;
		
		OkHttpUtils//
		.get()//
		.url(GET_CUR_PLAN)//
		.id(requestId)
		.params(params)//
		.build()//
		.execute(new StringCallback() 
		{
			@Override
			public void onResponse(String response, int requestId) 
			{
				// TODO Auto-generated method stub
				isRuning = false;
				
				IJsonParse jsonParse = new JsonParseFactory();
				HttpResult httpResult = jsonParse.parsePlanList(response);
				if(httpResult.isSuccess())
					iGetPlanListView.requestSuccess(requestId, (List<PlanInfor>)httpResult.getReuslt(), httpResult.getTotal());
				else
					iGetPlanListView.requestFailed(requestId, httpResult.getError());
			}
			
			@Override
			public void onError(Call call, Exception e, int requestId) 
			{
				// TODO Auto-generated method stub
				isRuning = false;
				iGetPlanListView.requestFailed(requestId, e.getMessage());
			}
			
			@Override
			public void onBefore(Request request, int requestId) {
				// TODO Auto-generated method stub
				super.onBefore(request, requestId);
			}
			@Override
			public void onAfter(int requestId) {
				// TODO Auto-generated method stub
				super.onAfter(requestId);
				isRuning = false;
			}
		});
	}
}