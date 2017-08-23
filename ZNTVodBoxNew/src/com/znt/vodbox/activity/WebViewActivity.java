
package com.znt.vodbox.activity; 

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.znt.vodbox.R;
import com.znt.vodbox.utils.MyWebViewClient;

/** 
 * @ClassName: WebViewActivity 
 * @Description: TODO
 * @author yan.yu 
 * @date 2014-11-5 ����10:04:57  
 */
public class WebViewActivity extends BaseActivity implements OnClickListener
{

	private WebView webView = null;
	
	private MyWebViewClient client = null;
	private String webUrl = null;
	private String title = null;
	/**
	*callbacks
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_webview);
		
		showRightImageView(false);
		setRightText("ˢ��");
		getRightView().setOnClickListener(this);
		
		webUrl = getIntent().getStringExtra("URL");
		title = getIntent().getStringExtra("TITLE");
		if(TextUtils.isEmpty(title))
			title = "��ҳ";
		
		webView = (WebView)findViewById(R.id.webview);
		
		client = new MyWebViewClient(webView);
		client.shouldOverrideUrlLoading(webView, webUrl);
		//���ؽ���
		webView.setWebChromeClient(new WebChromeClient()
	    {
	    	@Override
	    	public void onProgressChanged(WebView view, int newProgress) 
	    	{
	    	    if(newProgress==100)
	    	    {   
	    	    	// ����������activity�ı��⣬ Ҳ���Ը����Լ���������һЩ�����Ĳ���
	    	    	setCenterString(title);
	    	    }
	    	    else
	    	    {
	    	    	setCenterString("������......." + newProgress + "%");
	    	    }
	    	}
	    });
	}
	
	public boolean onKeyDown(int keyCoder,KeyEvent event)
   	{  
        if(webView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK)
        {  
        	//goBack()��ʾ����webView����һҳ��   
        	webView.goBack();   
            return true;  
        }  
        finish();
        return false;  
    }

	/**
	*callbacks
	*/
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v == getRightView())
		{
			webView.stopLoading();
			client.shouldOverrideUrlLoading(webView, webUrl);
		}
	}  
}
 
