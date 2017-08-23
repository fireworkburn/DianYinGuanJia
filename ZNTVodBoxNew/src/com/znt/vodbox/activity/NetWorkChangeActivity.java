
package com.znt.vodbox.activity; 

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.znt.diange.mina.cmd.DeviceInfor;
import com.znt.vodbox.R;
import com.znt.vodbox.dmc.engine.OnConnectHandler;
import com.znt.vodbox.entity.Constant;
import com.znt.vodbox.mina.client.MinaClient;
import com.znt.vodbox.netset.WifiFactory;
import com.znt.vodbox.timer.CheckSsidTimer;
import com.znt.vodbox.utils.SystemUtils;
import com.znt.vodbox.utils.ViewUtils;

/** 
 * @ClassName: NetWorkChangeActivity 
 * @Description: TODO
 * @author yan.yu 
 * @date 2015-8-20 ����9:35:16  
 */
public class NetWorkChangeActivity extends BaseActivity implements OnClickListener
{
	private ImageView ivLoading = null;
	private ImageView ivHint = null;
	private TextView tvHint = null;
	private TextView tvReconnect = null;
	
	private WifiFactory mWifiFactory = null;
	private DeviceInfor deviceInfor = null;
	private CheckSsidTimer checkSsidTimer = null;
	
	private String currentSSID = "";
	
	private final int CHECK_SSID = 1;
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == CHECK_SSID)
			{
				if(checkSsidTimer.isOver())
				{
					doConnectFail();
				}
				else
				{
					currentSSID = SystemUtils.getConnectWifiSsid(getActivity());
					if(deviceInfor.getWifiName().equals(currentSSID))
					{
						checkSsidTimer.stopTimer();
						doConnectSuccess();
					}
				}
				
			}
			else if(msg.what == OnConnectHandler.ON_NETWORK_RECONNECTED_SUCCESS)
			{
				if(isWifiApConnect())//������ȵ㣬�����ӹ̶�ip
					getLocalData().setWifiInfor(getCurrentSsid(), Constant.WIFI_HOT_PWD);
				Constant.isSongUpdate = true;
				//ViewUtils.sendMessage(handler, START_REGISET);
				ViewUtils.startActivity(getActivity(), DeviceEditActivity.class, null);
				finish();
			}
			else if(msg.what == OnConnectHandler.ON_NETWORK_RECONNETC_FAIL)
			{
				dismissDialog();
				
				boolean isDeviceRemove = (Boolean)msg.obj;
				if(isDeviceRemove)
					showToast("�豸�Ͽ����ӣ�������ѡ���豸");
				else
					showToast("���ӷ�����ʧ�ܣ�������");
				doConnectFail();
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
		
		setContentView(R.layout.activity_net_work_change);
		
		ivLoading = (ImageView)findViewById(R.id.iv_net_work_change_loading);
		ivHint = (ImageView)findViewById(R.id.iv_net_work_change_hint);
		tvHint = (TextView)findViewById(R.id.tv_net_work_change_loading);
		tvReconnect = (TextView)findViewById(R.id.tv_net_work_change_reconnect);
		
		tvReconnect.setOnClickListener(this);
		
		setCenterString("��������");
		
		mWifiFactory = WifiFactory.newInstance(getActivity());
		
		checkSsidTimer = new CheckSsidTimer(getActivity());
		checkSsidTimer.setHandler(handler, CHECK_SSID);
		
		deviceInfor = (DeviceInfor)getIntent().getSerializableExtra("DEVICE_INFOR");
		if(deviceInfor == null)
			deviceInfor = getLocalData().getDeviceInfor();
		
		connectWifi();
	}
	
	private void startCheckSSID()
	{
		checkSsidTimer.setMaxTime(18);
		checkSsidTimer.startTimer();
	}
	
	private void connectWifi()
	{
		if(deviceInfor == null)
			return;
		showConnectingView();
		if(!getCurrentSsid().equals(deviceInfor.getWifiName()))
		{
			mWifiFactory.connectWifi(deviceInfor.getWifiName(), deviceInfor.getWifiPwd());
			startCheckSSID();
		}
		else
			doConnectSuccess();
			
	}
	
	private void showConnectingView()
	{
		ivHint.setImageDrawable(getResources().getDrawable(R.drawable.icon_net_work));
		ivLoading.setVisibility(View.VISIBLE);
		tvReconnect.setVisibility(View.GONE);
		startAnim();
		tvHint.setText("�����л����磬���Ժ�...");
	}
	
	private void showConnectFailView()
	{
		ivHint.setImageDrawable(getResources().getDrawable(R.drawable.icon_net_work_error));
		ivLoading.setVisibility(View.GONE);
		tvReconnect.setVisibility(View.VISIBLE);
		stopAnim();
		tvHint.setText("�����л�ʧ�ܣ�����������");
	}
	
	private void startAnim()
    {
    	Animation anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setRepeatCount(Animation.INFINITE); // ����INFINITE����Ӧֵ-1�������ظ�����Ϊ�����
        anim.setDuration(1000);                  // ���øö����ĳ���ʱ�䣬���뵥λ
        anim.setInterpolator(new LinearInterpolator());	// ����һ������������в�������������ɴӶ�����һ����ʼ�������м�Ĳ��䲿��
        ivLoading.startAnimation(anim);
    }
	private void stopAnim()
	{
		ivLoading.clearAnimation();
	}
	
	private void doConnectSuccess()
	{
		if(checkSsidTimer != null)
			checkSsidTimer.stopTimer();
		
		if(deviceInfor.isAvailable())
		{
			connectServer();
		}
		else
		{
			finish();
		}
	}
	
	private void connectServer()
	{
		if(!MinaClient.getInstance().isConnected())
		{
			MinaClient.getInstance().setOnConnectListener(getActivity(), handler);
			MinaClient.getInstance().close();
			if(isWifiApConnect())//������ȵ㣬�����ӹ̶�ip
				getLocalData().setWifiInfor(getCurrentSsid(), Constant.WIFI_HOT_PWD);
			MinaClient.getInstance().startClient();
		}
	}
	
	private void doConnectFail()
	{
		if(checkSsidTimer != null)
			checkSsidTimer.stopTimer();
		showToast("��������ʧ��,������");
		showConnectFailView();
	}
	
	/**
	*callbacks
	*/
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	/**
	*callbacks
	*/
	@Override
	protected void onPause()
	{
		if(checkSsidTimer != null)
			checkSsidTimer.stopTimer();
		MinaClient.getInstance().stopConnect();
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	/**
	*callbacks
	*/
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v == tvReconnect)
		{
			connectWifi();
		}
	}
}
 
