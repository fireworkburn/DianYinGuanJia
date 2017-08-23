package com.znt.vodbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.znt.vodbox.R;
import com.znt.diange.mina.entity.UserInfor;
import com.znt.vodbox.factory.HttpFactory;
import com.znt.vodbox.http.HttpMsg;

public class RegisterActivity extends BaseActivity 
{

	private Button mBtnRegister;
	private Button mBtnCancel;
	private EditText etPhone;
	private EditText etPwd;
	private EditText etName;
	
	private String phoneNum;
	private UserInfor userInfor = null;
	private HttpFactory httpFactory = null;
	
	private boolean isRunning = false;
	
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			if(msg.what == HttpMsg.REGISTER_START)//×¢²á¿ªÊ¼
			{
				//tvConfirm.setText("ÕýÔÚ×¢²á...");
				showProgressDialog(getActivity(), "ÕýÔÚ×¢²á...");
				isRunning = true;
			}
			else if(msg.what == HttpMsg.REGISTER_SUCCESS)//×¢²á³É¹¦
			{

				dismissDialog();
				
				Object obj = msg.obj;
				if(obj instanceof String)
				{
					showToast((String)obj);
				}
				else if(obj instanceof UserInfor)
				{
					showToast("×¢²á³É¹¦");
					userInfor = (UserInfor)obj;
					Intent intent = new Intent();
					intent.putExtra("phone", userInfor.getAccount());
					intent.putExtra("pwd", userInfor.getPwd());
					setResult(RESULT_OK,intent);
					finish();
				}
				//getLocalData().setUserInfor(userInfor);
				
				//loginByLocalData();
				isRunning = false;
			}
			else if(msg.what == HttpMsg.REGISTER_FAIL)//×¢²áÊ§°Ü
			{
				
				dismissDialog();
				
				String error = (String)msg.obj;
				if(TextUtils.isEmpty(error))
					error = "×¢²áÊ§°Ü";
				showToast(error);
				isRunning = false;
				//tvConfirm.setText("×¢²á");
			}
		};
	};
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		initScrollView();
		initEditText();
		setBtnListener();
		
		httpFactory = new HttpFactory(getActivity(), handler);
	}
	
	private void initScrollView()
	{
		setContentView(R.layout.activity_register);
		setCenterString("ÓÃ»§×¢²á");
		
		Bundle bundle = this.getIntent().getExtras();
		phoneNum = bundle.getString("phone");
		
	}
	
	private void initEditText() 
	{
		etPhone = (EditText) findViewById(R.id.register_phone);
		etPwd = (EditText) findViewById(R.id.register_psw);
		etName = (EditText) findViewById(R.id.register_user_name);
		
		etPhone.setText(phoneNum);
		
		etPwd.requestFocus();
		
		etPwd.setOnEditorActionListener(new OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if ((actionId == 0 || actionId == 2) && event != null) 
				{
					register();
				}
				// TODO Auto-generated method stub
				return false;
			}
		});
		etName.setOnEditorActionListener(new OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if ((actionId == 0 || actionId == 2) && event != null) 
				{
					register();
				}
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	private void setBtnListener() 
	{
		mBtnRegister = (Button) findViewById(R.id.btn_user_register);
		mBtnRegister.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				register();
			}
		});
		
		mBtnCancel = (Button) findViewById(R.id.btn_cancel);
		mBtnCancel.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				finish();
			}
		});
	}
	
	private void register()
	{
		if(isRunning)
			return;
		String phone = etPhone.getText().toString();
		String pwd = etPwd.getText().toString();
		String name = etName.getText().toString();
		
		if(TextUtils.isEmpty(pwd))
		{
			showToast("ÇëÊäÈëµÇÂ½ÃÜÂë");
			return;
		}
		if(pwd.length() < 6)
		{
			showToast("ÃÜÂë×îÉÙÁùÎ»");
			return;
		}
		
		if(TextUtils.isEmpty(name))
		{
			showToast("ÇëÊäÈëêÇ³Æ");
			return;
		}
		
		/*if(name.length() < 2)
		{
			showToast("êÇ³ÆÌ«¶ÌÀ²");
			return;
		}*/
		
		httpFactory.doRegisterByPhone(phone, pwd, name);
		
		/*Intent intent = new Intent();
		intent.putExtra("phone", etPhone.getText().toString());
		intent.putExtra("pwd", pwd);
		setResult(RESULT_OK, intent);
		finish();*/
	}  
	
	/**
	*callbacks
	*/
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		if(httpFactory != null)
			httpFactory.stopHttp();
	}
}
