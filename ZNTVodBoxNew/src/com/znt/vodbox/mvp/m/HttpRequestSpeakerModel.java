package com.znt.vodbox.mvp.m;

import java.util.List;
import java.util.Map;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.znt.diange.mina.cmd.DeviceInfor;
import com.znt.vodbox.factory.IJsonParse;
import com.znt.vodbox.factory.JsonParseFactory;
import com.znt.vodbox.http.HttpAPI;
import com.znt.vodbox.http.HttpRequestID;
import com.znt.vodbox.http.HttpResult;
import com.znt.vodbox.mvp.v.IHttpRequestSpeakerView;

import okhttp3.Call;
import okhttp3.Request;

public class HttpRequestSpeakerModel extends HttpAPI
{
	
	private boolean isRuning = false;
	private IHttpRequestSpeakerView iHttpRequestSpeakerView = null;
	
	public HttpRequestSpeakerModel(IHttpRequestSpeakerView iHttpRequestSpeakerView)
	{
		this.iHttpRequestSpeakerView = iHttpRequestSpeakerView;
	}
	
	/**
	 * зЂВс
	 * @param params
	 */
	public void getBindedSpeakers(Map<String, String> params)
    {
		int requestId = HttpRequestID.GET_BIND_SPEAKERS;
		if(isRuning)
			return;
		isRuning = true;
		
		OkHttpUtils//
    	.get()//
    	.url(GET_BIND_SPEAKERS)//
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
				HttpResult httpResult = jsonParse.parseSpeakerList(response);
				if(httpResult.isSuccess())
					iHttpRequestSpeakerView.requestSuccess(requestId, (List<DeviceInfor>)httpResult.getReuslt());
				else
					iHttpRequestSpeakerView.requestFailed(requestId, httpResult.getError());
			}
			
			@Override
			public void onError(Call call, Exception e, int requestId) 
			{
				// TODO Auto-generated method stub
				isRuning = false;
				iHttpRequestSpeakerView.requestFailed(requestId, e.getMessage());
			}
			
			@Override
			public void onBefore(Request request, int requestId) {
				// TODO Auto-generated method stub
				super.onBefore(request, requestId);
				iHttpRequestSpeakerView.requestRunning(requestId);
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
