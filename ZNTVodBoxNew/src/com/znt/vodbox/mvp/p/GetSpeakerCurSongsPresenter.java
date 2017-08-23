package com.znt.vodbox.mvp.p;

import java.util.HashMap;
import java.util.Map;

import com.znt.vodbox.R;
import com.znt.vodbox.entity.LocalDataEntity;
import com.znt.vodbox.http.HttpRequestID;
import com.znt.vodbox.mvp.m.GetSpeakerCurSongsModel;
import com.znt.vodbox.mvp.v.IGetCurPlanSongsView;
import com.znt.vodbox.utils.SystemUtils;

import android.content.Context;
import android.text.TextUtils;

public class GetSpeakerCurSongsPresenter 
{
	private Context context = null;
	private IGetCurPlanSongsView iGetCurPlanSongsView = null;
	private GetSpeakerCurSongsModel getSpeakerCurSongsModel = null;
	public GetSpeakerCurSongsPresenter(Context context, IGetCurPlanSongsView iGetCurPlanSongsView)
	{
		this.context = context;
		this.iGetCurPlanSongsView = iGetCurPlanSongsView;
		getSpeakerCurSongsModel = new GetSpeakerCurSongsModel(iGetCurPlanSongsView);
	}
	
	public void getCurSpeakersSongs(int pageNo)
	{
		if(!SystemUtils.isNetConnected(context)
				&& !SystemUtils.is3gConnected(context))
		{
			iGetCurPlanSongsView.requestFailed(HttpRequestID.GET_BIND_SPEAKERS, context.getString(R.string.network_error));
			return;
		}
		
		String uid = LocalDataEntity.newInstance(context).getUserInfor().getUserId();
		if(TextUtils.isEmpty(uid))
			return;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", iGetCurPlanSongsView.getTerminalId());
		params.put("pageSize", "25");
		params.put("pageNo", pageNo + "");
		
		
		getSpeakerCurSongsModel.getCurPlanMusics(params);
	}
}
