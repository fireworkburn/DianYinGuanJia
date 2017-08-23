
package com.znt.vodbox.activity; 

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qhkj.weishi.view.drag.DragGrid;
import com.qhkj.weishi.view.drag.OtherGridView;
import com.znt.vodbox.R;
import com.znt.vodbox.adapter.DragAdapter;
import com.znt.vodbox.adapter.OtherAdapter;
import com.znt.vodbox.entity.MusicAlbumInfor;
import com.znt.vodbox.entity.MyAlbumInfor;
import com.znt.vodbox.entity.PlanInfor;
import com.znt.vodbox.entity.SubPlanInfor;
import com.znt.vodbox.factory.HttpFactory;
import com.znt.vodbox.http.HttpMsg;

/** 
 * @ClassName: ChannelManagerActivity 
 * @Description: TODO
 * @author yan.yu 
 * @date 2015-11-24 ����3:39:49  
 */
public class ChannelActivity extends BaseActivity implements OnItemClickListener, OnClickListener
{

	/** �û���Ŀ��GRIDVIEW */
	private DragGrid userGridView;
	/** ������Ŀ��GRIDVIEW */
	private OtherGridView otherGridView;
	
	/** �û���Ŀ��Ӧ���������������϶� */
	DragAdapter userAdapter;
	/** ������Ŀ��Ӧ�������� */
	OtherAdapter otherAdapter;
	
	private HttpFactory httpfactory = null;
	
	private List<TextView> categoryViews = new ArrayList<TextView>();
	
