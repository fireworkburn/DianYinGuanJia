package com.znt.vodbox.mvp.m;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.znt.vodbox.entity.PlanInfor;
import com.znt.vodbox.factory.IJsonParse;
import com.znt.vodbox.factory.JsonParseFactory;
import com.znt.vodbox.http.HttpAPI;
import com.znt.vodbox.http.HttpRequestID;
import com.znt.vodbox.http.HttpResult;
import com.znt.vodbox.mvp.v.IPlanOperView;

import okhttp3.Call;
import okhttp3.Request;

public class GetPlanListModel extends HttpAPI
{
	
	private boolean isRuning = false;
	private IPlanOperView iGetPlanListView = null;
	
	public GetPlanListModel(IPlanOperView iGetPlanListView)
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
				iGetPlanListView.requestRunning(requestId);
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
				iGetPlanListView.requestRunning(requestId);
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
	
	public void deletePlan(Map<String, String> params)
	{
		int requestId = HttpRequestID.DELETE_PLAN;
		if(isRuning)
			return;
		isRuning = true;
		
		OkHttpUtils//
		.get()//
		.url(PLAN_DELETE)//
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
				try
				{
					JSONObject jsonObject = new JSONObject(response);
					int result = jsonObject.getInt(RESULT_OK);
					if(result == 0)
					{
						String code = getInforFromJason(jsonObject, RESULT_INFO);
						iGetPlanListView.requestSuccess(requestId, code, 0);
					}
					else
					{
						iGetPlanListView.requestFailed(requestId, "");
					}
				} 
				catch (JSONException e)
				{
					iGetPlanListView.requestFailed(requestId, e.getMessage());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	
	public void startPlan(Map<String, String> params)
	{
		int requestId = HttpRequestID.START_PLAN;
		if(isRuning)
			return;
		isRuning = true;
		
		OkHttpUtils//
		.get()//
		.url(PLAN_START)//
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
				
				isRuning = false;
				try
				{
					JSONObject jsonObject = new JSONObject(response);
					int result = jsonObject.getInt(RESULT_OK);
					if(result == 0)
					{
						String code = getInforFromJason(jsonObject, RESULT_INFO);
						iGetPlanListView.requestSuccess(requestId, code, 0);
					}
					else
					{
						iGetPlanListView.requestFailed(requestId, "");
					}
				} 
				catch (JSONException e)
				{
					iGetPlanListView.requestFailed(requestId, e.getMessage());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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