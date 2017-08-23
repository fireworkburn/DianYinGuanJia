package com.znt.vodbox.mvp.v;

import com.znt.diange.mina.entity.UserInfor;

public interface IHttpLoginView 
{
	public String getLoginAccount();
	public String getLoginPwd();
	public void loginRunning();
	public void loginFailed(String error);
	public void loginSuccess(UserInfor info);
}
