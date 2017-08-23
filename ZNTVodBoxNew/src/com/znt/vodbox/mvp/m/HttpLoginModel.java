package com.znt.vodbox.mvp.m;

import java.util.Map;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.znt.diange.mina.entity.UserInfor;
import com.znt.vodbox.factory.IJsonParse;
import com.znt.vodbox.factory.JsonParseFactory;
import com.znt.vodbox.http.HttpAPI;
import com.znt.vodbox.http.HttpRequestID;
import com.znt.vodbox.http.HttpResult;
import com.znt.vodbox.mvp.v.IHttpLoginView;

import okhttp3.Call;
import okhttp3.Request;

public class HttpLoginModel extends HttpAPI
{
	
	private boolean isRuning = false;
	private IHttpLoginView iHttpLoginView = null;
	
	public HttpLoginModel(IHttpLoginView iHttpLoginView)
	{
		this.iHttpLoginView = iHttpLoginView;
	}
	
	/**
	 * зЂВс
	 * @param params
	 */
	public void userLogin(Map<String, String> params)
    {
		int requestId = HttpRequestID.USER_LOGIN;
		if(isRuning)
			return;
		isRuning = true;
		
		OkHttpUtils//
    	.get()//
    	.url(USER_LOGIN)//
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
				HttpResult httpResult = jsonParse.parseUserInfor(response);
				if(httpResult.isSuccess())
					iHttpLoginView.loginSuccess((UserInfor)httpResult.getReuslt());
				else
					iHttpLoginView.loginFailed(httpResult.getError());
			}
			
			@Override
			public void onError(Call call, Exception e, int requestId) 
			{
				// TODO Auto-generated method stub
				isRuning = false;
				iHttpLoginView.loginFailed(e.getMessage());
			}
			
			@Override
			public void onBefore(Request request, int requestId) {
				// TODO Auto-generated method stub
				iHttpLoginView.loginRunning();
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
