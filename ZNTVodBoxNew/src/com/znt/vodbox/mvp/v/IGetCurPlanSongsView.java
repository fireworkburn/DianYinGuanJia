package com.znt.vodbox.mvp.v;

import java.util.List;

import com.znt.diange.mina.entity.SongInfor;

public interface IGetCurPlanSongsView 
{
	public void requestRunning(int requestId);
	public void requestFailed(int requestId, String error);
	public void requestSuccess(int requestId, List<SongInfor> songList, int total);
	public String getTerminalId();
}
