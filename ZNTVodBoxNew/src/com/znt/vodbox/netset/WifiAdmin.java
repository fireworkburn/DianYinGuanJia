package com.znt.vodbox.netset;

import java.util.ArrayList;
import java.util.List;

import com.znt.vodbox.entity.Constant;
import com.znt.vodbox.timer.AbstractTimer.OnCheckListener;
import com.znt.vodbox.timer.WifiListScanTimer;
import com.znt.vodbox.utils.SystemUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * @Description: TODO
 * @author ychuang
 * 
 */
public abstract class WifiAdmin
{
	private static final String TAG = "WifiAdmin";

	private WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	// 鎵弿鍑虹殑缃戠粶杩炴帴鍒楄〃
	private List<ScanResult> mWifiList = new ArrayList<ScanResult>();
	private List<WifiConfiguration> mWifiConfiguration;

	private WifiLock mWifiLock;

	private String mPasswd = "";
	private String mSSID = "";

	private Context mContext = null;
	
	private WifiListScanTimer wifiListScanTimer = null;

	public WifiAdmin(Context context)
	{
		mContext = context;
		// 鍙栧緱WifiManager瀵硅薄
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// 鍙栧緱WifiInfo瀵硅薄
		mWifiInfo = mWifiManager.getConnectionInfo();
		Log.v(TAG, "getIpAddress = " + mWifiInfo.getIpAddress());
		wifiListScanTimer = new WifiListScanTimer(mContext);
		wifiListScanTimer.setMaxTime(5);
	}
	
	public void scanWifiList()
	{
		if(mWifiList == null || mWifiList.size() == 0)
		{
			wifiListScanTimer.setOnCheckListener(new OnCheckListener() 
			{
				@Override
				public void onChecking()
				{
					// TODO Auto-generated method stub
					if(mWifiManager != null)
						mWifiManager.startScan();
					fillWifiList();
					if(mWifiList != null && mWifiList.size() > 0 || wifiListScanTimer.isOver())
						wifiListScanTimer.stopTimer();
				}
				
			});
			wifiListScanTimer.startTimer();
		}
	}

	// 鎵撳紑WIFI
	public void openWifi()
	{
		WifiApAdmin.closeWifiAp(mContext);
		
		if (!mWifiManager.isWifiEnabled())
		{
			if(!mWifiManager.setWifiEnabled(true))
				Toast.makeText(mContext, "鎵撳紑WIFI澶辫触", 0).show();
		}
	}

	// 鍏抽棴WIFI
	public void closeWifi()
	{
		if (mWifiManager.isWifiEnabled())
		{
			mWifiManager.setWifiEnabled(false);
		}
	}
	
	public boolean isWifiEnabled()
	{
		return mWifiManager.isWifiEnabled();
	}

	public abstract Intent myRegisterReceiver(BroadcastReceiver receiver,
			IntentFilter filter);

	public abstract void myUnregisterReceiver(BroadcastReceiver receiver);

	public abstract void onNotifyWifiConnected();

	public abstract void onNotifyWifiConnectFailed();

	// 娣诲姞涓�涓綉缁滃苟杩炴帴
	public void addNetwork(WifiConfiguration wcg)
	{
		WifiApAdmin.closeWifiAp(mContext);
		int wcgID = mWifiManager.addNetwork(wcg);
		boolean b = mWifiManager.enableNetwork(wcgID, true);
		startTimer();
	}

	public static final int TYPE_NO_PASSWD = 0x11;
	public static final int TYPE_WEP = 0x12;
	public static final int TYPE_WPA = 0x13;