	private SubPlanInfor subPlanInfor = null;
	private boolean isEdit = false;
	private String planId = "";
	/** ������Ŀ�б� */
	List<MusicAlbumInfor> otherChannelList = new ArrayList<MusicAlbumInfor>();
	/** �û���Ŀ�б� */
	List<MusicAlbumInfor> userChannelList = new ArrayList<MusicAlbumInfor>();
	/** �Ƿ����ƶ�����������Ƕ���������Ž��е����ݸ��棬�����������Ϊ�˱������̫Ƶ����ɵ����ݴ��ҡ� */	
	boolean isMove = false;
	
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == HttpMsg.GET_MUSIC_ALBUM_START)
			{
				showLoadingView(true);
				hideHintView();
			}
			else if(msg.what == HttpMsg.GET_MUSIC_ALBUM_SUCCESS)
			{
				
				MyAlbumInfor myAlbumInfor = (MyAlbumInfor)msg.obj;
				
				otherChannelList.clear();
				otherChannelList.addAll(myAlbumInfor.getCreateAlbums());
				otherChannelList.addAll(myAlbumInfor.getCollectAlbums());
				
				filterAlbums();
				
				otherAdapter.notifyDataSetChanged();
				showLoadingView(false);
				hideHintView();
			}
			else if(msg.what == HttpMsg.GET_MUSIC_ALBUM_FAIL)
			{
				showLoadingView(false);
				//showRefreshView(onHintListener);
			}
			else if(msg.what == HttpMsg.EDIT_PLAN_START)
			{
				
			}
			else if(msg.what == HttpMsg.EDIT_PLAN_SUCCESS)
			{
				showToast("�����ɹ�");
				finishAndFeedBack();
			}
			else if(msg.what == HttpMsg.EDIT_PLAN_FAIL)
			{
				showToast("����ʧ��");
			}
		};
	};
	/**
	*callbacks
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_channel);
		
		setCenterString("�赥����");
		
		setRightText("���");
		
		subPlanInfor = (SubPlanInfor)getIntent().getSerializableExtra("SubPlanInfor");
		isEdit = getIntent().getBooleanExtra("IS_EDIT", false);
		planId = getIntent().getStringExtra("PLAN_ID");
		if(subPlanInfor != null)
		{
			userChannelList.addAll(subPlanInfor.getAlbumList());
		}
		else
			subPlanInfor = new SubPlanInfor();
		
		initView();
		initData();
		
	}

	private void filterAlbums()
	{
		int size = userChannelList.size();
		if(size > 0)
		{
			for(int i=0;i<size;i++)
			{
				MusicAlbumInfor infor = userChannelList.get(i);
				//otherChannelList.remove(infor);
				for(int j=0;j<otherChannelList.size();j++)
				{
					if(otherChannelList.get(j).getAlbumId().equals(infor.getAlbumId()))
						otherChannelList.remove(j);
				}
			}
		}
			
	}
	
	/** ��ʼ������*/
	private void initData() 
	{
	    /*userChannelList = DBManager.newInstance(getActivity()).getMusicAlbumInfors(AlbumType.User);
	    otherChannelList = DBManager.newInstance(getActivity()).getMusicAlbumInfors(AlbumType.System);*/
	    userAdapter = new DragAdapter(this, userChannelList);
	    userGridView.setAdapter(userAdapter);
	    
	    otherAdapter = new OtherAdapter(this, otherChannelList);
	    otherGridView.setAdapter(this.otherAdapter);
	    //����GRIDVIEW��ITEM�ĵ������
	    otherGridView.setOnItemClickListener(this);
	    userGridView.setOnItemClickListener(this);
	    
	    httpfactory = new HttpFactory(getActivity(), handler);
	    httpfactory.getMusicAlbums();
	}
	
	/** ��ʼ������*/
	private void initView() 
	{
		userGridView = (DragGrid) findViewById(R.id.dg_channel_user);
		otherGridView = (OtherGridView) findViewById(R.id.dg_channel_other);
		
		getRightView().setOnClickListener(this);
		showRightImageView(false);
	}

	private void clickChannelView(View view)
	{
		int size = categoryViews.size();
		for(int i=0;i<size;i++)
		{
			TextView tvChannel = categoryViews.get(i);
			if((Integer)view.getTag() == (Integer)tvChannel.getTag())
			{
				//��ʾ��ǰƵ��������
				tvChannel.setBackgroundResource(R.drawable.style_channel_bg);
				tvChannel.setTextColor(getResources().getColor(R.color.white));
				
				refreshChannels(i);
			}
			else
			{
				tvChannel.setBackgroundResource(R.color.transparent);
				tvChannel.setTextColor(getResources().getColor(R.color.text_black_on));
			}
		}
	}
	
	private void refreshChannels(int index)
	{
		
		 otherAdapter.notifyDataSetChanged();
	}
	
	/** GRIDVIEW��Ӧ��ITEM��������ӿ�  */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view, final int position,long id) {
		//��������ʱ��֮ǰ������û��������ô���õ���¼���Ч
		if(isMove)
		{
			return;
		}
		switch (parent.getId())
		{
		case R.id.dg_channel_user:
			//positionΪ 0��1 �Ĳ����Խ����κβ���
			//if (position != 0) 
			{
				/*if(userChannelList.size() == 1)
				{
					showToast("�����ٱ���һ��Ƶ��Ŷ~");
					return ;
				}*/
				final ImageView moveImageView = getView(view);
				if (moveImageView != null) 
				{
					TextView newTextView = (TextView) view.findViewById(R.id.tv_channel_name);
					final int[] startLocation = new int[2];
					newTextView.getLocationInWindow(startLocation);
					final MusicAlbumInfor channel = ((DragAdapter) parent.getAdapter()).getItem(position);//��ȡ�����Ƶ������
					otherAdapter.setVisible(false);
					//��ӵ����һ��
					otherAdapter.addItem(channel);
					new Handler().postDelayed(new Runnable() 
					{
						public void run()
						{
							try 
							{
								int[] endLocation = new int[2];
								//��ȡ�յ������
								otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
								MoveAnim(moveImageView, startLocation , endLocation, channel,userGridView);
								userAdapter.setRemove(position);
							} 
							catch (Exception localException)
							{
								
							}
						}
					}, 50L);
				}
			}
			break;
		case R.id.dg_channel_other:
			final ImageView moveImageView = getView(view);
			if (moveImageView != null)
			{
				TextView newTextView = (TextView) view.findViewById(R.id.tv_channel_name);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final MusicAlbumInfor channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
				userAdapter.setVisible(false);
				//��ӵ����һ��
				userAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() 
				{
					public void run() 
					{
						try 
						{
							int[] endLocation = new int[2];
							//��ȡ�յ������
							userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
							MoveAnim(moveImageView, startLocation , endLocation, channel,otherGridView);
							otherAdapter.setRemove(position);
						} 
						catch (Exception localException) 
						{
							
						}
					}
				}, 50L);
			}
			break;
		default:
			break;
		}
	}
	/**
	 * ���ITEM�ƶ�����
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation,int[] endLocation, final MusicAlbumInfor moveChannel,
			final GridView clickGridView) 
	{
		int[] initLocation = new int[2];
		//��ȡ���ݹ�����VIEW������
		moveView.getLocationInWindow(initLocation);
		//�õ�Ҫ�ƶ���VIEW,�������Ӧ��������
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
		//�����ƶ�����
		TranslateAnimation moveAnimation = new TranslateAnimation(
				startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);//����ʱ��
		//��������
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);//����Ч��ִ����Ϻ�View���󲻱�������ֹ��λ��
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() 
		{
			
			@Override
			public void onAnimationStart(Animation animation) 
			{
				isMove = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) 
			{
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) 
			{
				moveViewGroup.removeView(mMoveView);
				// instanceof �����ж�2��ʵ���ǲ���һ�����жϵ������DragGrid����OtherGridView
				if (clickGridView instanceof DragGrid) 
				{
					otherAdapter.setVisible(true);
					otherAdapter.notifyDataSetChanged();
					userAdapter.remove();
				}
				else
				{
					userAdapter.setVisible(true);
					userAdapter.notifyDataSetChanged();
					otherAdapter.remove();
				}
				isMove = false;
			}
		});
	}
	
	/**
	 * ��ȡ�ƶ���VIEW�������ӦViewGroup��������
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) 
	{
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}
	
	/**
	 * �����ƶ���ITEM��Ӧ��ViewGroup��������
	 */
	private ViewGroup getMoveViewGroup() 
	{
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}
	
	/**
	 * ��ȡ�����Item�Ķ�ӦView��
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) 
	{
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v == getRightView())
		{
			if(subPlanInfor == null)
				subPlanInfor = new SubPlanInfor();
			subPlanInfor.setAlbumList(userChannelList);
			if(isEdit && !TextUtils.isEmpty(planId))
			{
				/*httpfactory.editPlan(subPlanInfor.getStartTime(), subPlanInfor.getEndTime()
						, subPlanInfor.getPlanAlbumIds(), planId, subPlanInfor.getId());*/
/*				httpfactory.editPlan(planInfor.getAllStartTimes(), planInfor.getAllEndTimes()
						, planInfor.getAllCategoryIds(), planInfor.getPlanId(), planInfor.getAllScheduleIds());
*/			}
			else
				finishAndFeedBack();
		}
		else
			clickChannelView(v);
	}
	
	private void finishAndFeedBack()
	{
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("SubPlanInfor", subPlanInfor);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}
}
 
