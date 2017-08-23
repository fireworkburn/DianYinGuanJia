package com.znt.vodbox.mvp.m;

import java.util.List;
import java.util.Map;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.znt.diange.mina.entity.SongInfor;
import com.znt.vodbox.factory.IJsonParse;
import com.znt.vodbox.factory.JsonParseFactory;
import com.znt.vodbox.http.HttpAPI;
import com.znt.vodbox.http.HttpRequestID;
import com.znt.vodbox.http.HttpResult;
import com.znt.vodbox.mvp.v.IGetCurPlanSongsView;

import okhttp3.Call;
import okhttp3.Request;

public class GetSpeakerCurSongsModel extends HttpAPI
{
	
	private boolean isRuning = false;
	private IGetCurPlanSongsView iGetCurPlanSongsView = null;
	
	public GetSpeakerCurSongsModel(IGetCurPlanSongsView iGetCurPlanSongsView)
	{
		this.iGetCurPlanSongsView = iGetCurPlanSongsView;
	}
	
	/**
	 * 
	 * @param params
	 */
	public void getCurPlanMusics(Map<String, String> params)
    {
		int requestId = HttpRequestID.GET_SPEAKER_CUR_MUSICS;
		if(isRuning)
			return;
		isRuning = true;
		
		OkHttpUtils//
    	.get()//
    	.url(GET_PLAN_MUSICS)//
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
				HttpResult httpResult = jsonParse.parseSongList(response);
				if(httpResult.isSuccess())
					iGetCurPlanSongsView.requestSuccess(requestId, (List<SongInfor>)httpResult.getReuslt(), httpResult.getTotal());
				else
					iGetCurPlanSongsView.requestFailed(requestId, httpResult.getError());
			}
			
			@Override
			public void onError(Call call, Exception e, int requestId) 
			{
				// TODO Auto-generated method stub
				isRuning = false;
				iGetCurPlanSongsView.requestFailed(requestId, e.getMessage());
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