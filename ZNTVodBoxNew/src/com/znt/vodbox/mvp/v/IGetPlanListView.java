package com.znt.vodbox.mvp.v;

import java.util.List;

import com.znt.vodbox.entity.PlanInfor;

public interface IGetPlanListView 
{
	public void requestRunning(int requestId);
	public void requestFailed(int requestId, String error);
	public void requestSuccess(int requestId, List<PlanInfor> songList, int total);
	public String getTerminalId();
}
