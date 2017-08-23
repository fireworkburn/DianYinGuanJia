package com.znt.vodbox.mvp.p;

import java.util.HashMap;
import java.util.Map;

import com.znt.vodbox.R;
import com.znt.vodbox.mvp.m.HttpLoginModel;
import com.znt.vodbox.mvp.v.IHttpLoginView;
import com.znt.vodbox.utils.SystemUtils;

import android.content.Context;

public class HttpLoginPresenter 
{

	private Context context = null;
	private IHttpLoginView iHttpLoginView = null;
	private HttpLoginModel httpLoginModel = null;
	
	public HttpLoginPresenter(Context context, IHttpLoginView iHttpLoginView)
	{
		this.context = context;
		this.iHttpLoginView = iHttpLoginView;
		httpLoginModel = new HttpLoginModel(iHttpLoginView);
	}
	
	public void userLogin()
	{
		if(!SystemUtils.isNetConnected(context)
				&& !SystemUtils.is3gConnected(context))
		{
			iHttpLoginView.loginFailed(context.getString(R.string.network_error));
			return;
		}
		
		String loginId = iHttpLoginView.getLoginAccount();
		String password = iHttpLoginView.getLoginPwd();
				
		if(password.length() < 6)
		{
			iHttpLoginView.loginFailed(context.getString(R.string.account_six_hint));
			return;
		}
		else if(password.length() > 16)
		{
			iHttpLoginView.loginFailed(context.getString(R.string.pwd_16_hint));
			return;
		}
		
		if(loginId.contains(" "))
			loginId = loginId.replace(" ", "");
		if(loginId.contains("-"))
			loginId = loginId.replace("-", "");
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginId", loginId);
		params.put("password", password);
		httpLoginModel.userLogin(params);
	}
}
