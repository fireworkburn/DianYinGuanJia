package com.znt.vodbox.view;

import com.znt.vodbox.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ISHeaderView extends RelativeLayout
{

	private Context context = null;
	private View bgView = null;
	private View operationView = null;
	
	public ISHeaderView(Context context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		initViews(context);
	}
	public ISHeaderView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initViews(context);
	}
	public ISHeaderView(Context context, AttributeSet attrs, int defStyleAttr) 
	{
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initViews(context);
	}
	public ISHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) 
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
		initViews(context);
	}

	private void initViews(Context context)
	{
		this.context = context;
		View.inflate(context, R.layout.is_news_header, ISHeaderView.this);
		
		bgView = this.findViewById(R.id.is_news_header_bg);
		operationView = this.findViewById(R.id.is_news_header_operation_views);
		
	}
	
	public void showHeader(boolean show)
	{
		if(show)
			bgView.setVisibility(View.VISIBLE);
		else
			bgView.setVisibility(View.GONE);
	}
	
	public void showHintViewOnly(String text )
	{
		operationView.setVisibility(View.GONE);
	}
	public void showHintViewOnly()
	{
		operationView.setVisibility(View.GONE);
	}
	
}
