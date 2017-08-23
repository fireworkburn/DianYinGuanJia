package com.znt.vodbox.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.smssdk.gui.RegisterPage;

import com.squareup.picasso.Picasso;
import com.znt.diange.mina.entity.UserInfor;
import com.znt.vodbox.R;
import com.znt.vodbox.entity.Constant;
import com.znt.vodbox.factory.HttpFactory;
import com.znt.vodbox.http.HttpMsg;
import com.znt.vodbox.utils.SystemUtils;
import com.znt.vodbox.utils.ViewUtils;
import com.znt.vodbox.view.CircleImageView;
import com.znt.vodbox.view.ClearEditText;
import com.znt.vodbox.view.ItemTextView;

public class AccountActivity extends BaseActivity implements OnClickListener
{
	private View accountUnLogin = null;
	private TextView tvLogin = null;
	private TextView tvRegister = null;
	private TextView tvForgetPwd = null;
	private ClearEditText etAccount = null;
	private ClearEditText etPwd = null;
	private CircleImageView ivHead = null;
	
	private View accountLogin = null;
	private ItemTextView itvName = null;
	private ItemTextView itvUser = null;
	private ItemTextView itvPwd = null;
	private TextView tvLogout = null;
	private TextView tvMoreDetail = null;
	private TextView tvMoreGetBox = null;
	private View viewMore = null;
	private View thirdLoginView = null;
	private ImageView ivQQLogin = null;
	private ImageView ivSinaLogin = null;
	private ImageView ivWeiXinLogin = null;
	
	private UserInfor userInfor = null;
	private boolean isInit = true;
	private boolean isRunning = false;
	
	public static String mAppid = "1104930384";
	private String nickName = "";
	private String headUrl = "";
	private String uid = "";
	private String token = "";
	private HttpFactory httpFactory = null;
	
