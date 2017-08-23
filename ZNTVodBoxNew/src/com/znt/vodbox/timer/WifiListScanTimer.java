package com.znt.vodbox.timer;

import android.content.Context;

public class WifiListScanTimer extends AbstractTimer
{
	private int maxTime = 0;
	public WifiListScanTimer(Context context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		setTimeInterval(2000);
	}
	public void setMaxTime(int maxTime)
	{
		this.maxTime = maxTime;
	}
	
	public boolean isOver()
	{
		if(maxTime > 0 && countTime > 0 && countTime >= maxTime)
		{
			stopTimer();
			return true;
		}
		return false;
	}
}
