package com.znt.vodbox.view;

import com.znt.vodbox.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ISFooterView extends RelativeLayout
{
	private Context context = null;
	private View bgView = null;
	private TextView tvHint = null;
	private ProgressBar pb = null;
	
	//private OnClickListener listener = null;
	public void setOnClickListener(OnClickListener listener)
	{
		if(bgView != null)
			bgView.setOnClickListener(listener);
	}

	public ISFooterView(Context context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		initViews(context);
	}
	public ISFooterView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initViews(context);
	}
	public ISFooterView(Context context, AttributeSet attrs, int defStyleAttr) 
	{
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initViews(context);
	}
	public ISFooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) 
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
		initViews(context);
	}
	private void initViews(Context context)
	{
		this.context = context;
		
		View.inflate(context, R.layout.is_news_footer, ISFooterView.this);
		
		bgView = this.findViewById(R.id.is_news_footer_bg);
		tvHint = (TextView)this.findViewById(R.id.tv_is_news_footer_hint);
		pb = (ProgressBar)this.findViewById(R.id.is_news_footer_progress);
		
	}
	
	public void showLoadStartiew()
	{
		showFooter(true);
		pb.setVisibility(View.VISIBLE);
		bgView.setClickable(true);
		tvHint.setText(this.getResources().getString(R.string.load_data_doing));
	}
	public void showLoadReady()
	{
		showFooter(true);
		pb.setVisibility(View.GONE);
		bgView.setClickable(true);
		tvHint.setText(this.getResources().getString(R.string.load_data_ready));
	}
	public void showLoadFailView()
	{
		pb.setVisibility(View.GONE);
		bgView.setClickable(true);
		tvHint.setText(this.getResources().getString(R.string.load_data_fail_hint_reload));
	}
	public void showLoadSuccess()
	{
		pb.setVisibility(View.GONE);
		bgView.setClickable(true);
		tvHint.setText(this.getResources().getString(R.string.load_data_success));
	}
	public void showFooter(boolean show)
	{
		this.setVisibility(show ? View.VISIBLE : View.GONE);
	}
	
}
