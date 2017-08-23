package com.znt.vodbox.activity;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.znt.diange.mina.entity.UserInfor;
import com.znt.vodbox.R;
import com.znt.vodbox.entity.Constant;
import com.znt.vodbox.entity.LocalDataEntity;
import com.znt.vodbox.mvp.p.HttpLoginPresenter;
import com.znt.vodbox.mvp.v.IHttpLoginView;
import com.znt.vodbox.utils.ViewUtils;
import com.znt.vodbox.view.SplashView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class LoginActivity extends BaseActivity implements IHttpLoginView
{

    public static final String VIDEO_NAME = "welcome_video.mp4";

    private static final int REQUEST_SIGNUP = 0;

    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private TextView tvFindPwd;
    private LinearLayout linearLayout = null;
    private SplashView mSplashView = null;
    private View viewSpBg = null;
    
    private RegisterPage registerPage = null;
    // 填写从短信SDK应用后台注册得到的APPKEY
 	private final String APPKEY = "1042787b0334e";
 	// 填写从短信SDK应用后台注册得到的APPSECRET
 	private final String APPSECRET = "084334a5af0fca9cdd5ebd886516e95a";
 	private boolean ready;
    
    private HttpLoginPresenter httpLoginPresenter = null;
    private Handler handler = new Handler();
    
    private boolean isClickLogin = false;
    private long startTime = 0;
    private final long finishTime = 1000 * 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    	super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.activity_login);
        showTopView(false);
        _signupLink = (TextView)findViewById(R.id.link_signup);
        tvFindPwd = (TextView)findViewById(R.id.link_signup_findpwd);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _emailText = (EditText)findViewById(R.id.input_email);
        _loginButton = (Button)findViewById(R.id.btn_login);
        linearLayout = (LinearLayout) findViewById(R.id.view_login_splash);
        viewSpBg = findViewById(R.id.view_login_splash_bg);
        
        _loginButton.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
            	if (validate()) 
            	{
            		isClickLogin = true;
            		httpLoginPresenter.userLogin();
            	}
            }
        });
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                /*Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);*/
            	startSMSCheck();
            }
        });
        tvFindPwd.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		startSMSCheck();
        	}
        });
        
        mSplashView = new SplashView(this);
		handler.postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				mSplashView.stopRotate();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(MyApplication.isLogin)
							goHomePage();
						else
						{
							showTopView(true);
							setCenterString("用户登录");
							hideSplash();
						}
					}
				}, 500);
			}
		}, finishTime);
    	linearLayout.addView(mSplashView);
    	
    	httpLoginPresenter = new HttpLoginPresenter(getApplicationContext(), this);
    	
    	initSMSSDK();
    	
    	initLogin();
    }
    
    private void hideSplash()
    {
		linearLayout.setVisibility(View.GONE);
		viewSpBg.setVisibility(View.GONE);
    }
    
    private void initLogin()
    {
    	UserInfor info = LocalDataEntity.newInstance(getApplicationContext()).getUserInfor();
    	String account = info.getAccount();
    	String pwd = info.getPwd();
    	if(!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd))
    	{
    		startTime = System.currentTimeMillis();
    		_emailText.setText(account);
    		_passwordText.setText(pwd);
        	httpLoginPresenter.userLogin();
    	}
    }
    
    public boolean validate() {

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        if(email.isEmpty())
        {
        	_emailText.setError("请输入正确的邮箱或者手机号码");
        	return false;
        }
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        		|| isMobileNO(email)) 
        {
            _emailText.setError(null);
        } 
        else 
        {
        	 _emailText.setError("请输入正确的邮箱或者手机号码");
        	 return false;
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 12) 
        {
            _passwordText.setError("请输入6到10位的字母数字");
            return false;
        } 
        else 
        {
            _passwordText.setError(null);
        }

        return true;
    }
    
    private void initSMSSDK() 
	{
		if(ready)
			return;
		// 初始化短信SDK
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		EventHandler eventHandler = new EventHandler() 
		{
			public void afterEvent(int event, int result, Object data) 
			{
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		ready = true;

		// 获取新好友个数
		//showDialog();
		//SMSSDK.getNewFriendsCount();
	}
    
    private void registerSms()
	{
		// 打开注册页面
		if(registerPage == null)
		{
			registerPage = new RegisterPage();
			registerPage.setRegisterCallback(new EventHandler() 
			{
				public void afterEvent(int event, int result, Object data) 
				{
					// 解析注册结果
					if (result == SMSSDK.RESULT_COMPLETE) 
					{
						if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) 
						{
						    if(result == SMSSDK.RESULT_COMPLETE) 
						    {
								boolean smart = (Boolean)data;
								if(smart) 
								{
						           //通过智能验证
									showToast("智能验证");
								} 
								else 
								{
						           //依然走短信验证
									showToast("短信验证");
								}
						    }
						}
						
						@SuppressWarnings("unchecked")
						HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
						String country = (String) phoneMap.get("country");
						String phone = (String) phoneMap.get("phone");
						
						Bundle bundle = new Bundle();
						bundle.putString("phone", phone);
						ViewUtils.startActivity(getActivity(), RegisterActivity.class, bundle, 5);
						//String pwd = (String) phoneMap.get("pwd");
						//int type = (Integer)phoneMap.get("checktype");
						
						/*userInfor.setAccount(phone);
						userInfor.setPwd(pwd);
						
						etAccount.setText(phone);
						etPwd.setText(pwd);*/
					}
				}
			});
		}
		registerPage.show(this);
	}
	
	private void startSMSCheck()
	{
		registerSms();
	}
	
	/**
	*callbacks
	*/
	@Override
	protected void onResume()
	{
		
		//showUserInfor();
		
		if (ready) 
		{
			// 获取新好友个数
			//showDialog();
			//SMSSDK.getNewFriendsCount();
		}
		// TODO Auto-generated method stub
		super.onResume();
	}
	
   public static boolean isMobileNO(String mobiles) 
   {
	    Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$");
	    Matcher m = p.matcher(mobiles);
	    return m.matches();
	}
    

    @Override
    protected void onDestroy() 
    {
        if (ready)
		{
			// 销毁回调监听接口
			SMSSDK.unregisterAllEventHandler();
		}
        super.onDestroy();
    }
    
    private void userLoginResult(UserInfor info)
    {
    	UserInfor userInfor = info;
		userInfor.setAccount(this.getLoginAccount());
		userInfor.setPwd(this.getLoginPwd());
		if(userInfor != null)
		{
			LocalDataEntity.newInstance(getApplicationContext()).setUserInfor(userInfor);
			LocalDataEntity.newInstance(getApplicationContext()).setUserRecord(userInfor);
			
			MyApplication.isLogin = true;
			Constant.isShopUpdated = true;
			Constant.isAlbumUpdated = true;
		}
    }
    
    private void goHomePage()
    {
    	Intent intent = new Intent();
		intent.setClass(getApplicationContext(), MainActivity.class);
		startActivity(intent);
		finish();
    }

	@Override
	public String getLoginAccount() 
	{
		// TODO Auto-generated method stub
		return _emailText.getText().toString().trim();
	}

	@Override
	public String getLoginPwd() 
	{
		// TODO Auto-generated method stub
		return _passwordText.getText().toString().trim();
	}

	@Override
	public void loginRunning() 
	{
		// TODO Auto-generated method stub
		if(isClickLogin)
			showProgressDialog(getActivity(), "正在登录．．．");;
	}

	@Override
	public void loginFailed(String error) 
	{
		// TODO Auto-generated method stub
		Snackbar.make(_loginButton, error, 0).show();
		dismissDialog();
	}

	@Override
	public void loginSuccess(UserInfor info) 
	{
		// TODO Auto-generated method stub
		userLoginResult(info);
		dismissDialog();
		if(isClickLogin)
			goHomePage();
		/*long endTime = System.currentTimeMillis() - startTime;
		if(endTime < finishTime)
		{
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					goHomePage();
				}
			}, endTime);
		}*/
	}
}