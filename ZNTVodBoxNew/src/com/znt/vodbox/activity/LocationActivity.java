/*  
* @Project: ZNTVodBox 
* @User: Administrator 
* @Description: ��ͥ����
* @Author�� yan.yu
* @Company��http://www.zhunit.com/
* @Date 2017-4-11 ����10:49:48 
* @Version V1.1   
*/ 

package com.znt.vodbox.activity; 

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.znt.vodbox.R;
import com.znt.vodbox.entity.LocationInfor;
import com.znt.vodbox.utils.SystemUtils;
import com.znt.vodbox.utils.ViewUtils;

/** 
 * @ClassName: LocationActivity 
 * @Description: TODO
 * @author yan.yu 
 * @date 2017-4-11 ����10:49:48  
 */
public class LocationActivity extends BaseActivity implements OnClickListener, OnItemClickListener,
AMapLocationListener, OnGeocodeSearchListener, OnPoiSearchListener, Runnable, TextWatcher
{

	private View header = null;
	private View viewCurLocation = null;
	private TextView tvLocation = null;
	private AutoCompleteTextView etSearch = null;
	private TextView tvSearch = null;
	private TextView tvHint = null;
	private ListView listView = null;
	
	private List<String> addrs = new ArrayList<String>();
	private List<TextView> addrViews = new ArrayList<TextView>();
	
	// ��λ���
	private LocationManagerProxy aMapLocManager = null;
	private AMapLocation aMapLocation;// �����ж϶�λ��ʱ
	private Handler handler = new Handler();
	private GeocodeSearch geocoderSearch;
	
	private PoiResult poiResult; // poi���صĽ��
	private PoiSearch.Query query;// Poi��ѯ������
	private PoiSearch poiSearch;// POI����
	
	private boolean isFirst = true;
	private boolean isStop = false;
	private boolean isNearBy = true;
	
	private PoiAdapter adapter = null;
	
	private List<PoiItem> poiNear = new ArrayList<PoiItem>();
	private List<PoiItem> poiList = new ArrayList<PoiItem>();
	
	private LocationInfor locationInfor = null;
	
	private String poiProvince = "";
	private String poiCity = "";
	private String poiStrict = null;
	
	private boolean isShopperAddr = false;
	
	private long touchTime = 0;
	
	/**
	*callbacks
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_location);
		
		header = LayoutInflater.from(getActivity()).inflate(R.layout.activity_location_header, null);
		viewCurLocation = header.findViewById(R.id.view_location_cur_location);
		listView = (ListView)findViewById(R.id.lv_location);
		tvLocation = (TextView)header.findViewById(R.id.tv_location_cur_location);
		etSearch = (AutoCompleteTextView)header.findViewById(R.id.cet_location_header);
		tvSearch = (TextView)header.findViewById(R.id.tv_search);
		tvHint = (TextView)header.findViewById(R.id.tv_location_second_tag);
		
		listView.addHeaderView(header);
		listView.setOnItemClickListener(this);
		adapter = new PoiAdapter();
		listView.setAdapter(adapter);
		
		//viewCurLocation.setOnClickListener(this);
		tvSearch.setOnClickListener(this);
		tvLocation.setOnClickListener(this);
		etSearch.addTextChangedListener(this);// ����ı����������¼�
		
		setCenterString("����λ��");
		
		isShopperAddr = getIntent().getBooleanExtra("AddrSelect", false);
		
		locationInfor = new LocationInfor();
		
		etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() 
		{ 
			@Override 
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
			{ 
				if (actionId == EditorInfo.IME_ACTION_SEARCH) 
				{ 
					doSearchQuery();
					return true; 
					
				} 
				return false; 
			}
		});
		
	}
	
	@Override
	protected void onResume() 
	{
		startLocation();
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void startLocation()
	{
		
		if(SystemUtils.isNetConnected(getActivity()))
		{
			stopLocation();
			initMapLoc();
		}
		else
			showNetWorkSetDialog();
	}
	
	private void showNearPoi()
	{
		isNearBy = true;
		
		poiList.clear();
		poiList.addAll(poiNear);
		adapter.notifyDataSetChanged();
		
		tvHint.setText("�ҵ��ܱ�");
		
	}
	private void showSearch(String keyWord)
	{
		isNearBy = false;
		tvHint.setText(keyWord + "�ܱ�");
	}
	
	private void showNetWorkSetDialog()
	{
		showAlertDialog(getActivity(), this, null, "δ��⵽���磬��������������");
	}
	
	private void initMapLoc()
	{
		tvLocation.setText("���ڶ�λ����λ��...");
		
		isStop = false;
		isFirst = true;
		aMapLocManager = LocationManagerProxy.getInstance(getActivity());
		aMapLocManager.setGpsEnable(true);
		aMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 2000, 10, this);
		handler.postDelayed(this, 12000);// ���ó���12�뻹û�ж�λ����ֹͣ��λ
	}
	
    private void showCurLocation(String addrDesc)
    {
         tvLocation.setText(addrDesc);
    }
    
    /**
	 * ��ʼ����poi����
	 */
	protected void doSearchQuery() 
	{
		
		SystemUtils.hideInputView(getActivity());
		
		String keyWords = etSearch.getText().toString();
		if(TextUtils.isEmpty(keyWords))
			showNearPoi();
		else
		{
			showSearch(keyWords);
			
			poiList.clear();
			
			query = new PoiSearch.Query(keyWords, "", poiCity);// ��һ��������ʾ�����ַ������ڶ���������ʾpoi�������ͣ�������������ʾpoi�������򣨿��ַ�������ȫ����
			/*query.setPageSize(10);// ����ÿҳ��෵�ض�����poiitem
			query.setPageNum(currentPage);// ���ò��һҳ
*/			
			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn();
		}
	}
	
	private void doFinishAction()
	{
		/*if(isShopperAddr)//ѡ�����Ա��ַ
		{
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("LocationInfor", locationInfor);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
		}
		else
		{
			saveLocation();
			goHomePage();
		}*/
		
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		if(locationInfor == null || TextUtils.isEmpty(locationInfor.getLat()))
		{
			showToast("δ��ȡ��λ����Ϣ��������");
			return;
		}
		bundle.putSerializable("LocationInfor", locationInfor);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	/**
	*callbacks
	*/
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btn_my_alert_right)
		{
			ViewUtils.startNetWorkSet(getActivity());
		}
		else if(v == tvSearch)
		{
			doSearchQuery();
		}
		else if(v == viewCurLocation || v == tvLocation)
		{
			doFinishAction();
		}
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		stopLocation();// ֹͣ��λ
	}
	
	/**
	 * ���ٶ�λ
	 */
	private void stopLocation()
	{
		if (aMapLocManager != null) 
		{
			isStop = true;
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
		dismissDialog();
	}

	/**
	 * �˷����Ѿ�����
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	/**
	 * ��϶�λ�ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) 
		{
			if(isFirst)
			{
				isFirst = false;
				isStop = true;
				this.aMapLocation = location;// �жϳ�ʱ����
				
				locationInfor.setLat(location.getLatitude() + "");
				locationInfor.setLon(location.getLongitude() + "");
				
				if(!isStrNull(location.getCity()))
					poiCity = location.getCity();
				if(!isStrNull(location.getProvince()))
					poiProvince = location.getProvince();
				if(!isStrNull(location.getDistrict()))
					poiStrict = location.getDistrict();
				locationInfor.setCity(poiCity);
				locationInfor.setProvince(poiProvince);
				locationInfor.setDistrict(poiStrict);
				
				locationInfor.setPoi(location.getStreet());
				
				Bundle locBundle = location.getExtras();
				String desc = locBundle.getString("desc");
				locationInfor.setAddr(desc);
				showCurLocation(desc);
				/*if(getLocalData().isFirst())
				{
					DBManager.newInstance(getActivity()).deleteDb();
					saveLocation();
				}*/
				
				LatLonPoint latLonPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
				startSearch(latLonPoint);//������������
			}
		}
		dismissDialog();
	}

	private void saveLocation()
	{
		/*MyApplication.isLocChanged = true;
		
		getLocalData().setLocation(locationInfor);
		DBManager.newInstance(getActivity()).insertLocation(locationInfor);*/
	}
	
	@Override
	public void run() 
	{
		if (aMapLocation == null)
		{
			if(!isStop)
			{
				showToast("��λ��ʱ�����������¶�λ");
				tvLocation.setText("��λ��ʱ�����������¶�λ");
			}
			stopLocation();// ���ٵ���λ
		}
	}
	
	private void startSearch(final LatLonPoint latLonPoint)
	{
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		RegeocodeQuery regecodeQuery = new RegeocodeQuery(latLonPoint, 10000,
				GeocodeSearch.AMAP);// ��һ��������ʾһ��Latlng���ڶ�������ʾ��Χ�����ף�������������ʾ�ǻ�ϵ����ϵ����GPSԭ������ϵ
		geocoderSearch.getFromLocationAsyn(regecodeQuery);// ����ͬ��������������
	}
	
	
	class PoiAdapter extends BaseAdapter
	{

		/**
		*callbacks
		*/
		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return poiList.size();
		}

		/**
		*callbacks
		*/
		@Override
		public Object getItem(int arg0)
		{
			// TODO Auto-generated method stub
			return poiList.get(arg0);
		}

		/**
		*callbacks
		*/
		@Override
		public long getItemId(int arg0)
		{
			// TODO Auto-generated method stub
			return arg0;
		}

		/**
		*callbacks
		*/
		@Override
		public View getView(int pos, View convertView, ViewGroup arg2)
		{
			ViewHolder vh = null;
			if(convertView == null)
			{
				vh = new ViewHolder();
				
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.view_location_item, null);
			
				vh.tvName = (TextView)convertView.findViewById(R.id.tv_location_item_name);
				vh.tvDistance = (TextView)convertView.findViewById(R.id.tv_location_item_distance);
				vh.tvAddr = (TextView)convertView.findViewById(R.id.tv_location_item_addr);
				convertView.setTag(vh);
			}
			else
				vh = (ViewHolder)convertView.getTag();
			
			PoiItem infor = poiList.get(pos);
			vh.tvName.setText(infor.getTitle());
			vh.tvDistance.setText(infor.getDistance() + "");
			vh.tvAddr.setText(infor.getAdName() + "_" + infor.getProvinceName() + infor.getCityName());
			
			/*LatLonPoint p = infor.getLatLonPoint();
			double tempLat = p.getLatitude();
			double tempLon = p.getLongitude();
			vh.tvPosition.setText("���ȣ�" + tempLat + "     γ�ȣ�" + tempLon);*/
			
			return convertView;
		}
		
		private class ViewHolder
		{
			public TextView tvName = null;
			public TextView tvDistance = null;
			public TextView tvAddr = null;
		}
		
	}


	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		
	}

	/**
	 * ��������ص�
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		if (rCode == 0)
		{
			
			//currtentCity = result.getRegeocodeAddress().getCity();
			poiNear.clear();
			poiNear.addAll(result.getRegeocodeAddress().getPois());
			showNearPoi();
			
		}
		else if (rCode == 27) 
		{
			showToast(R.string.error_network);
		} 
		else if (rCode == 32) 
		{
			showToast(R.string.error_key);
		} else 
		{
			showToast(getString(R.string.error_other) + rCode);
		}
	}
	
	/**
	*callbacks
	*/
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3)
	{
		// TODO Auto-generated method stub
		
		if(pos >= 1)
			pos = pos - 1;
		if(poiList.size() <= pos)
			return;
		PoiItem infor = poiList.get(pos);
		locationInfor.setLat(infor.getLatLonPoint().getLatitude() + "");
		locationInfor.setLon(infor.getLatLonPoint().getLongitude() + "");
		locationInfor.setPoi(infor.getTitle());
		
		if(!isStrNull(infor.getCityName()))
			poiCity = infor.getCityName();
		if(!isStrNull(infor.getProvinceName()))
			poiProvince = infor.getProvinceName();
		if(!isStrNull(infor.getAdName()))
			poiStrict = infor.getAdName();
		locationInfor.setCity(poiCity);
		locationInfor.setProvince(poiProvince);
		locationInfor.setDistrict(poiStrict);
		
		doFinishAction();
		
	}

	private boolean isStrNull(String str)
	{
		if(!TextUtils.isEmpty(str) && !str.equals("null"))
			return false;
		return true;
	}
	
	/**
	*callbacks
	*/
	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1)
	{
		// TODO Auto-generated method stub
		
	}

	/**
	*callbacks
	*/
	@Override
	public void onPoiSearched(PoiResult result, int rCode)
	{
		// TODO Auto-generated method stub
		if (rCode == 0)
		{
			if (result != null && result.getQuery() != null)
			{
				// ����poi�Ľ��
				if (result.getQuery().equals(query)) 
				{
					// �Ƿ���ͬһ��
					poiResult = result;
					// ȡ����������poiitems�ж���ҳ
					List<PoiItem> tempList = poiResult.getPois();
					if(tempList != null)
					{
						poiList.addAll(tempList);// ȡ�õ�һҳ��poiitem���ݣ�ҳ��������0��ʼ
						List<SuggestionCity> suggestionCities = poiResult
								.getSearchSuggestionCitys();// ����������poiitem����ʱ���᷵�غ��������ؼ��ֵĳ�����Ϣ
						
					}
					else
						poiList.clear();
					adapter.notifyDataSetChanged();
				}
			} 
			else 
			{
				showToast(R.string.no_result);
			}
		} 
		else if (rCode == 27) 
		{
			showToast(R.string.error_network);
		} 
		else if (rCode == 32) 
		{
			showToast(R.string.error_key);
		} 
		else 
		{
			showToast(getString(R.string.error_other) + rCode);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		String newText = s.toString().trim();
		Inputtips inputTips = new Inputtips(getActivity(),
				new InputtipsListener() 
		{

					@Override
					public void onGetInputtips(List<Tip> tipList, int rCode)
					{
						if (rCode == 0)
						{
							// ��ȷ����
							List<String> listString = new ArrayList<String>();
							for (int i = 0; i < tipList.size(); i++) 
							{
								listString.add(tipList.get(i).getName());
							}
							ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
									getApplicationContext(),
									R.layout.view_search_hint, listString);
							etSearch.setAdapter(aAdapter);
							aAdapter.notifyDataSetChanged();
						}
					}
				});
		try 
		{
			inputTips.requestInputtips(newText, "");// ��һ��������ʾ��ʾ�ؼ��֣��ڶ�������Ĭ�ϴ���ȫ����Ҳ����Ϊ��������

		} 
		catch (AMapException e) 
		{
			e.printStackTrace();
		}
	}
	
}
 
