package com.znt.vodbox.factory;

import com.znt.vodbox.http.HttpResult;

public interface IJsonParse 
{
	public HttpResult parseUserInfor(String response);
	public HttpResult parseSpeakerList(String response);
	public HttpResult parseSongList(String response);
	public HttpResult parsePlanList(String response);
	public HttpResult parseBoxPlanList(String response);
}
