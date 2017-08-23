/*  
* @Project: ZNTVodBox 
* @User: Administrator 
* @Description: 家庭音乐
* @Author： yan.yu
* @Company：http://www.zhunit.com/
* @Date 2016-8-30 下午1:26:11 
* @Version V1.1   
*/ 

package com.znt.vodbox.activity; 

import java.util.ArrayList;
import java.util.List;

import com.znt.diange.mina.cmd.DeviceInfor;
import com.znt.vodbox.R;
import com.znt.vodbox.adapter.Action;
import com.znt.vodbox.adapter.DeviceInforAdapter;
import com.znt.vodbox.adapter.ShopAdapter;
import com.znt.vodbox.entity.Constant;
import com.znt.vodbox.factory.HttpFactory;
import com.znt.vodbox.http.HttpMsg;
import com.znt.vodbox.http.HttpRequestID;
import com.znt.vodbox.http.HttpResult;
import com.znt.vodbox.mvp.p.HttpGetSpeakerPresenter;
import com.znt.vodbox.mvp.v.IHttpRequestSpeakerView;
import com.znt.vodbox.utils.SystemUtils;
import com.znt.vodbox.utils.ViewUtils;
import com.znt.vodbox.view.ISFooterView;
import com.znt.vodbox.view.ISHeaderView;
import com.znt.vodbox.view.RefreshRecyclerView;
import com.znt.vodbox.view.SearchShopView;
import com.znt.vodbox.view.listview.LJListView;
import com.znt.vodbox.view.listview.LJListView.IXListViewListener;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

/** 
 * @ClassName: SearchShopActivity 
 * @Description: TODO
 * @author yan.yu 
 * @date 2016-8-30 下午1:26:11  
 */
public class SearchShopActivity extends BaseActivity implements IHttpRequestSpeakerView
{
	private SearchShopView searchView = null;
	private ISFooterView footer = null;
	
	private RefreshRecyclerView mRecyclerView;
    CoordinatorLayout clContent;
    private HttpGetSpeakerPresenter httpGetSpeakerPresenter = null;
    private DeviceInforAdapter deviceInforAdapter;

    private List<DeviceInfor> deviceList = new ArrayList<DeviceInfor>();
    private boolean pendingIntroAnimation;
    private int page = 1;
	
	/**
	*callbacks
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_all_shop);
		
		searchView = new SearchShopView(getApplicationContext());
        clContent = (CoordinatorLayout)findViewById(R.id.content);
       
		setCenterString("店铺搜索");
		searchView.setVisibility(View.VISIBLE);
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.rvFeed);
        footer = new ISFooterView(getActivity());
        footer.showLoadReady();
        footer.setVisibility(View.GONE);
        footer.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				getSpeakersByName();
			}
		});
        
        //initCollapsingToolbarLayout();
        
        //setupFeed();
        deviceInforAdapter = new DeviceInforAdapter(getActivity(), deviceList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        mRecyclerView.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        mRecyclerView.setAdapter(deviceInforAdapter);
        
        deviceInforAdapter.addHeaderView(searchView);
        deviceInforAdapter.addFootView(footer);
        
        mRecyclerView.setRefreshAction(new Action() 
        {
            @Override
            public void onAction() 
            {
            	page = 1;
            	getSpeakersByName();
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
                	getSpeakersByName();
                }
            }
        });
        
        
        if (savedInstanceState == null) 
        {
            pendingIntroAnimation = true;
        } 
        
        httpGetSpeakerPresenter = new HttpGetSpeakerPresenter(getActivity(), this);
		
        searchView.getEditText().setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) 
                {
                	getSpeakersByName();
                }
                return false;
            }
        });
		
        searchView.getEditText().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchView.getEditText().setFocusable(true);
				searchView.getEditText().setFocusableInTouchMode(true);
				searchView.getEditText().requestFocus();
				searchView.getEditText().findFocus();
				
				SystemUtils.showInputView(v);
			}
		});
		
        searchView.getTextView().setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				getSpeakersByName();
			}
		});
		
	}
	
	private void getSpeakersByName()
	{
		String name = searchView.getEditText().getText().toString();
		if(!TextUtils.isEmpty(name))
		{
			httpGetSpeakerPresenter.getBindedSpeakersByName(page, name);
			hideInput();
		}
		else
		{
			mRecyclerView.dismissSwipeRefresh();
			showToast("请输入搜索内容");
		}
	}
	
	private void hideInput()
	{
		searchView.getEditText().setFocusable(false);
		SystemUtils.hideInputView(getActivity());
	}

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
 
