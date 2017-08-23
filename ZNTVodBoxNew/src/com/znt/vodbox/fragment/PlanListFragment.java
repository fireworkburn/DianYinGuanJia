/*  
* @Project: ZNTVodBox 
* @User: Administrator 
* @Description: 瀹跺涵闊充箰
* @Author锛? yan.yu
* @Company锛歨ttp://www.zhunit.com/
* @Date 2016-6-1 涓嬪崍3:01:27 
* @Version V1.1   
*/ 

package com.znt.vodbox.fragment; 

import java.util.ArrayList;
import java.util.List;

import com.znt.diange.mina.entity.UserInfor;
import com.znt.vodbox.R;
import com.znt.vodbox.activity.PlanDetailActivity;
import com.znt.vodbox.adapter.Action;
import com.znt.vodbox.adapter.PlanListAdapter;
import com.znt.vodbox.entity.LocalDataEntity;
import com.znt.vodbox.entity.PlanInfor;
import com.znt.vodbox.entity.SubPlanInfor;
import com.znt.vodbox.factory.HttpFactory;
import com.znt.vodbox.http.HttpMsg;
import com.znt.vodbox.http.HttpRequestID;
import com.znt.vodbox.http.HttpResult;
import com.znt.vodbox.mvp.p.GetPlanListPresenter;
import com.znt.vodbox.mvp.v.IGetPlanListView;
import com.znt.vodbox.utils.ViewUtils;
import com.znt.vodbox.view.ISFooterView;
import com.znt.vodbox.view.RefreshRecyclerView;
import com.znt.vodbox.view.HintView.OnHintListener;
import com.znt.vodbox.view.listview.LJListView;
import com.znt.vodbox.view.listview.LJListView.IXListViewListener;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * @ClassName: ShopFragment 
 * @Description: TODO
 * @author yan.yu 
 * @date 2016-6-1 涓嬪崍3:01:27  
 */
public class PlanListFragment extends BaseFragment implements IGetPlanListView
{

	private View parentView = null;
	private RefreshRecyclerView listView = null;
	private View viewLeft = null;
	private View viewRight = null;
	private View viewBottom = null;
	private TextView tvLeft = null;
	private TextView tvRight = null;
	private ISFooterView footer = null;
	private PlanListAdapter adapter = null;
	
	private GetPlanListPresenter getPlanListPresenter = null;
	
	private PlanInfor planInfor = null;
	private String terminalId = null;
	private String terminalName = null;
	private List<PlanInfor> planList = new ArrayList<PlanInfor>();
	private List<SubPlanInfor> subPlanList = new ArrayList<SubPlanInfor>();
	
