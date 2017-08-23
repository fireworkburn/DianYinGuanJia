package com.znt.vodbox.mvp.p;

import java.util.HashMap;
import java.util.Map;

import com.znt.vodbox.R;
import com.znt.vodbox.entity.LocalDataEntity;
import com.znt.vodbox.http.HttpRequestID;
import com.znt.vodbox.mvp.m.HttpRequestSpeakerModel;
import com.znt.vodbox.mvp.v.IHttpRequestSpeakerView;
import com.znt.vodbox.utils.SystemUtils;

import android.content.Context;
import android.text.TextUtils;

public class HttpGetSpeakerPresenter 
{
	public final int MAX_PAGE_SIZE = 25;
	
	private Context context = null;
	private IHttpRequestSpeakerView iHttpRequestSpeakerView = null;
	private HttpRequestSpeakerModel httpRequestSpeakerModel = null;
	
	public HttpGetSpeakerPresenter(Context context, IHttpRequestSpeakerView iHttpRequestSpeakerView)
	{
		this.context = context;
		this.iHttpRequestSpeakerView = iHttpRequestSpeakerView;
		httpRequestSpeakerModel = new HttpRequestSpeakerModel(iHttpRequestSpeakerView);
	}
	
	public void getBindedSpeakers(int pageNo)
	{
		if(!SystemUtils.isNetConnected(context)
				&& !SystemUtils.is3gConnected(context))
		{
			iHttpRequestSpeakerView.requestFailed(HttpRequestID.GET_BIND_SPEAKERS, context.getString(R.string.network_error));
			return;
		}
		
		String uid = LocalDataEntity.newInstance(context).getUserInfor().getUserId();
		if(TextUtils.isEmpty(uid))
			return;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("memberId", uid);
		params.put("pageNo", pageNo + "");
		params.put("pageSize", MAX_PAGE_SIZE + "");
		params.put("longitude", LocalDataEntity.newInstance(context).getLon());
		params.put("latitude", LocalDataEntity.newInstance(context).getLat());
		httpRequestSpeakerModel.getBindedSpeakers(params);
	}
	public void getBindedSpeakers(String userId, int pageNo)
	{
		if(!SystemUtils.isNetConnected(context)
				&& !SystemUtils.is3gConnected(context))
		{
			iHttpRequestSpeakerView.requestFailed(HttpRequestID.GET_BIND_SPEAKERS, context.getString(R.string.network_error));
			return;
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("memberId", userId);
		params.put("pageNo", pageNo + "");
		params.put("pageSize", MAX_PAGE_SIZE + "");
		params.put("longitude", LocalDataEntity.newInstance(context).getLon());
		params.put("latitude", LocalDataEntity.newInstance(context).getLat());
		httpRequestSpeakerModel.getBindedSpeakers(params);
	}
	public void getBindedSpeakersByName(int pageNo, String name)
	{
		if(!SystemUtils.isNetConnected(context)
				&& !SystemUtils.is3gConnected(context))
		{
			iHttpRequestSpeakerView.requestFailed(HttpRequestID.GET_BIND_SPEAKERS, context.getString(R.string.network_error));
			return;
		}
		
		String uid = LocalDataEntity.newInstance(context).getUserInfor().getUserId();
		if(TextUtils.isEmpty(uid))
			return;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("memberId", uid);
		params.put("pageNo", pageNo + "");
		params.put("pageSize", MAX_PAGE_SIZE + "");
		params.put("longitude", LocalDataEntity.newInstance(context).getLon());
		params.put("latitude", LocalDataEntity.newInstance(context).getLat());
		httpRequestSpeakerModel.getBindedSpeakers(params);
	}
}
