package com.znt.vodbox.view;

import com.znt.vodbox.R;
import com.znt.vodbox.utils.SystemUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchShopView extends RelativeLayout
{

	private Context context = null;
	private AutoCompleteTextView etSearch = null;
	private TextView tvSearch = null;
	public SearchShopView(Context context) {
		super(context);
		initViews(context);
		// TODO Auto-generated constructor stub
	}
	public SearchShopView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
		// TODO Auto-generated constructor stub
	}
	public SearchShopView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initViews(context);
		// TODO Auto-generated constructor stub
	}
	public SearchShopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initViews(context);
		// TODO Auto-generated constructor stub
	}

	private void initViews(Context context)
	{
		this.context = context;
		View.inflate(context, R.layout.view_search_shop, SearchShopView.this);
		
		 etSearch = (AutoCompleteTextView)findViewById(R.id.cet_search_shop);
		 tvSearch = (TextView)findViewById(R.id.tv_search_shop);
	}
	
	public AutoCompleteTextView getEditText()
	{
		return etSearch;
	}
	public TextView getTextView()
	{
		return tvSearch;
	}
	
}
