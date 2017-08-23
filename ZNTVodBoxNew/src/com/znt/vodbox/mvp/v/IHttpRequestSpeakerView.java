package com.znt.vodbox.mvp.v;

import java.util.List;

import com.znt.diange.mina.cmd.DeviceInfor;

public interface IHttpRequestSpeakerView 
{
	public void requestRunning(int requestId);
	public void requestFailed(int requestId, String error);
	public void requestSuccess(int requestId, List<DeviceInfor> tempList);
	//public void networkError(int requestId);
}