	private int time = 0;
	private final int MAX_TIME = 30;
	private boolean isTimerRunning = false;
	private Handler handler = new Handler();
	private Runnable task = new Runnable()
	{
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			time ++;
			String curssid = SystemUtils.getConnectWifiSsid(mContext);
			if(mSSID.equals(curssid))//杩炴帴鎴愬姛
			{
				onNotifyWifiConnected();
				stopTimer();
			}
			else if(isTimerRunning)
				handler.postDelayed(task, 1000);
			if(time >= MAX_TIME)
			{
				onNotifyWifiConnectFailed();
				stopTimer();
			}
		}
	};

	private void startTimer()
	{
		
		if (handler != null)
		{
			stopTimer();
		}
		isTimerRunning = true;
		handler.postDelayed(task, 1000);
	}

	private void stopTimer()
	{
		if (handler != null && isTimerRunning)
		{
			handler.removeCallbacks(task);
			time = 0;
		}
		isTimerRunning = false;
	}

	@Override
	protected void finalize()
	{
		try
		{
			super.finalize();
		} catch (Throwable e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public WifiConfiguration createWifiInfo(String SSID, String Password)
	{
		int type = TYPE_NO_PASSWD;
		if(TextUtils.isEmpty(Password))
			type = TYPE_NO_PASSWD;
		else
			type = TYPE_WPA;
		
		WifiConfiguration tempConfig = isExsits(SSID);

		if (tempConfig != null) 
		{
			mWifiManager.removeNetwork(tempConfig.networkId);
		}
		
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		// nopass
		if (type == TYPE_NO_PASSWD)
		{
			// config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			// config.wepTxKeyIndex = 0;
		}
		// wep
		else if (type == TYPE_WEP)
		{
			if (!TextUtils.isEmpty(Password)) {
				if (isHexWepKey(Password)) {
					config.wepKeys[0] = Password;
				} else {
					config.wepKeys[0] = "\"" + Password + "\"";
				}
			}
			config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		// wpa
		else if (type == TYPE_WPA)
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			// 姝ゅ闇�瑕佷慨鏀瑰惁鍒欎笉鑳借嚜鍔ㄩ噸鑱�
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;
	}
	
	private static boolean isHexWepKey(String wepKey) {
		final int len = wepKey.length();

		// WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
		if (len != 10 && len != 26 && len != 58) {
			return false;
		}

		return isHex(wepKey);
	}

	private static boolean isHex(String key) {
		for (int i = key.length() - 1; i >= 0; i--) {
			final char c = key.charAt(i);
			if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a'
					&& c <= 'f')) {
				return false;
			}
		}

		return true;
	}
	
	// 鏌ョ湅浠ュ墠鏄惁涔熼厤缃繃杩欎釜缃戠粶
	private WifiConfiguration isExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager
				.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	public static final int WIFI_CONNECTED = 0x01;
	public static final int WIFI_CONNECT_FAILED = 0x02;
	public static final int WIFI_CONNECTING = 0x03;

	/**
	 * 鍒ゆ柇wifi鏄惁杩炴帴鎴愬姛,涓嶆槸network
	 * 
	 * @param context
	 * @return
	 */
	public int isWifiContected(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		Log.v(TAG,
				"isConnectedOrConnecting = "
						+ wifiNetworkInfo.isConnectedOrConnecting());
		Log.d(TAG,
				"wifiNetworkInfo.getDetailedState() = "
						+ wifiNetworkInfo.getDetailedState());
		if (wifiNetworkInfo.getDetailedState() == DetailedState.OBTAINING_IPADDR
				|| wifiNetworkInfo.getDetailedState() == DetailedState.CONNECTING)
		{
			return WIFI_CONNECTING;
		} else if (wifiNetworkInfo.getDetailedState() == DetailedState.CONNECTED)
		{
			return WIFI_CONNECTED;
		} else
		{
			Log.d(TAG,
					"getDetailedState() == "
							+ wifiNetworkInfo.getDetailedState());
			return WIFI_CONNECT_FAILED;
		}
	}

	private WifiConfiguration IsExsits(String SSID)
	{
		List<WifiConfiguration> existingConfigs = mWifiManager
				.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs)
		{
			if (existingConfig.SSID.equals("\"" + SSID + "\"") /*
																 * &&
																 * existingConfig
																 * .
																 * preSharedKey.
																 * equals("\"" +
																 * password +
																 * "\"")
																 */)
			{
				return existingConfig;
			}
		}
		return null;
	}
	
    public boolean isWifiHasPwd(String ssid) 
    {
    	boolean result = true;
        List<ScanResult> list = mWifiManager.getScanResults();
        for (ScanResult scResult : list) 
        {
            if (!TextUtils.isEmpty(scResult.SSID) && scResult.SSID.equals(ssid))
            {
                String capabilities = scResult.capabilities;
                Log.i("hefeng","capabilities=" + capabilities);
                if (!TextUtils.isEmpty(capabilities))
                {
                    if (capabilities.contains("WPA") || capabilities.contains("wpa"))
                    {
                        Log.i("hefeng", "wpa");
                    } 
                    else if (capabilities.contains("WEP") || capabilities.contains("wep")) 
                    {
                        Log.i("hefeng", "wep");
                        result = true;
                    } 
                    else
                    {
                        Log.i("hefeng", "no");
                        result = false;
                    }
                }
                
                break;
            }
        }
        
        return result;
    }

	// 鏂紑鎸囧畾ID鐨勭綉缁�
	public void disconnectWifi(int netId)
	{
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	// 妫�鏌ュ綋鍓峎IFI鐘舵��
	public int checkState()
	{
		return mWifiManager.getWifiState();
	}

	// 閿佸畾WifiLock
	public void acquireWifiLock()
	{
		mWifiLock.acquire();
	}

	// 瑙ｉ攣WifiLock
	public void releaseWifiLock()
	{
		// 鍒ゆ柇鏃跺�欓攣瀹�
		if (mWifiLock.isHeld())
		{
			mWifiLock.acquire();
		}
	}

	// 鍒涘缓涓�涓猈ifiLock
	public void creatWifiLock()
	{
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	// 寰楀埌閰嶇疆濂界殑缃戠粶
	public List<WifiConfiguration> getConfiguration()
	{
		return mWifiConfiguration;
	}

	// 鎸囧畾閰嶇疆濂界殑缃戠粶杩涜杩炴帴
	public void connectConfiguration(int index)
	{
		// 绱㈠紩澶т簬閰嶇疆濂界殑缃戠粶绱㈠紩杩斿洖
		if (index > mWifiConfiguration.size())
		{
			return;
		}
		// 杩炴帴閰嶇疆濂界殑鎸囧畾ID鐨勭綉缁�
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
				true);
	}

	public void startScan()
	{
		scanWifiList();
		/*mWifiManager.startScan();
		fillWifiList();*/
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
	}

	// 寰楀埌缃戠粶鍒楄〃
	public List<ScanResult> getWifiList()
	{
		return mWifiList;
	}
	
	public boolean ifHasWifi(String ssid)
	{
		boolean result = false;
		if(mWifiManager != null)
			mWifiManager.startScan();
		//fillWifiList();
		if(mWifiList != null && mWifiList.size() > 0)
		{
			int size = mWifiList.size();
			for(int i=0;i<size;i++)
			{
				ScanResult scanResult = mWifiList.get(i);
				if(scanResult.SSID.equals(ssid))
				{
					result = true;
					break;
				}
			}
		}
		
		return result;
	}
	
	public String ifHasWifiAp()
	{
		String ssid = null;
		if(mWifiManager != null)
			mWifiManager.startScan();
		//fillWifiList();
		if(mWifiList != null && mWifiList.size() > 0)
		{
			int size = mWifiList.size();
			for(int i=0;i<size;i++)
			{
				ScanResult scanResult = mWifiList.get(i);
				if(scanResult.SSID.endsWith(Constant.UUID_TAG))
				{
					ssid = scanResult.SSID;
					break;
				}
			}
		}
		
		return ssid;
	}
	
	private void fillWifiList()
	{
		if(mWifiList != null)
		{
			mWifiList.clear();
		}
		List<ScanResult> tempList = mWifiManager.getScanResults();
		if(tempList != null)
			mWifiList.addAll(tempList);
	}

	// 鏌ョ湅鎵弿缁撴灉
	public StringBuilder lookUpScan()
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < mWifiList.size(); i++)
		{
			stringBuilder
					.append("Index_" + new Integer(i + 1).toString() + ":");
			// 灏哠canResult淇℃伅杞崲鎴愪竴涓瓧绗︿覆鍖�
			// 鍏朵腑鎶婂寘鎷細BSSID銆丼SID銆乧apabilities銆乫requency銆乴evel
			stringBuilder.append((mWifiList.get(i)).toString());
			stringBuilder.append("/n");
		}
		return stringBuilder;
	}

	// 寰楀埌MAC鍦板潃
	public String getMacAddress()
	{
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	// 寰楀埌鎺ュ叆鐐圭殑BSSID
	public String getBSSID()
	{
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	// 寰楀埌IP鍦板潃
	public int getIPAddress()
	{
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	// 寰楀埌杩炴帴鐨処D
	public int getNetworkId()
	{
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	// 寰楀埌WifiInfo鐨勬墍鏈変俊鎭寘
	public String getWifiInfo()
	{
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}
}
