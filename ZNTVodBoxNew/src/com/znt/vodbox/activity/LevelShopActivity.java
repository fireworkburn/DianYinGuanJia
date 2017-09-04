package com.znt.vodbox.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.znt.diange.mina.entity.UserInfor;
import com.znt.vodbox.R;
import com.znt.vodbox.adapter.UserAdapter;
import com.znt.vodbox.factory.HttpFactory;
import com.znt.vodbox.http.HttpMsg;
import com.znt.vodbox.http.HttpResult;
import com.znt.vodbox.mvp.p.HttpLoginPresenter;
import com.znt.vodbox.utils.ViewUtils;
import com.znt.vodbox.view.SplashView;
import com.znt.vodbox.view.listview.LJListView;
import com.znt.vodbox.view.listview.LJListView.IXListViewListener;

public class LevelShopActivity extends BaseActivity implements IXListViewListener, OnItemClickListener
{
	private LJListView listView = null;
	private HttpFactory httpFactory = null;
	private List<UserInfor> userList = new ArrayList<UserInfor>();
	
	private UserAdapter userAdapter = null;
	private boolean isRunning = false;
	private boolean isLoadFinish = false;
	private boolean isLoaded = false;
	private boolean isPrepared = false;
	private int total = 0;
	private int pageNum = 1;
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == HttpMsg.GET_SECOND_LEVELS_START)
			{
				isRunning = true;
			}
			else if(msg.what == HttpMsg.GET_SECOND_LEVELS_SUCCESS)
			{
				HttpResult httpResult = (HttpResult)msg.obj;
				total = httpResult.getTotal();
				
				List<UserInfor> tempList = (List<UserInfor>)httpResult.getReuslt();
				if(pageNum == 1)
					userList.clear();
				if(tempList.size() > 0)
					userList.addAll(tempList);
				if(userList.size() >= total)
				{
					listView.showFootView(false);
					isLoadFinish = true;
				}
				else
				{
					listView.showFootView(true);
					isLoadFinish = false;
				}
				if(userList.size() == 0)
				{
					showNoDataView("还没有创建分区");
					listView.showFootView(false);
				}
				userAdapter.notifyDataSetChanged();
				hideHintView();
				pageNum ++;
				onLoad(0);
				isRunning = false;
				isLoaded = true;
			}
			else if(msg.what == HttpMsg.GET_SECOND_LEVELS_FAIL)
			{
				isRunning = false;
				onLoad(0);
				//showHint("鑾峰彇鏁版嵁澶辫触");
				showToast("获取分区店铺失败");
			}
		};
	};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    	super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.fragment_level);
        showTopView(true);
        setCenterString("我们的分区");
        setRightTopIcon(R.drawable.icon_add_off);
        
        listView = (LJListView)findViewById(R.id.ptrl_level_devices);
		listView.getListView().setDivider(getResources().getDrawable(R.color.transparent));
		listView.getListView().setDividerHeight(1);
		listView.setPullLoadEnable(true,"鍏�5鏉℃暟鎹�"); 
		listView.setPullRefreshEnable(true);
		listView.setIsAnimation(true); 
		listView.setXListViewListener(this);
		listView.showFootView(false);
		listView.setRefreshTime();
		listView.setOnItemClickListener(this);
		userAdapter = new UserAdapter(getActivity(), userList);
		listView.setAdapter(userAdapter);
		
		httpFactory = new HttpFactory(getActivity(), handler);
		
		loadData();
		
		getRightView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showToast("添加分区开发中");
			}
		});
    }
	
	public void loadData()
	{
		if(!isLoaded)
			listView.onFresh();
	}
	private void getSecondLevels()
	{
		if(!isRunning)
			httpFactory.getSecondLevels(pageNum);
	}
	
	private void onLoad(int updateCount) 
	{
		listView.setCount(updateCount);
		listView.stopLoadMore();
		listView.stopRefresh();
		listView.setRefreshTime();
	}
	
	/**
	*callbacks
	*/
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3)
	{
		// TODO Auto-generated method stub
		if(pos > 0)
			pos = pos - 1;
		UserInfor tempInfor = userList.get(pos);
		Bundle bundle = new Bundle();
		bundle.putSerializable("UserInfor", tempInfor);
		ViewUtils.startActivity(getActivity(), ShopActivity.class, bundle);
	}

	/**
	*callbacks
	*/
	@Override
	public void onRefresh() 
	{
		// TODO Auto-generated method stub
		if(isRunning)
			return;
		if(httpFactory != null)
		{
			pageNum = 1;
			getSecondLevels();
		}
	}

	/**
	*callbacks
	*/
	@Override
	public void onLoadMore() 
	{
		// TODO Auto-generated method stub
		if(isRunning)
			return;
		if(httpFactory != null)
		{
			getSecondLevels();
		}
	}
}
