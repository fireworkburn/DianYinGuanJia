package com.znt.vodbox.mvp.v;

import java.util.List;

import com.znt.vodbox.entity.PlanInfor;

public interface IPlanOperView 
{
	public void requestRunning(int requestId);
	public void requestFailed(int requestId, String error);
	public void requestSuccess(int requestId, Object obj, int total);
	public String getTerminalId();
}
