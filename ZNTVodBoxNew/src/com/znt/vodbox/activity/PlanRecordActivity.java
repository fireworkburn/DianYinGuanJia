/*  
* @Project: ZNTVodBox 
* @User: Administrator 
* @Description: ��ͥ����
* @Author�� yan.yu
* @Company��http://www.zhunit.com/
* @Date 2017-2-16 ����3:12:56 
* @Version V1.1   
 

package com.znt.vodbox.activity; 

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.znt.vodbox.R;
import com.znt.vodbox.entity.PlanInfor;
import com.znt.vodbox.entity.SubPlanInfor;
import com.znt.vodbox.factory.HttpFactory;
import com.znt.vodbox.http.HttpMsg;
import com.znt.vodbox.utils.ViewUtils;
import com.znt.vodbox.view.HintView.OnHintListener;

*//** 
 * @ClassName: PlanRecordActivity 
 * @Description: TODO
 * @author yan.yu 
 * @date 2017-2-16 ����3:12:56  
 *//*
public class PlanRecordActivity extends BaseActivity implements OnItemClickListener
{

	private ListView listView = null;
	private ParentPlanAdapter adapter = null;
	private HttpFactory httpFactory = null;
	
	private OnHintListener onHintListener = null;
	
	private PlanInfor planInfor = null;
	private List<PlanInfor> planList = new ArrayList<PlanInfor>();
	private List<SubPlanInfor> subPlanList = new ArrayList<SubPlanInfor>();
	
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == HttpMsg.ADD_PLAN_START)
			{
				showProgressDialog(getActivity(), "���ڴ���...");
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
					error = "����ʧ��";
				dismissDialog();
			}
			else if(msg.what == HttpMsg.GET_CUR_PLAN_START)
			{
				//planView.showErrorHint("���ڻ�ȡ���żƻ�...");
				//showToast("��ȡ����ʧ�ܣ�������ˢ��");
				showLoadingView(true);
			}
			else if(msg.what == HttpMsg.GET_CUR_PLAN_SUCCESS)
			{
				List<PlanInfor> tempList = (List<PlanInfor>)msg.obj;
				planList.clear();
				if(tempList.size() > 0)
				{
					planList.addAll(tempList);
				}
				else
					showNoDataView("����û�д����κμƻ�Ŷ");
				adapter.notifyDataSetChanged();
				showLoadingView(false);
				hideHintView();
				List<SubPlanInfor> subPlanList = planInfor.getSubPlanList();
				if(subPlanList.size() > 0)
				{
					planView.setPlanInfor(planInfor);
				}
				else
					planView.showErrorHint("����û�в��żƻ�");
				Constant.isPlanUpdated = false;
			}
			else if(msg.what == HttpMsg.GET_CUR_PLAN_FAIL)
			{
				String error = (String)msg.obj;
				if(TextUtils.isEmpty(error))
					error = "��ȡ����ʧ��";
				showToast(error);
				showLoadingView(false);
				showRefreshView(onHintListener);
				//isRunning = false;
				//onLoad(0);
				//showHint("��ȡ���żƻ�ʧ��");
				//planView.showErrorHint("��ȡ���żƻ�ʧ�ܣ�������");
				//showToast("��ȡ����ʧ�ܣ�������ˢ��");
			}
		};
	};
	
	*//**
	*callbacks
	*//*
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_plan_list);
		
		setCenterString("��ʷ�ƻ�");
		
		listView = (ListView)findViewById(R.id.lv_plan);
		
		adapter = new ParentPlanAdapter();
		httpFactory = new HttpFactory(getActivity(), handler);
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
		planInfor = (PlanInfor)getIntent().getSerializableExtra("PlanInfor");
		
		if(planInfor != null && planInfor.getSubPlanList() != null && planInfor.getSubPlanList().size() != 0)
			showCurPlanView();
		
		if(planInfor == null)
			planInfor = new PlanInfor();
		
		showRightView(true);
		setRightText("�½��ƻ�");
		
		onHintListener = new OnHintListener() {
			
			@Override
			public void onHintRefresh() {
				// TODO Auto-generated method stub
				httpFactory.getCurPlan();
			}
		};
		
		getRightView().setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				//ViewUtils.startActivity(getActivity(), PlanCreateListActivity.class, null, 2);
				Bundle bundle = new Bundle();
				bundle.putBoolean("IS_EDIT", false);
				ViewUtils.startActivity(getActivity(), PlanDetailActivity.class, bundle, 2);
			}
		});
		
		httpFactory.getCurPlan();
	}
	
	private void showCurPlanView()
	{
		if(planInfor != null)
		{
			subPlanList.addAll(planInfor.getSubPlanList());
			adapter.notifyDataSetChanged();
		}
		setCenterString("��ǰ���żƻ�");
		showRightView(false);
	}
	
	*//**
	*callbacks
	*//*
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putSerializable("PlanInfor", planInfor);
		planInfor.setSelectIndex(arg2);
		bundle.putBoolean("IS_EDIT", true);
		ViewUtils.startActivity(getActivity(), PlanCreateActivity.class, bundle, 2);
		Bundle bundle = new Bundle();
		bundle.putSerializable("PlanInfor", planList.get(arg2));
		bundle.putBoolean("IS_EDIT", true);
		ViewUtils.startActivity(getActivity(), PlanDetailActivity.class, bundle, 2);
	}
	
	private class ParentPlanAdapter extends BaseAdapter
	{

		*//**
		*callbacks
		*//*
		@Override
		public int getCount() 
		{
			// TODO Auto-generated method stub
			return planList.size();
		}

		*//**
		*callbacks
		*//*
		@Override
		public Object getItem(int arg0) 
		{
			// TODO Auto-generated method stub
			return planList.get(arg0);
		}

		*//**
		*callbacks
		*//*
		@Override
		public long getItemId(int arg0) 
		{
			// TODO Auto-generated method stub
			return arg0;
		}

		*//**
		*callbacks
		*//*
		@Override
		public View getView(int pos, View convertView, ViewGroup arg2)
		{
			// TODO Auto-generated method stub
			ViewHolder vh = null;
			if(convertView == null)
			{
				vh = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.view_parent_plan_item, null);
				vh.tvName = (TextView)convertView.findViewById(R.id.tv_parent_plan_item_name);
				vh.tvTime = (TextView)convertView.findViewById(R.id.tv_parent_plan_item_time);
				vh.tvTypeTime = (TextView)convertView.findViewById(R.id.tv_parent_plan_item_tpye_shop);
				vh.tvTypeShop = (TextView)convertView.findViewById(R.id.tv_parent_plan_item_type_time);
				
				convertView.setTag(vh);
			}
			else
				vh = (ViewHolder)convertView.getTag();
			
			
			PlanInfor infor = planList.get(pos);
			vh.tvName.setText(infor.getPlanName());
			vh.tvTime.setText("�����ڣ� " + infor.getPublishTimeFormat());
			
			
			String planFlag = infor.getPlanFlag();
			if(planFlag.equals("0"))
			{
				vh.tvTypeTime.setText("ȫ������");
			}
			else
			{
				vh.tvTypeTime.setText("ָ������  " + infor.getDeviceList().size() + " ��");
			}
			
			String planType = infor.getPlanType();
			if(planType.equals("0"))
			{
				vh.tvTypeShop.setText("ָ�����ڼƻ�:  " + infor.getStartDate() + "~" + infor.getEndDate());
			}
			else
			{
				vh.tvTypeShop.setText("ÿ��ļƻ�");
			}
			
			
			return convertView;
		}
		
		private class ViewHolder
		{
			TextView tvName = null;
			TextView tvTime = null;
			TextView tvTypeTime = null;
			TextView tvTypeShop = null;
		}
	}
	
}
 
*/