	// ��д�Ӷ���SDKӦ�ú�̨ע��õ���APPKEY
	private final String APPKEY = "1042787b0334e";
	// ��д�Ӷ���SDKӦ�ú�̨ע��õ���APPSECRET
	private final String APPSECRET = "084334a5af0fca9cdd5ebd886516e95a";
	private boolean ready;
	
	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == HttpMsg.LOGIN_START)//��½��ʼ
			{
				tvLogin.setText("���ڵ�½...");
				isRunning = true;
			}
			else if(msg.what == HttpMsg.LOGIN_SUCCESS)//��½�ɹ�
			{
				userInfor = (UserInfor)msg.obj;
				userInfor.setAccount(etAccount.getText().toString());
				userInfor.setPwd(etPwd.getText().toString());
				if(userInfor != null)
				{
					getLocalData().setUserInfor(userInfor);
					getLocalData().setUserRecord(userInfor);
					
					//showLoginView(userInfor);
					MyApplication.isLogin = true;
					Constant.isShopUpdated = true;
					Constant.isAlbumUpdated = true;
					if(isInit)
					{
						ViewUtils.startActivity(getActivity(), MainActivity.class, null);
						finish();
					}
					else
					{
						setResult(RESULT_OK);
						finish();
					}
				}
				isRunning = false;
				//tvLogin.setText("��½");
			}
			else if(msg.what == HttpMsg.LOGIN_FAIL)//��½ʧ��
			{
				String error = (String)msg.obj;
				if(TextUtils.isEmpty(error))
					error = "��¼ʧ��";
				showToast(error);
				tvLogin.setText("��½");
				isRunning = false;
			}
			else if(msg.what == HttpMsg.SET_USER_HEAD)//����ͷ��
			{
				showProgressDialog(getActivity(), "�����ϴ�ͷ��...");
			}
			else if(msg.what == HttpMsg.SET_USER_HEAD_SUCCESS)//����ͷ��ɹ�
			{
				String headUrl = (String)msg.obj;
				if(!TextUtils.isEmpty(headUrl))
				{
					/*getLocalData().setUserHead(headUrl);
					//MyApplication.imageWorker.loadBitmap(headUrl, new ImageCacheView(ivHead, DecodeType.Ldpi));
					ImageLoader.getInstance().displayImage(headUrl, ivHead, getImageOptions());*/
				}
				dismissDialog();
			}
			else if(msg.what == HttpMsg.SET_USER_HEAD_FAIL)//����ͷ��ʧ��
			{
				String error = (String)msg.obj;
				if(TextUtils.isEmpty(error))
					error = "ͷ���ϴ�ʧ��";
				showToast(error);
				dismissDialog();
			}
			else if(msg.what == HttpMsg.NO_NET_WORK_CONNECT)//����������
			{
				showToast("����������");
				tvLogin.setText("��½");
			}
			else if(msg.what == HttpMsg.HTTP_CANCEL)//����ȡ��
			{
				//showToast("�����쳣");
				tvLogin.setText("��½");
			}
			else if(msg.what == HttpMsg.NEW_PWD_BY_PHONE)
			{
				//ͨ���ֻ������һ�����
				
			}
			else if(msg.what == HttpMsg.NEW_PWD_BY_PHONE_SUCCESS)
			{
				//ͨ���ֻ������һ�����ɹ�
				showToast("�����޸ĳɹ�~");
				etPwd.getText().clear();
			}
			else if(msg.what == HttpMsg.NEW_PWD_BY_PHONE_FAIL)
			{
				//ͨ���ֻ������һ�����ʧ��
				String error = (String)msg.obj;
				if(TextUtils.isEmpty(error))
					error = "����ʧ��";
				showToast(error);
			}
			else if(msg.what == HttpMsg.REGISTER_START)//
			{
				//tvConfirm.setText("����ע��...");
				showProgressDialog(getActivity(), "���ڵ�¼...");
				isRunning = true;
			}
			else if(msg.what == HttpMsg.REGISTER_SUCCESS)//
			{

				getLocalData().setLoginType("1");
				
				Object obj = msg.obj;
				if(obj instanceof String)
				{
					showToast((String)obj);
				}
				else if(obj instanceof UserInfor)
				{
					//showToast("��¼�ɹ�");
					userInfor = (UserInfor)obj;
					userInfor.setHead(headUrl);
					getLocalData().setUserInfor(userInfor);
					getLocalData().setThirdId(uid);
					getLocalData().setThirdToken(token);
					MyApplication.isLogin = true;
					if(isInit)
					{
						ViewUtils.startActivity(getActivity(), MainActivity.class, null);
					}
					else
					{
						setResult(RESULT_OK);
						finish();
					}
				}
				isRunning = false;
			}
			else if(msg.what == HttpMsg.REGISTER_FAIL)//
			{
				
				dismissDialog();
				
				String error = (String)msg.obj;
				if(TextUtils.isEmpty(error))
					error = "��¼ʧ��";
				showToast(error);
				isRunning = false;
				//tvConfirm.setText("ע��");
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_account);
		
		userInfor = getLocalData().getUserInfor();
		
		isInit = getIntent().getBooleanExtra("INIT", true);
		
		httpFactory = new HttpFactory(getActivity(), handler);
		
		initSMSSDK();
		
		getViews();
		initViews();
		initData();
		/*if(!MyApplication.isLogin)
			gotDeviceEdit();*/
		
	}
	
	public void gotDeviceEdit()
	{
		if(isWifiApConnect())
			deviceConfirgure();
	}
	private void deviceConfirgure() 
	{
		Intent intent = new Intent(this, DeviceEditActivity.class);
		startActivity(intent);
	}
	
	private void showUserInfor()
	{
		if(MyApplication.isLogin)
		{
			UserInfor infor = getLocalData().getUserInfor();
			itvUser.getSecondView().setText(infor.getAccount());
			itvName.getSecondView().setText(infor.getUserName());
			itvPwd.getSecondView().setText(infor.getPwd());
			
			if(getLocalData().getLoginType().equals("0"))
			{
				itvUser.setVisibility(View.VISIBLE);
				itvPwd.setVisibility(View.VISIBLE);
			}
			else if(getLocalData().getLoginType().equals("1"))
			{
				itvUser.setVisibility(View.INVISIBLE);
				itvPwd.setVisibility(View.INVISIBLE);
			}
			
			if(!TextUtils.isEmpty(infor.getHead()))
				Picasso.with(getActivity()).load(infor.getHead()).into(ivHead);
			else
				ivHead.setImageResource(R.drawable.logo);
		}
	}
	
	/**
	*callbacks
	*/
	@Override
	protected void onResume()
	{
		
		showUserInfor();
		
		if (ready) 
		{
			// ��ȡ�º��Ѹ���
			//showDialog();
			//SMSSDK.getNewFriendsCount();
		}
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	/**
	*callbacks
	*/
	@Override
	protected void onDestroy()
	{
		if(httpFactory != null)
			httpFactory.stopHttp();
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void getViews()
	{
	
		ivHead = (CircleImageView)findViewById(R.id.civ_account_head);
		etAccount = (ClearEditText)findViewById(R.id.cet_account_unlogin_account);
		etPwd = (ClearEditText)findViewById(R.id.cet_account_unlogin_pwd);
		tvLogin = (TextView)findViewById(R.id.tv_account_login);
		tvRegister = (TextView)findViewById(R.id.tv_account_register);
		tvForgetPwd = (TextView)findViewById(R.id.tv_account_forget_pwd);
		accountUnLogin = findViewById(R.id.view_account_unlogin);
		accountLogin = findViewById(R.id.view_account_login);
		itvName = (ItemTextView)findViewById(R.id.itv_account_login_name);
		itvUser = (ItemTextView)findViewById(R.id.itv_account_login_user);
		itvPwd = (ItemTextView)findViewById(R.id.itv_account_login_pwd);
		tvLogout = (TextView)findViewById(R.id.tv_account_loginout);
		tvMoreDetail = (TextView)findViewById(R.id.tv_account_more);
		tvMoreGetBox = (TextView)findViewById(R.id.tv_account_get_box);
		viewMore = findViewById(R.id.view_account_more);
		thirdLoginView = findViewById(R.id.view_account_third_login);
		ivQQLogin = (ImageView)findViewById(R.id.iv_login_qq);
		ivWeiXinLogin = (ImageView)findViewById(R.id.iv_login_weixin);
		ivSinaLogin = (ImageView)findViewById(R.id.iv_login_sina);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
	}
	private void initViews()
	{
		
		if(MyApplication.isLogin)
			showLoginView();
		else
			showUnLoginView();
		
		tvLogout.setOnClickListener(this);
		tvMoreDetail.setOnClickListener(this);
		tvMoreGetBox.setOnClickListener(this);
		
		itvName.showMoreButton(true);
		itvPwd.showMoreButton(true);
		itvUser.getMoreView().setVisibility(View.INVISIBLE);
		
		itvName.getBgView().setOnClickListener(this);
		itvPwd.getBgView().setOnClickListener(this);
		
		itvName.hideIocn();
		itvPwd.hideIocn();
		itvPwd.showBottomLine(false);
		itvUser.showBottomLine(false);
		itvName.showBottomLine(false);
		
		accountUnLogin.setOnClickListener(this);
		tvLogin.setOnClickListener(this);
		tvRegister.setOnClickListener(this);
		tvForgetPwd.setOnClickListener(this);
		ivQQLogin.setOnClickListener(this);
		ivWeiXinLogin.setOnClickListener(this);
		ivSinaLogin.setOnClickListener(this);
		
		etAccount.setOnEditorActionListener(new OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if ((actionId == 0 || actionId == 2) && event != null) 
                {
                	login();
                }
				// TODO Auto-generated method stub
				return false;
			}
		});
		etPwd.setOnEditorActionListener(new OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if ((actionId == 0 || actionId == 2) && event != null) 
				{
					login();
				}
				// TODO Auto-generated method stub
				return false;
			}
		});
		
	}
	
	private void initData()
	{
		
		if(userInfor != null && !TextUtils.isEmpty(userInfor.getAccount())
				&& !TextUtils.isEmpty(userInfor.getPwd())
				&& !MyApplication.isLogin)
		{
			etAccount.setText(userInfor.getAccount());
			etPwd.setText(userInfor.getPwd());
			login();
		}
		else
		{
			String localCID = getLocalData().getThirdId();
			String localToken = getLocalData().getThirdToken();
			String localName = getLocalData().getUserName();
			headUrl = getLocalData().getUserHead();
			if(!TextUtils.isEmpty(localCID) && !TextUtils.isEmpty(localToken)&& !MyApplication.isLogin)
			{
				uid = localCID;
				token = localToken;
				httpFactory.doRegister(localCID, localToken, localName);
			}
		}
	}
	
	private void showLoginView()
	{
		
		if(accountUnLogin != null)
			accountUnLogin.setVisibility(View.GONE);
		/*if(thirdLoginView != null)
			thirdLoginView.setVisibility(View.GONE);*/
		accountLogin.setVisibility(View.VISIBLE);
		accountLogin.setOnClickListener(this);
		
		setCenterString("�˻�����");
		
		itvPwd.getSecondView().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		itvName.getSecondView().setTextColor(getResources().getColor(R.color.text_black_on));
		itvUser.getSecondView().setTextColor(getResources().getColor(R.color.text_black_on));
		itvPwd.getSecondView().setTextColor(getResources().getColor(R.color.text_black_on));
		
		tvRegister.setVisibility(View.GONE);
		
	}
	
	private void showUnLoginView()
	{
		
		setCenterString("�û���½");
		
		accountUnLogin.setVisibility(View.VISIBLE);
		/*if(thirdLoginView != null)
			thirdLoginView.setVisibility(View.VISIBLE);*/
		if(accountLogin != null)
			accountLogin.setVisibility(View.GONE);
		
		/*String localAccount = getLocalData().getAccount();
		String localPwd = getLocalData().getPwd();
		if(!TextUtils.isEmpty(localAccount))
			etAccount.setText(localAccount);
		if(!TextUtils.isEmpty(localPwd))
			etPwd.setText(localPwd);*/
		
		tvRegister.setVisibility(View.VISIBLE);
		
	}
	
	private void login()
	{
		if(isRunning)
			return;
		String tempAccount = etAccount.getText().toString().trim();
		httpFactory.login(tempAccount, etPwd.getText().toString().trim());
	}
	
	/**
	* @Description: ����ͷ��
	* @param @param image
	* @param @param imageContentType   
	* @return void 
	* @throws
	 */
	private void editUserHead(String image)
	{
		/*httpHelper = new HttpHelper(handler, getActivity());
		MyMultipartEntity mpEntity = new MyMultipartEntity();
		try
		{
			mpEntity.addPart("token", new StringBody(getLocalData().getToken()));
			mpEntity.addPart("id", new StringBody(getLocalData().getUserId()));
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		httpHelper.startHttp(HttpType.SetUserHead, mpEntity, image);
		
		setHttpHelper(httpHelper);*/
	}
	
	private void showPwdEditDialog()
	{
		ViewUtils.startActivity(getActivity(), PwdEditActivity.class, null);
	}
	private void showForgetPwdDialog()
	{
		Bundle bundle = new Bundle();
		bundle.putString("EMAIL", etAccount.getText().toString());
		//ViewUtils.startActivity(getActivity(), ForgetPwdActivity.class, bundle);
	}
	private void showNameEditDialog()
	{
		Bundle bundle = new Bundle();
		bundle.putString("CONTENT", userInfor.getUserName());
		ViewUtils.startActivity(getActivity(), UserInforEditAct.class, bundle, 4);
	}
	
	private void loginByLocalData()
	{
		userInfor = getLocalData().getUserInfor();
		etAccount.setText(userInfor.getAccount());
		etPwd.setText(userInfor.getPwd());
		login();
	}
	
	/**
	*callbacks
	*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
		if (resultCode != RESULT_OK) 
		{
			return;
		} 
		
		if(requestCode == 5)//ע�ᷴ��
		{
			String phone = data.getStringExtra("phone");
			String pwd = data.getStringExtra("pwd");
			etAccount.setText(phone);
			etPwd.setText(pwd);
			login();
			showToast("ע��ɹ�����ʼ��½");
		}
		else if(requestCode == 4)//�ǳƸ�����
		{
			String tempName = data.getStringExtra("NICK_NAME");
			userInfor.setUserName(tempName);
			getLocalData().setUserName(tempName);
			itvName.getSecondView().setText(tempName);
			//getLocalData().setUserName(tempName);
		}
		else if(requestCode == 1)//�ֻ�ע��ɹ���ֱ�ӵ�½
		{
			loginByLocalData();
		}
		else if(requestCode == 2)//���ѡ����Ƭ
		{
			//List<String> tempList = (List<String>)data.getSerializableExtra(IntentParam.IMAGE_LIST);
			String imageUrl = data.getStringExtra("ImageUrl");
			if(!TextUtils.isEmpty(imageUrl))
			{
				File tempFile = new File(imageUrl);
				if(tempFile.exists() && tempFile.canRead())
					editUserHead(imageUrl);
				else
					showToast("ͼƬ��Ч��������ѡ��");
			}
		}
		else if(requestCode == 3)//���ջ�ȡ��Ƭ
		{
			String sdStatus = Environment.getExternalStorageState();  
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) 
            { // ���sd�Ƿ����  
                Log.i("TestFile",  
                        "SD card is not avaiable/writeable right now.");  
                showToast("SD��������");
                return;  
            }  
            @SuppressWarnings("static-access")
			String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";  
            Bundle bundle = data.getExtras();  
            Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ  
          
    		String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
    	
            FileOutputStream b = null;  
           //����ֱ�ӱ�����ϵͳ���λ��
            File file = new File(path);  
            if(!file.exists())
            	file.mkdirs();// �����ļ���  
            String fileName = path + "IMG_" + name;  
  
            try
            {  
                b = new FileOutputStream(fileName);  
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�  
            } 
            catch (FileNotFoundException e) 
            {  
                e.printStackTrace();  
            } 
            finally 
            {  
                try 
                {  
                	if(b != null)
                	{
                		b.flush();  
                        b.close();
                	}
                } 
                catch (IOException e)
                {  
                    e.printStackTrace();  
                }  
            }  
            editUserHead(fileName);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void logOutProcess()
	{
		getLocalData().clearUserInfor();
		showUnLoginView();
		MyApplication.isLogin = false;
		ivHead.setImageResource(R.drawable.logo);
	}
	
	private void initSMSSDK() 
	{
		if(ready)
			return;
		// ��ʼ������SDK
		ready = true;

		// ��ȡ�º��Ѹ���
		//showDialog();
		//SMSSDK.getNewFriendsCount();
	}
	
	private void registerSms()
	{
	}
	
	private void startSMSCheck()
	{
		registerSms();
	}
	
	/**
	*callbacks
	*/
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v == tvForgetPwd)//��������
		{
			startSMSCheck();
			
			//��ȡ��ϵ��
			/*ContactsPage contactsPage = new ContactsPage();
			contactsPage.show(getActivity());*/
			//showForgetPwdDialog();
		}
		else if(v == tvLogin)//��½
		{
			/*if(isWifiApConnect())
				deviceConfirgure();
			else*/
				login();
		}
		else if(v == tvRegister)//ע��
		{
			startSMSCheck();
			//ViewUtils.startActivity(getActivity(), RegisterActivity.class, null, 1);
		}
		else if(v == tvMoreDetail)//ע��
		{
			String url = getString(R.string.system_detail);
			Bundle bundle = new Bundle();
			bundle.putString("TITLE", "�����ܼ�ϵͳ����");
			bundle.putString("URL", url);
			ViewUtils.startActivity(getActivity(), WebViewActivity.class, bundle);
		}
		else if(v == tvMoreGetBox)//ע��
		{
			String url = getString(R.string.get_box);
			Bundle bundle = new Bundle();
			bundle.putString("TITLE", "��������");
			bundle.putString("URL", url);
			ViewUtils.startActivity(getActivity(), WebViewActivity.class, bundle);
		}
		else if(v == tvLogout)//ע��
		{
			Constant.isShopUpdated = true;
			Constant.isAlbumUpdated = true;
			//logout();
			logOutProcess();
			
			//PushManager.getInstance().turnOffPush(getActivity());
		}
		else if(v == itvName.getBgView())//�༭�ǳ�
		{
			showNameEditDialog();
		}
		else if(v == itvPwd.getBgView())//�༭����
		{
			showPwdEditDialog();
		}
		else if(v == accountUnLogin)
		{
			SystemUtils.hideInputView(getActivity());
		}
		else if(v == accountLogin)
		{
			
		}
	}
	
	private long touchTime = 0;
	@Override
	public void onBackPressed()
	{
		if(!isInit && MyApplication.isLogin)
		{
			finish();
		}
		else
		{
			if((System.currentTimeMillis() - touchTime) < 2000)
			{
				 closeApp();
				 super.onBackPressed();
				// TODO Auto-generated method stub
			}
		    else
			{
				showToast("�ٰ�һ���˳� �����ܼ�");
				touchTime = System.currentTimeMillis();
			}
		}
	}
	
	private void closeApp()
	{
		 closeAllActivity();
		 android.os.Process.killProcess(android.os.Process.myPid());
		 System.exit(0);
	}
}
