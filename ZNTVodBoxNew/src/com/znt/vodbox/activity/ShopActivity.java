/*  
* @Project: ZNTVodBox 
* @User: Administrator 
* @Description: 家庭音乐
* @Author： yan.yu
* @Company：http://www.zhunit.com/
* @Date 2017-6-6 下午10:56:45 
* @Version V1.1   
*/ 

package com.znt.vodbox.activity; 

import java.util.ArrayList;
import java.util.List;

import com.znt.diange.mina.cmd.DeviceInfor;
import com.znt.diange.mina.entity.UserInfor;
import com.znt.vodbox.R;
import com.znt.vodbox.adapter.Action;
import com.znt.vodbox.adapter.DeviceInforAdapter;
import com.znt.vodbox.http.HttpRequestID;
import com.znt.vodbox.mvp.p.HttpGetSpeakerPresenter;
import com.znt.vodbox.mvp.v.IHttpRequestSpeakerView;
import com.znt.vodbox.utils.ViewUtils;
import com.znt.vodbox.view.ISFooterView;
import com.znt.vodbox.view.ISHeaderView;
import com.znt.vodbox.view.RefreshRecyclerView;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

/** 
 * @ClassName: ShopActivity 
 * @Description: TODO
 * @author yan.yu 
 * @date 2017-6-6 下午10:56:45  
 */
public class ShopActivity extends BaseActivity implements IHttpRequestSpeakerView
{
	public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;

    private ISFooterView footer = null;
    private ISHeaderView header = null;
    private RefreshRecyclerView mRecyclerView;
    CoordinatorLayout clContent;
    private HttpGetSpeakerPresenter httpGetSpeakerPresenter = null;
    private DeviceInforAdapter deviceInforAdapter;

    private List<DeviceInfor> deviceList = new ArrayList<DeviceInfor>();
    private boolean pendingIntroAnimation;
    private int page = 1;
	private String areaName = "";
	private String userId = null;
	private String userName = null;
	/**
	*callbacks
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_all_shop);
		clContent = (CoordinatorLayout)findViewById(R.id.content);
        
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.rvFeed);
        header = new ISHeaderView(getActivity());
        footer = new ISFooterView(getActivity());
        footer.showLoadReady();
        
        UserInfor tempInfor = (UserInfor) getIntent().getSerializableExtra("UserInfor");
		if(tempInfor != null)
		{
			userName = tempInfor.getUserName();
			userId = tempInfor.getUserId();
		}
		areaName = tempInfor.getUserName();
		
		//setBackViewIcon(R.drawable.icon_search_shop);
		setCenterString(areaName);
		showTopView(true);
		if(TextUtils.isEmpty(userId))
			setRightText("播放计划");
		else
			setRightText("区域计划");
		setRightTopIcon(R.drawable.icon_plan_item);
        
        footer.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				getSecondLevels();
			}
		});
        
        //setupFeed();
        deviceInforAdapter = new DeviceInforAdapter(getActivity(), deviceList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        mRecyclerView.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        mRecyclerView.setAdapter(deviceInforAdapter);
        
        //deviceInforAdapter.addHeaderView(header);
        deviceInforAdapter.addFootView(footer);
        
        mRecyclerView.setRefreshAction(new Action() 
        {
            @Override
            public void onAction() 
            {
            	page = 1;
            	getSecondLevels();
            }
        });
        
        mRecyclerView.getRecyclerView().setOnScrollListener(new RecyclerView.OnScrollListener() 
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) 
            {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition == totalItemCount - 1
                        && visibleItemCount > 0) 
                {
                	getSecondLevels();
                }
            }
        });
        
        
        if (savedInstanceState == null) 
        {
            pendingIntroAnimation = true;
        } 
        
        httpGetSpeakerPresenter = new HttpGetSpeakerPresenter(getActivity(), this);
        getSecondLevels();
		
		getRightView().setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				if(deviceList.size() == 0)
				{
					showToast("该区域下没有店铺");
					return;
				}
				Bundle bundle = new Bundle();
				//bundle.putSerializable("PlanInfor", planInfor);
				bundle.putString("USER_ID", userId);
				bundle.putString("USER_NAME", userName);
				ViewUtils.startActivity(getActivity(), PlanListActivity.class, bundle);
			}
		});
	}
	
	/**
	*callbacks
	*/
	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
	}
	private void getSecondLevels()
	{
		httpGetSpeakerPresenter.getBindedSpeakers(userId, page);
	}
	

	/**//**
	*callbacks
	*//*
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) 
	{
		// TODO Auto-generated method stub
		if(pos > 0)
			pos = pos - 1;
		DeviceInfor deviceInfor = deviceList.get(pos);
		Bundle bundle = new Bundle();
		bundle.putSerializable("DeviceInfor", deviceInfor);
		ViewUtils.startActivity(getActivity(), ShopDetailActivity.class, bundle);
	}*/
	
	@Override
	public void requestRunning(int requestId) 
	{
		// TODO Auto-generated method stub
		if(requestId == HttpRequestID.GET_BIND_SPEAKERS)
		{
			if(page != 1)
				footer.showLoadStartiew();
			else
				footer.showFooter(false);
		}
	}

	@Override
	public void requestFailed(int requestId, String error) 
	{
		// TODO Auto-generated method stub
		//showLoadDataFailView();
		if(page == 1)
			mRecyclerView.dismissSwipeRefresh();
		Snackbar.make(clContent, getResources().getString(R.string.load_data_fail) + ":" + error, Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void requestSuccess(int requestId, List<DeviceInfor> tempList) 
	{
		// TODO Auto-generated method stub
		if(requestId == HttpRequestID.GET_BIND_SPEAKERS)
		{
			int oldSize = deviceList.size();
			if(tempList != null)
				deviceList.addAll(tempList);
			if(page == 1)
			{
				deviceList.clear();
				
				deviceInforAdapter.clear();
				deviceInforAdapter.addAll(tempList);
                mRecyclerView.dismissSwipeRefresh();
                mRecyclerView.getRecyclerView().scrollToPosition(0);
                if(tempList.size() == 0)
                	showNoDataView("该区域下没有店铺");
			}
			else
			{
				if(tempList.size() < httpGetSpeakerPresenter.MAX_PAGE_SIZE)
					footer.setVisibility(View.INVISIBLE);
				else
					footer.setVisibility(View.VISIBLE);
					deviceInforAdapter.addAll(tempList);
			}
			
	        page++;
			deviceInforAdapter.notifyItemRangeChanged(oldSize, deviceList.size());
		}
		
	}
	
}
 
