package com.znt.vodbox.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.znt.diange.mina.cmd.DeviceInfor;
import com.znt.diange.mina.entity.SongInfor;
import com.znt.diange.mina.entity.UserInfor;
import com.znt.vodbox.entity.LocalDataEntity;
import com.znt.vodbox.entity.MusicAlbumInfor;
import com.znt.vodbox.entity.PlanInfor;
import com.znt.vodbox.entity.SubPlanInfor;
import com.znt.vodbox.http.HttpResult;
import com.znt.vodbox.utils.DateUtils;
import com.znt.vodbox.utils.StringUtils;
import com.znt.vodbox.utils.SystemUtils;
import com.znt.vodbox.utils.UrlUtils;

import android.text.TextUtils;

public class JsonParseFactory implements IJsonParse
{
	private String RESULT_INFO = "info";
	private String RESULT_OK = "result";

	private String getInforFromJason(JSONObject json, String key)
	{
		if(json == null || key == null)
			return "";
		if(json.has(key))
		{
			try
			{
				String result = json.getString(key);
				if(result.equals("null"))
					result = "";
				return result;
				//return StringUtils.decodeStr(result);
			} 
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}
	
	@Override
	public HttpResult parseUserInfor(String respose) 
	{
		// TODO Auto-generated method stub
		HttpResult httpResult = new HttpResult();
		try
		{
			JSONObject jsonObject = new JSONObject(respose);
			int result = jsonObject.getInt(RESULT_OK);
			if(result == 0)
			{
				String info = getInforFromJason(jsonObject, "info");
				JSONObject json = new JSONObject(info);
				String id = getInforFromJason(json, "id");
				String phone = getInforFromJason(json, "phone");
				String nickName = getInforFromJason(json, "nickName");
				String terminalCodes = getInforFromJason(json, "terminalCodes");
				String memberType = getInforFromJason(json, "memberType");
				String pcCode = getInforFromJason(json, "activateCode");
				
				UserInfor userInfor = new UserInfor();
				userInfor.setAccount(phone);
				userInfor.setUserId(id);
				userInfor.setUserName(nickName);
				userInfor.setMemType(memberType);//鐢ㄦ埛绫诲瀷 0-鏅�氫細鍛� 1-搴楅暱 2-绠＄悊鍛�
				userInfor.setPcCode(pcCode);

				httpResult.setResult(true, userInfor);
				userInfor.setBindDevices(terminalCodes);
			}
			else
			{
				httpResult.setResult(false, jsonObject.getString(RESULT_INFO));
			}
		} 
		catch (JSONException e)
		{
			httpResult.setResult(false, e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return httpResult;
	}

	@Override
	public HttpResult parseSpeakerList(String response) {
		// TODO Auto-generated method stub
		HttpResult httpResult = new HttpResult();
		try
		{
			JSONObject jsonObject = new JSONObject(response);
			int result = jsonObject.getInt(RESULT_OK);
			if(result == 0)
			{
				List<DeviceInfor> tempList = new ArrayList<DeviceInfor>();
				String info = getInforFromJason(jsonObject, RESULT_INFO);
				JSONObject jsonObj = new JSONObject(info);
				String total = getInforFromJason(jsonObj, "total");
				if(!TextUtils.isEmpty(total))
				{
					int totalInt = Integer.parseInt(total);
					httpResult.setTotal(totalInt);
				}
				String rows = getInforFromJason(jsonObj, "rows");
				JSONArray jsonArray = new JSONArray(rows);
				int count = jsonArray.length();
				for(int i=0;i<count;i++)
				{
					JSONObject json = jsonArray.getJSONObject(i);
					String id = getInforFromJason(json, "id");
					String wifiName = getInforFromJason(json, "wifiName");
					String name = getInforFromJason(json, "name");
					String longitude = getInforFromJason(json, "longitude");
					String latitude = getInforFromJason(json, "latitude");
					String code = getInforFromJason(json, "code");
					String wifiPassword = getInforFromJason(json, "wifiPassword");
					String ip = getInforFromJason(json, "ip");
					String addr = getInforFromJason(json, "address");
					String playingSong = getInforFromJason(json, "playingSong");
					String playingSongType = getInforFromJason(json, "playingSongType");
					String softVersion = getInforFromJason(json, "softVersion");
					String lastBootTime = getInforFromJason(json, "lastBootTime");
					String onlineStatus = getInforFromJason(json, "onlineStatus");//0锛屾帀绾�   1鍦ㄧ嚎
					String volume = getInforFromJason(json, "volume");//0锛屾帀绾�   1鍦ㄧ嚎
					String expiredTime = getInforFromJason(json, "expiredTime");//0锛屾帀绾�   1鍦ㄧ嚎
					String netInfo = getInforFromJason(json, "netInfo");
					String videoWhirl = getInforFromJason(json, "videoWhirl");
					if(TextUtils.isEmpty(videoWhirl))
						videoWhirl = "0";
					DeviceInfor deviceInfor = new DeviceInfor();
					deviceInfor.setVideoWhirl(videoWhirl);
					//鍒ゆ柇璁惧鏄惁鍙敤
					/*String curssid = SystemUtils.getConnectWifiSsid(context);
					if(!TextUtils.isEmpty(curssid) && curssid.equals(wifiName))
						deviceInfor.setAvailable(true);
					//鍒ゆ柇璁惧鏄惁閫変腑
					String localUuid = LocalDataEntity.newInstance(context).getDeviceId();
					if(!TextUtils.isEmpty(localUuid) && localUuid.equals(code))
					{
						deviceInfor.setSelected(true);
					}*/
					deviceInfor.setOnlineStatus(onlineStatus);
					deviceInfor.setLastBootTime(lastBootTime);
					deviceInfor.setSoftVersion(softVersion);
					deviceInfor.setCurPlaySong(playingSong);
					deviceInfor.setId(code);
					deviceInfor.setName(name);
					deviceInfor.setWifiName(wifiName);
					deviceInfor.setWifiPwd(wifiPassword);
					deviceInfor.setCode(id);
					deviceInfor.setIp(ip);
					deviceInfor.setAddr(addr);
					deviceInfor.setLat(latitude);
					deviceInfor.setLon(longitude);
					deviceInfor.setVolume(volume);
					deviceInfor.setEndTime(expiredTime);
					deviceInfor.setNetInfor(netInfo);
					if(TextUtils.isEmpty(playingSongType))
						playingSongType = "0";
					deviceInfor.setPlayingSongType(playingSongType);
					if(deviceInfor.isAvailable())
						tempList.add(0, deviceInfor);
					else
						tempList.add(deviceInfor);
				}
					
				httpResult.setResult(true, tempList);
			}
			else
			{
				httpResult.setResult(false, jsonObject.getString(RESULT_INFO));
			}
		} 
		catch (JSONException e)
		{
			httpResult.setResult(false, e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpResult;
	}

	@Override
	public HttpResult parseSongList(String response) 
	{
		HttpResult httpResult = new HttpResult();
		try
		{
			JSONObject jsonObject = new JSONObject(response);
			int result = jsonObject.getInt(RESULT_OK);
			if(result == 0)
			{
				String info = getInforFromJason(jsonObject, RESULT_INFO);
				JSONObject jsonObject1 = new JSONObject(info);
				String total = getInforFromJason(jsonObject1, "listSize");
				int totalInt = 0;
				if(!TextUtils.isEmpty(total))
					totalInt = Integer.parseInt(total);
				httpResult.setTotal(totalInt);
				
				String playingPos = getInforFromJason(jsonObject1, "playingPos");
				int playingPosInt = 0;
				if(!TextUtils.isEmpty(playingPos))
				{
					playingPosInt = Integer.parseInt(playingPos);
				}
				
				String seekPos = getInforFromJason(jsonObject1, "playSeek");
				long seekPosInt = 0;
				if(!TextUtils.isEmpty(seekPos))
					seekPosInt = Integer.parseInt(seekPos);
				String lastUpdateTime = getInforFromJason(jsonObject1, "lastUpdateTime");
				if(!TextUtils.isEmpty(lastUpdateTime))
				{
					long lastUpdateTimeInt = Long.parseLong(lastUpdateTime);
					seekPosInt = seekPosInt + (System.currentTimeMillis() - lastUpdateTimeInt);
				}
				
				String listInfo = getInforFromJason(jsonObject1, "infoList");
				JSONArray jsonArray = new JSONArray(listInfo);
				int size = jsonArray.length();
				
				List<SongInfor> tempList = new ArrayList<SongInfor>();
				//List<String> urls = new ArrayList<String>();
				if(size > 0)
				{
					/*if(totalInt > 1)
						DBManager.newInstance(context).deleteAllRecord();*/
					
					for(int i=0;i<size;i++)
					{
						JSONObject json = jsonArray.getJSONObject(i);
						String musicAlbum = getInforFromJason(json, "musicAlbum");
						String musicId = getInforFromJason(json, "musicId");
						String id = getInforFromJason(json, "id");
						String musicUrl = getInforFromJason(json, "musicUrl");
						String musicName = getInforFromJason(json, "musicName");
						String musicSing = getInforFromJason(json, "musicSing");
						if(!TextUtils.isEmpty(musicUrl))
							musicUrl = UrlUtils.decodeUrl(musicUrl);
						SongInfor tempInfor = new SongInfor();
						tempInfor.setMediaId(id);
						tempInfor.setResId(musicId);
						tempInfor.setMediaName(musicName);
						tempInfor.setMediaUrl(musicUrl);
						tempInfor.setArtist(musicSing);
						tempInfor.setAlbumName(musicAlbum);
						tempList.add(tempInfor);
						if(playingPosInt == i)
						{
							tempInfor.setIsPlaying(true);
							tempInfor.setCurPlayTime(seekPosInt);
						}
						else
							tempInfor.setIsPlaying(false);
					}
				}
				
				httpResult.setResult(true, tempList);
			}
			else
			{
				httpResult.setResult(false, jsonObject.getString(RESULT_INFO));
			}
		} 
		catch (JSONException e)
		{
			httpResult.setResult(false, e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpResult;
	}

	@Override
	public HttpResult parsePlanList(String response) 
	{
		// TODO Auto-generated method stub
		
		HttpResult httpResult = new HttpResult();
		try
		{
			JSONObject jsonObject = new JSONObject(response);
			int result = jsonObject.getInt(RESULT_OK);
			if(result == 0)
			{
				List<PlanInfor> planList = new ArrayList<PlanInfor>();
				String info = jsonObject.getString(RESULT_INFO);
				JSONObject json = new JSONObject(info);
				String total = getInforFromJason(json, "total");
				if(!TextUtils.isEmpty(total))
					httpResult.setTotal(Integer.parseInt(total));
				String rows = getInforFromJason(json, "rows");
				JSONArray jsonArray = new JSONArray(rows);
				int len = jsonArray.length();
				for(int i=0;i<len;i++)
				{
					PlanInfor planInfor = new PlanInfor();
					List<SubPlanInfor> subPlanList = new ArrayList<SubPlanInfor>();
					JSONObject json1 = (JSONObject) jsonArray.get(i);
					String id = getInforFromJason(json1, "id");
					String pslist = getInforFromJason(json1, "pslist");
					String planFlag = getInforFromJason(json1, "planFlag");//0閸忋劑鍎撮惃鍕╋拷锟�1 閹稿洤鐣鹃惃锟�
					String planType = getInforFromJason(json1, "planType");//1濮ｅ繐銇�  ;0閿涘矁濡弮銉礉閺冦儲婀¤箛鍛淬�忕憰浣革綖閸愶拷
					String endDate = getInforFromJason(json1, "endDate");
					String startDate = getInforFromJason(json1, "startDate");
					String planName = getInforFromJason(json1, "planName");
					String tlist = getInforFromJason(json1, "tlist");
					String publishTime = getInforFromJason(json1, "publishTime");
					//String status = getInforFromJason(json1, "status");
					String terminalList = getInforFromJason(json1, "terminalList");
					
					//String memberId = getInforFromJason(json1, "memberId");
					planInfor.setPlanId(id);
					planInfor.setPlanFlag(planFlag);
					planInfor.setPlanName(planName);
					planInfor.setPublishTime(publishTime);
					planInfor.setTerminalList(terminalList);
					planInfor.setPlanType(planType);
					
					if(!TextUtils.isEmpty(startDate))
					{
						long dateLong = Long.parseLong(startDate);
						planInfor.setStartDate(DateUtils.getStringTimeHead(dateLong));
					}
					if(!TextUtils.isEmpty(endDate))
					{
						long dateLong = Long.parseLong(endDate);
						planInfor.setEndDate(DateUtils.getStringTimeHead(dateLong));
					}
					
					
					JSONArray jsonArray0 = new JSONArray(pslist);
					int len0 = jsonArray0.length();
					for(int j=0;j<len0;j++)
					{
						JSONObject json2 = (JSONObject) jsonArray0.get(j);
						SubPlanInfor subPlanInfor = new SubPlanInfor();
						
						String startTime = getInforFromJason(json2, "startTime");
						String id1 = getInforFromJason(json2, "id");
						/*String cycleType = getInforFromJason(json2, "cycleType");
						String publishId = getInforFromJason(json2, "publishId");
						String musicCategoryList = getInforFromJason(json2, "musicCategoryList");*/
						String endTime = getInforFromJason(json2, "endTime");
						if(!TextUtils.isEmpty(startTime) && startTime.contains(":"))
						{
							subPlanInfor.setStartTime(StringUtils.getPlanTimeShow(startTime));
						}
						if(!TextUtils.isEmpty(endTime) && endTime.contains(":"))
						{
							subPlanInfor.setEndTime(StringUtils.getPlanTimeShow(endTime));
						}
						
						String loopAddNum = getInforFromJason(json2, "loopAddNum");
						String cycleType = getInforFromJason(json2, "cycleType");
						String loopMusicName = getInforFromJason(json2, "loopMusicName");
						String loopMusicInfoId = getInforFromJason(json2, "loopMusicInfoId");
						subPlanInfor.setLoopAddNum(loopAddNum);
						subPlanInfor.setLoopMusicInfoId(loopMusicInfoId);
						subPlanInfor.setLoopMusicName(loopMusicName);
						subPlanInfor.setCycleType(cycleType);
						
						subPlanInfor.setId(id1);
						List<MusicAlbumInfor> albumList = new ArrayList<MusicAlbumInfor>();
						
						String mclist = getInforFromJason(json2, "mclist");
						JSONArray jsonArray1 = new JSONArray(mclist);
						int size1 = jsonArray1.length();
						for(int k=0;k<size1;k++)
						{
							MusicAlbumInfor musicAlbumInfor = new MusicAlbumInfor();
							JSONObject json3 = jsonArray1.getJSONObject(k);
							String id2 = getInforFromJason(json3, "id");
							//String status1 = getInforFromJason(json3, "status");
							//String remark = getInforFromJason(json3, "remark");
							String imageUrl = getInforFromJason(json3, "imageUrl");
							//String lastUpdateTime = getInforFromJason(json3, "lastUpdateTime");
							String description = getInforFromJason(json3, "description");
							String name = getInforFromJason(json3, "name");
							//String ordinal = getInforFromJason(json3, "ordinal");
							//String memberId1 = getInforFromJason(json3, "memberId");
							//String type = getInforFromJason(json3, "type");
							//String sysUserId = getInforFromJason(json3, "sysUserId");
							//String addTime = getInforFromJason(json3, "addTime");
							
							musicAlbumInfor.setAlbumId(id2);
							musicAlbumInfor.setAlbumName(name);
							musicAlbumInfor.setDescription(description);
							musicAlbumInfor.setCover(imageUrl);
							
							albumList.add(musicAlbumInfor);
							
						}
						subPlanInfor.setAlbumList(albumList);
						subPlanList.add(subPlanInfor);
						
					}
					planInfor.setSubPlanList(subPlanList);
					if(planFlag.equals("1"))//閹稿洤鐣鹃惃鍕暗闁撅拷
					{
						List<DeviceInfor> deviceList = new ArrayList<DeviceInfor>();
						JSONArray jsonArray1 = new JSONArray(tlist);
						int len1 = jsonArray1.length();
						for(int j=0;j<len1;j++)
						{
							DeviceInfor deviceInfor = new DeviceInfor();
							JSONObject json2 = (JSONObject) jsonArray1.get(j);
							//String lastMusicUpdate = getInforFromJason(json2, "lastMusicUpdate");
							String wifiName = getInforFromJason(json2, "wifiName");
							String wifiPassword = getInforFromJason(json2, "wifiPassword");
							//String wifiMac = getInforFromJason(json2, "wifiMac");
							String playingSong = getInforFromJason(json2, "playingSong");
							String softVersion = getInforFromJason(json2, "softVersion");
							//String vodFlag = getInforFromJason(json2, "vodFlag");
							String code = getInforFromJason(json2, "code");
							String lastBootTime = getInforFromJason(json2, "lastBootTime");
							String onlineStatus = getInforFromJason(json2, "onlineStatus");
							//String geohash = getInforFromJason(json2, "geohash");
							String ip = getInforFromJason(json2, "ip");
							String id2 = getInforFromJason(json2, "id");
							//String publishPlanId = getInforFromJason(json2, "publishPlanId");
							String distance = getInforFromJason(json2, "distance");
							String address = getInforFromJason(json2, "address");
							String name = getInforFromJason(json2, "name");
							//String hardVersion = getInforFromJason(json2, "hardVersion");
							String longitude = getInforFromJason(json2, "longitude");
							String latitude = getInforFromJason(json2, "latitude");
							//String playStatus = getInforFromJason(json2, "playStatus");
							//String addTime = getInforFromJason(json2, "longitude");
							deviceInfor.setName(name);
							deviceInfor.setAddr(address);
							deviceInfor.setLat(latitude);
							deviceInfor.setLon(longitude);
							deviceInfor.setIp(ip);
							deviceInfor.setLastBootTime(lastBootTime);
							deviceInfor.setOnlineStatus(onlineStatus);
							deviceInfor.setCurPlaySong(playingSong);
							deviceInfor.setVersion(softVersion);
							deviceInfor.setCode(id2);
							deviceInfor.setWifiName(wifiName);
							deviceInfor.setWifiPwd(wifiPassword);
							deviceInfor.setId(code);
							deviceList.add(deviceInfor);
						}
						planInfor.setDeviceList(deviceList);
					}
					
					planList.add(planInfor);
				}
				httpResult.setResult(true, planList);
			}
			else
			{
				httpResult.setResult(false, jsonObject.getString(RESULT_INFO));
			}
		} 
		catch (JSONException e)
		{
			httpResult.setResult(false, e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpResult;
	}

	@Override
	public HttpResult parseBoxPlanList(String response) 
	{
		HttpResult httpResult = new HttpResult();
		try
		{
			JSONObject jsonObject = new JSONObject(response);
			int result = jsonObject.getInt(RESULT_OK);
			if(result == 0)
			{
				List<PlanInfor> planList = new ArrayList<PlanInfor>();
				String info = jsonObject.getString(RESULT_INFO);
				if(!TextUtils.isEmpty(info))
				{
					
					JSONObject json = new JSONObject(info);
				
					PlanInfor planInfor = new PlanInfor();
					List<SubPlanInfor> subPlanList = new ArrayList<SubPlanInfor>();
					String id = getInforFromJason(json, "id");
					String pslist = getInforFromJason(json, "pslist");
					String planFlag = getInforFromJason(json, "planFlag");//0鍏ㄩ儴鐨勩��1 鎸囧畾鐨�
					String planType = getInforFromJason(json, "planType");//1姣忓ぉ  ;0锛岃妭鏃ワ紝鏃ユ湡蹇呴』瑕佸～鍐�
					String endDate = getInforFromJason(json, "endDate");
					String startDate = getInforFromJason(json, "startDate");
					String planName = getInforFromJason(json, "planName");
					String tlist = getInforFromJason(json, "tlist");
					String publishTime = getInforFromJason(json, "publishTime");
					//String status = getInforFromJason(json1, "status");
					String terminalList = getInforFromJason(json, "terminalList");
					
					//String memberId = getInforFromJason(json1, "memberId");
					planInfor.setPlanId(id);
					planInfor.setPlanFlag(planFlag);
					planInfor.setPlanName(planName);
					planInfor.setPublishTime(publishTime);
					planInfor.setTerminalList(terminalList);
					planInfor.setPlanType(planType);
					
					if(!TextUtils.isEmpty(startDate))
					{
						long dateLong = Long.parseLong(startDate);
						planInfor.setStartDate(DateUtils.getStringTimeHead(dateLong));
					}
					if(!TextUtils.isEmpty(endDate))
					{
						long dateLong = Long.parseLong(endDate);
						planInfor.setEndDate(DateUtils.getStringTimeHead(dateLong));
					}
					
					
					JSONArray jsonArray0 = new JSONArray(pslist);
					int len0 = jsonArray0.length();
					for(int j=0;j<len0;j++)
					{
						JSONObject json2 = (JSONObject) jsonArray0.get(j);
						SubPlanInfor subPlanInfor = new SubPlanInfor();
						
						String startTime = getInforFromJason(json2, "startTime");
						String id1 = getInforFromJason(json2, "id");
						/*String cycleType = getInforFromJason(json2, "cycleType");
						String publishId = getInforFromJason(json2, "publishId");
						String musicCategoryList = getInforFromJason(json2, "musicCategoryList");*/
						String endTime = getInforFromJason(json2, "endTime");
						if(!TextUtils.isEmpty(startTime) && startTime.contains(":"))
						{
							subPlanInfor.setStartTime(StringUtils.getPlanTimeShow(startTime));
						}
						if(!TextUtils.isEmpty(endTime) && endTime.contains(":"))
						{
							subPlanInfor.setEndTime(StringUtils.getPlanTimeShow(endTime));
						}
						
						String loopAddNum = getInforFromJason(json2, "loopAddNum");
						String cycleType = getInforFromJason(json2, "cycleType");
						String loopMusicName = getInforFromJason(json2, "loopMusicName");
						String loopMusicInfoId = getInforFromJason(json2, "loopMusicInfoId");
						subPlanInfor.setLoopAddNum(loopAddNum);
						subPlanInfor.setLoopMusicInfoId(loopMusicInfoId);
						subPlanInfor.setLoopMusicName(loopMusicName);
						subPlanInfor.setCycleType(cycleType);
						
						subPlanInfor.setId(id1);
						List<MusicAlbumInfor> albumList = new ArrayList<MusicAlbumInfor>();
						
						String mclist = getInforFromJason(json2, "mclist");
						JSONArray jsonArray1 = new JSONArray(mclist);
						int size1 = jsonArray1.length();
						for(int k=0;k<size1;k++)
						{
							MusicAlbumInfor musicAlbumInfor = new MusicAlbumInfor();
							JSONObject json3 = jsonArray1.getJSONObject(k);
							String id2 = getInforFromJason(json3, "id");
							//String status1 = getInforFromJason(json3, "status");
							//String remark = getInforFromJason(json3, "remark");
							String imageUrl = getInforFromJason(json3, "imageUrl");
							//String lastUpdateTime = getInforFromJason(json3, "lastUpdateTime");
							String description = getInforFromJason(json3, "description");
							String name = getInforFromJason(json3, "name");
							//String ordinal = getInforFromJason(json3, "ordinal");
							//String memberId1 = getInforFromJason(json3, "memberId");
							//String type = getInforFromJason(json3, "type");
							//String sysUserId = getInforFromJason(json3, "sysUserId");
							//String addTime = getInforFromJason(json3, "addTime");
							
							musicAlbumInfor.setAlbumId(id2);
							musicAlbumInfor.setAlbumName(name);
							musicAlbumInfor.setDescription(description);
							musicAlbumInfor.setCover(imageUrl);
							
							albumList.add(musicAlbumInfor);
							
						}
						subPlanInfor.setAlbumList(albumList);
						subPlanList.add(subPlanInfor);
						
					}
					planInfor.setSubPlanList(subPlanList);
					if(planFlag.equals("1") && !TextUtils.isEmpty(tlist))//鎸囧畾鐨勫簵閾�
					{
						List<DeviceInfor> deviceList = new ArrayList<DeviceInfor>();
						JSONArray jsonArray1 = new JSONArray(tlist);
						int len1 = jsonArray1.length();
						for(int j=0;j<len1;j++)
						{
							DeviceInfor deviceInfor = new DeviceInfor();
							JSONObject json2 = (JSONObject) jsonArray1.get(j);
							//String lastMusicUpdate = getInforFromJason(json2, "lastMusicUpdate");
							String wifiName = getInforFromJason(json2, "wifiName");
							String wifiPassword = getInforFromJason(json2, "wifiPassword");
							//String wifiMac = getInforFromJason(json2, "wifiMac");
							String playingSong = getInforFromJason(json2, "playingSong");
							String softVersion = getInforFromJason(json2, "softVersion");
							//String vodFlag = getInforFromJason(json2, "vodFlag");
							String code = getInforFromJason(json2, "code");
							String lastBootTime = getInforFromJason(json2, "lastBootTime");
							String onlineStatus = getInforFromJason(json2, "onlineStatus");
							//String geohash = getInforFromJason(json2, "geohash");
							String ip = getInforFromJason(json2, "ip");
							String id2 = getInforFromJason(json2, "id");
							//String publishPlanId = getInforFromJason(json2, "publishPlanId");
							String distance = getInforFromJason(json2, "distance");
							String address = getInforFromJason(json2, "address");
							String name = getInforFromJason(json2, "name");
							//String hardVersion = getInforFromJason(json2, "hardVersion");
							String longitude = getInforFromJason(json2, "longitude");
							String latitude = getInforFromJason(json2, "latitude");
							//String playStatus = getInforFromJason(json2, "playStatus");
							//String addTime = getInforFromJason(json2, "longitude");
							deviceInfor.setName(name);
							deviceInfor.setAddr(address);
							deviceInfor.setLat(latitude);
							deviceInfor.setLon(longitude);
							deviceInfor.setIp(ip);
							deviceInfor.setLastBootTime(lastBootTime);
							deviceInfor.setOnlineStatus(onlineStatus);
							deviceInfor.setCurPlaySong(playingSong);
							deviceInfor.setVersion(softVersion);
							deviceInfor.setCode(id2);
							deviceInfor.setWifiName(wifiName);
							deviceInfor.setWifiPwd(wifiPassword);
							deviceInfor.setId(code);
							deviceList.add(deviceInfor);
						}
						planInfor.setDeviceList(deviceList);
					}
					
					planList.add(planInfor);
				}
				httpResult.setResult(true, planList);
			}
			else
			{
				httpResult.setResult(false, jsonObject.getString(RESULT_INFO));
			}
		} 
		catch (JSONException e)
		{
			httpResult.setResult(false, e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpResult;
	}
	
}