	private int pageNo = 1;
	private String status = "0";
	private String userId = "";
	private String userName = "";
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == HttpMsg.ADD_PLAN_START)
			{
				showProgressDialog(getActivity(), "正在处理...");
			}
			else if(msg.what == HttpMsg.ADD_PLAN_SUCCESS)
			{
				dismissDialog();
				planInfor.setSubPlanList(subPlanList);
				showCurPlanView();
			}
			else if(msg.what == HttpMsg.ADD_PLAN_FAIL)
			{
				String error = (String)msg.obj;
				if(TextUtils.isEmpty(error))
					error = "操作失败";
				dismissDialog();
			}
			else if(msg.what == HttpMsg.GET_CUR_PLAN_START)
			{
				//planView.showErrorHint("正在获取播放计划...");
				//showToast("获取数据失败，请下拉刷新");
				//showLoadingView(true);
			}
			else if(msg.what == HttpMsg.START_OLDPLAN_START)
			{
				showProgressDialog(getActivity(), "正在启动计划...");
			}
			else if(msg.what == HttpMsg.START_OLDPLAN_SUCCESS)
			{
				getCurPlans();
				dismissDialog();
			}
			else if(msg.what == HttpMsg.START_OLDPLAN_FAIL)
			{
				dismissDialog();
				String error = (String)msg.obj;
				if(TextUtils.isEmpty(error))
					error = "启动计划失败";
				showToast(error);
			}
			else if(msg.what == HttpMsg.DELETE_OLDPLAN_START)
			{
				showProgressDialog(getActivity(), "正在删除计划...");
			}
			else if(msg.what == HttpMsg.DELETE_OLDPLAN_SUCCESS)
			{
				pageNo = 1;
				getCurPlans();
				dismissDialog();
			}
			else if(msg.what == HttpMsg.DELETE_OLDPLAN_FAIL)
			{
				dismissDialog();
				String error = (String)msg.obj;
				if(TextUtils.isEmpty(error))
					error = "删除失败";
				showToast(error);
			}
		};
	};
	
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(parentView == null)
		{
			parentView = getContentView(R.layout.activity_plan_list);
			
			viewBottom = parentView.findViewById(R.id.view_plan_list_head);
			viewLeft = parentView.findViewById(R.id.view_plan_list_leftopr);
			viewRight = parentView.findViewById(R.id.view_plan_list_rightopr);
			tvLeft = (TextView)parentView.findViewById(R.id.tv_plan_list_leftopr);
			tvRight = (TextView)parentView.findViewById(R.id.tv_plan_list_rightopr);
			listView = (RefreshRecyclerView)parentView.findViewById(R.id.lv_plan);
			footer = new ISFooterView(getActivity());
			adapter = new PlanListAdapter(getActivity(), planList);
			
			getPlanListPresenter = new GetPlanListPresenter(getActivity(), this);
			
			listView.setAdapter(adapter);
			adapter.addFootView(footer);
			listView.setLayoutManager(new LinearLayoutManager(getActivity()));
	        
			listView.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
			footer.showLoadReady();
	        footer.setOnClickListener(new OnClickListener() 
	        {
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					getCurPlans();
				}
			});
			
			UserInfor userInfor = LocalDataEntity.newInstance(getActivity()).getUserInfor();
			userId = userInfor.getUserId();
			userName = userInfor.getUserName();
			
			if(planInfor != null && planInfor.getSubPlanList() != null && planInfor.getSubPlanList().size() != 0)
				showCurPlanView();
			
			if(planInfor == null)
				planInfor = new PlanInfor();
			
			
			if(TextUtils.isEmpty(userId))
				setCenterString("鎴戠殑鎾斁璁″垝");
			else
				setCenterString("鍖哄煙璁″垝");
			showRightView(true);
			setRightText("鏂板缓璁″垝");
			
			viewBottom.setVisibility(View.VISIBLE);
			
			listView.setRefreshAction(new Action() 
	        {
	            @Override
	            public void onAction() 
	            {
	            	pageNo = 1;
	            	getCurPlans();
	            }
	        });
	        
			listView.getRecyclerView().setOnScrollListener(new RecyclerView.OnScrollListener() 
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
	                	getCurPlans();
	                }
	            }
	        });
			
			if(TextUtils.isEmpty(terminalName))
			{
				if(TextUtils.isEmpty(userId))
					setCenterString("我的播放计划");
				else
					setCenterString("区域计划");
				showRightView(true);
				setRightText("新建计划");
			}
			else
			{
				if(getLocalData().isAdminUser())
				{
					showRightView(false);
					setCenterString(terminalName+"的播放计划");
				}
				else 
				{
					if(TextUtils.isEmpty(userId))
						setCenterString("我的播放计划");
					else
						setCenterString("区域计划");
					showRightView(true);
					setRightText("新建计划");
				}
			}
			
			//if(getLocalData().isAdminUser())
			if(TextUtils.isEmpty(terminalId))
				viewBottom.setVisibility(View.VISIBLE);
			else
				viewBottom.setVisibility(View.GONE);
			
			getRightView().setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View arg0) 
				{
					// TODO Auto-generated method stub
					//ViewUtils.startActivity(getActivity(), PlanCreateListActivity.class, null, 2);
					Bundle bundle = new Bundle();
					bundle.putBoolean("IS_EDIT", false);
					bundle.putString("USER_ID", userId);
					bundle.putString("USER_NAME", userName);
					ViewUtils.startActivity(getActivity(), PlanDetailActivity.class, bundle, 2);
				}
			});
			viewLeft.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View arg0) 
				{
					// TODO Auto-generated method stub
					showLeftView();
					planList.clear();
					adapter.notifyDataSetChanged();
					pageNo = 1;
					status = "0";
					getCurPlans();
				}
			});
			viewRight.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View arg0) 
				{
					// TODO Auto-generated method stub
					showRightView();
					planList.clear();
					adapter.notifyDataSetChanged();
					pageNo = 1;
					status = "1";
					getCurPlans();
				}
			});
			showLeftView();
		}
		else
		{
			ViewGroup parent = (ViewGroup) parentView.getParent();
            if(parent != null) 
            {
                parent.removeView(parentView);
            }
		}
		getCurPlans();
		return parentView;
	}
	
	private void getCurPlans()
	{
		if(TextUtils.isEmpty(terminalId))
			getPlanListPresenter.getCurPlan(userId, status, pageNo);
		else
		{
			/*if(status.equals("0"))
				httpFactory.getBoxPlan(status, pageNo, terminalId);
			else
				httpFactory.getCurPlan(status, pageNo);*/
			getPlanListPresenter.getBoxPlan(status, pageNo, terminalId);
		}
	}
	
	public void startCreatePlan()
	{
		Bundle bundle = new Bundle();
		bundle.putBoolean("IS_EDIT", false);
		bundle.putString("USER_ID", userId);
		bundle.putString("USER_NAME", userName);
		ViewUtils.startActivity(getActivity(), PlanDetailActivity.class, bundle, 2);
	}
	
	private void showLeftView()
	{
		viewLeft.setSelected(true);
		viewRight.setSelected(false);
		tvLeft.setTextColor(getResources().getColor(R.color.text_blue_on));
		tvRight.setTextColor(getResources().getColor(R.color.text_black_on));
	}
	private void showRightView()
	{
		viewLeft.setSelected(false);
		viewRight.setSelected(true);
		tvLeft.setTextColor(getResources().getColor(R.color.text_black_on));
		tvRight.setTextColor(getResources().getColor(R.color.text_blue_on));
	}
	
	private void showCurPlanView()
	{
		if(planInfor != null)
		{
			subPlanList.addAll(planInfor.getSubPlanList());
			adapter.notifyDataSetChanged();
		}
		setCenterString("褰撳墠鎾斁璁″垝");
		showRightView(false);
	}
	
	/**
	*callbacks
	*/
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(resultCode != RESULT_OK)
		{
			return;
		}
		if(requestCode == 2)
		{
			planInfor = (PlanInfor)data.getSerializableExtra("PlanInfor");
			subPlanList.clear();
			showCurPlanView();
			Constant.isPlanUpdated = true;
			pageNo = 1;
			listView.onFresh();
		}
		
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}*/
	
	/**
	*callbacks
	*//*
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// TODO Auto-generated method stub
		if(status.equals("0"))
		{
			if(arg2 >= 1)
				arg2 = arg2 - 1;
			Bundle bundle = new Bundle();
			bundle.putSerializable("PlanInfor", planList.get(arg2));
			bundle.putBoolean("IS_EDIT", true);
			ViewUtils.startActivity(getActivity(), PlanDetailActivity.class, bundle, 2);
		}
	}*/
	
	@Override
	public void requestRunning(int requestId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void requestFailed(int requestId, String error) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void requestSuccess(int requestId, List<PlanInfor> tempList, int total) 
	{
		// TODO Auto-generated method stub
		if(requestId == HttpRequestID.GET_SPEAKER_PLAN_LIST)
		{
			if(total > 0)
			{
				if(TextUtils.isEmpty(userId))
					setCenterString("我的播放计划(" + total + ")");
				else
					setCenterString("区域计划(" + total + ")");
			}
			int oldSize = planList.size();
			if(pageNo == 1)
				planList.clear();
			
			if(tempList.size() > 0)
				planList.addAll(tempList);
			if(planList.size() == 0)
			{
				showNoDataView("您还没有创建计划哦~");
			}
			if(planList.size() >= total)
			{
				footer.setVisibility(View.GONE);
			}
			else
			{
				footer.setVisibility(View.VISIBLE);
			}
			adapter.notifyDataSetChanged();
			hideHintView();
			
			pageNo ++;
			
			adapter.notifyItemRangeChanged(oldSize, planList.size());
			hideHintView();
			footer.setVisibility(View.GONE);
			listView.dismissSwipeRefresh();
		}
		
	}
	@Override
	public String getTerminalId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		
	}

}
 
