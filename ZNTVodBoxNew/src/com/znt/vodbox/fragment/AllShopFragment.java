/*  
* @Project: ZNTVodBox 
* @User: Administrator 
* @Description: 瀹跺涵闊充箰
* @Author锛� yan.yu
* @Company锛歨ttp://www.zhunit.com/
* @Date 2017-6-6 涓嬪崍11:19:22 
* @Version V1.1   
*/ 

package com.znt.vodbox.fragment; 

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.znt.diange.mina.cmd.DeviceInfor;
import com.znt.vodbox.R;
import com.znt.vodbox.adapter.Action;
import com.znt.vodbox.adapter.DeviceInforAdapter;
import com.znt.vodbox.entity.Constant;
import com.znt.vodbox.http.HttpRequestID;
import com.znt.vodbox.mvp.p.HttpGetSpeakerPresenter;
import com.znt.vodbox.mvp.v.IHttpRequestSpeakerView;
import com.znt.vodbox.view.ISFooterView;
import com.znt.vodbox.view.ISHeaderView;
import com.znt.vodbox.view.RefreshRecyclerView;
import com.znt.vodbox.view.ShopListHeaderView;

/** 
 * @ClassName: AllShopFragment 
 * @Description: TODO
 * @author yan.yu 
 * @date 2017-6-6 涓嬪崍11:19:22  
 */
public class AllShopFragment extends BaseFragment implements IHttpRequestSpeakerView
{
	private View parentView = null;
	
	public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;

    private ISFooterView footer = null;
    private ShopListHeaderView header = null;
    private RefreshRecyclerView mRecyclerView;
    CoordinatorLayout clContent;
    private HttpGetSpeakerPresenter httpGetSpeakerPresenter = null;
    private DeviceInforAdapter deviceInforAdapter;

    private List<DeviceInfor> deviceList = new ArrayList<DeviceInfor>();
    private boolean pendingIntroAnimation;
    private int page = 1;
    private boolean isInit = true;
	
	public AllShopFragment()
	{
		
	}
	public static ShopFragment getInstance()
	{
		return new ShopFragment();
	}
	
	/**
	*callbacks
	*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		if(parentView == null)
		{
			parentView = getContentView(R.layout.fragment_all_shop);
			//planView = (PlanView)parentView.findViewById(R.id.pv_cur_plan);
			
	        clContent = (CoordinatorLayout)parentView.findViewById(R.id.content);
	        
	        mRecyclerView = (RefreshRecyclerView) parentView.findViewById(R.id.rvFeed);
	        header = new ShopListHeaderView(getActivity());
	        footer = new ISFooterView(getActivity());
	        footer.showLoadReady();
	        footer.setOnClickListener(new OnClickListener() 
	        {
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					httpGetSpeakerPresenter.getBindedSpeakers(page);
				}
			});
	        
	        //setupFeed();
	        deviceInforAdapter = new DeviceInforAdapter(getActivity(), deviceList);
	        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
	        
	        mRecyclerView.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
	        mRecyclerView.setAdapter(deviceInforAdapter);
	        
	        deviceInforAdapter.addHeaderView(header);
	        deviceInforAdapter.addFootView(footer);
	        
	        mRecyclerView.setRefreshAction(new Action() 
	        {
	            @Override
	            public void onAction() 
	            {
	            	page = 1;
	            	httpGetSpeakerPresenter.getBindedSpeakers(page);
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
	                	httpGetSpeakerPresenter.getBindedSpeakers(page);
	                }
	            }
	        });
	        
	        
	        if (savedInstanceState == null) 
	        {
	            pendingIntroAnimation = true;
	        } 
	        
	        httpGetSpeakerPresenter = new HttpGetSpeakerPresenter(getActivity(), this);
	        httpGetSpeakerPresenter.getBindedSpeakers(page);
			
		}
		else
		{
			ViewGroup parent = (ViewGroup) parentView.getParent();
			if(parent != null)
				parent.removeView(parentView);
		}
		
		// TODO Auto-generated method stub
		return parentView;
	}
	
	/**
	*callbacks
	*/
	@Override
	public void onResume() 
	{
		// TODO Auto-generated method stub
		
		if(Constant.isPlanUpdated)
		{
			//httpFactory.getCurPlan();
			Constant.isPlanUpdated = false;
		}
		/*if(Constant.isShopUpdated && !isFirstEnter)
		{
			reloadShops();
			Constant.isShopUpdated = false;
		}
		isFirstEnter = false;*/
		
		super.onResume();
	}
	
	@Override
	public void requestRunning(int requestId) 
	{
		// TODO Auto-generated method stub
		if(requestId == HttpRequestID.GET_BIND_SPEAKERS)
		{
			if(isInit)
			{
				//isLoadingView.showLoadingView(true);
			}
			
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
		if(isInit)//锟斤拷一锟轿硷拷锟斤拷展示锟叫硷拷募锟斤拷锟斤拷锟酵�
		{
			isInit = false;
			//isLoadingView.showLoadError(getResources().getString(R.string.reload_hint_when_request_error));
		}
		else
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
                if(isInit)
        		{
        			isInit = false;
        			//isLoadingView.showLoadingView(false);
        		}
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
	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		
	}

}
 