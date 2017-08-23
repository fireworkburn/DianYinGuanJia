package com.znt.vodbox.view;

import com.znt.vodbox.R;
import com.znt.vodbox.adapter.Action;
import com.znt.vodbox.adapter.RecycleViewAdapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


public class RefreshRecyclerView extends FrameLayout 
{

    private final String TAG = "RefreshRecyclerView";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecycleViewAdapter mAdapter;

    public RefreshRecyclerView(Context context) 
    {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) 
    {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) 
    {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.view_refresh_recycler, this);
        
        mRecyclerView = (RecyclerView) view.findViewById(R.id.$_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.$_refresh_layout);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView);
        boolean refreshAble = typedArray.getBoolean(R.styleable.RefreshRecyclerView_refresh_able, true);
        if (!refreshAble) 
        {
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    public void setAdapter(RecycleViewAdapter adapter)
    {
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;
    }

    public void setLayoutManager(final RecyclerView.LayoutManager layoutManager)
    {
        mRecyclerView.setLayoutManager(layoutManager);
        /*if (layoutManager instanceof GridLayoutManager) 
        {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position) 
                {
                    int type = mAdapter.getItemViewType(position);
                    if (type == RecyclerAdapter.HEADER_TYPE
                            || type == RecyclerAdapter.FOOTER_TYPE
                            || type == RecyclerAdapter.STATUS_TYPE) 
                    {
                        return ((GridLayoutManager) layoutManager).getSpanCount();
                    }
                    else
                    {
                        return 1;
                    }
                }
            });
        }*/
    }

    public void setRefreshAction(final Action action) 
    {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() 
        {
            @Override
            public void onRefresh() 
            {
                action.onAction();
            }
        });
    }

    /*public void setItemSpace(int left, int top, int right, int bottom) {
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(left, top, right, bottom));
    }*/

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) 
    {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public RecyclerView getRecyclerView() 
    {
        return mRecyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() 
    {
        return mSwipeRefreshLayout;
    }

    public void setSwipeRefreshColorsFromRes(@ColorRes int... colors) 
    {
        mSwipeRefreshLayout.setColorSchemeResources(colors);
    }

    /**
     * 8娴ｏ拷16鏉╂稑鍩楅弫锟� ARGB
     */
    public void setSwipeRefreshColors(@ColorInt int... colors) 
    {
        mSwipeRefreshLayout.setColorSchemeColors(colors);
    }

    public void showSwipeRefresh()
    {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    public void dismissSwipeRefresh()
    {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
