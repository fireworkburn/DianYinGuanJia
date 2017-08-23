
package com.znt.vodbox.activity; 

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;
import com.znt.vodbox.exception.MyExceptionHandler;
import com.znt.vodbox.utils.SystemUtils;

import android.app.Application;
import okhttp3.OkHttpClient;

/** 
 * @ClassName: MyApplication 
 * @Description: TODO
 * @author yan.yu 
 * @date 2014-7-18 上午11:27:21  
 */
public class MyApplication extends Application
{

	public static boolean isLogin = false;
	
	private static MyApplication mInstance = null;
	
	/**
	*callbacks
	*/
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		
		MyExceptionHandler excetionHandler = MyExceptionHandler.getInstance();
		excetionHandler.init(getApplicationContext());
		
		int size[] = SystemUtils.getScreenSize(getApplicationContext());
		/*ScreenSize.WIDTH = StringUtils.px2dip(getApplicationContext(), size[0]);
		ScreenSize.HEIGHT = StringUtils.px2dip(getApplicationContext(), size[1]);;
		ScreenSize.SCALE = ScreenSize.HEIGHT * 1.0 / ScreenSize.WIDTH;*/
		
		mInstance = this;
		
		ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);

//        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))
                .cookieJar(cookieJar1)
                .hostnameVerifier(new HostnameVerifier()
                {
                    @Override
                    public boolean verify(String hostname, SSLSession session)
                    {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
		
	}
	
	/**
	*callbacks
	*/
	@Override
	public void onTerminate()
	{
		// TODO Auto-generated method stub
		super.onTerminate();
		 /*for (Activity activity : activities) 
		 {  
	         activity.finish();  
		 }  */
	    System.exit(0);  
	}
	
	public static MyApplication getInstance()
	{
		return mInstance;
	}
}
 
