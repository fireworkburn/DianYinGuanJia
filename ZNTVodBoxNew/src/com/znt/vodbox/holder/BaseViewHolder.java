package com.znt.vodbox.holder;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class BaseViewHolder<T> extends RecyclerView.ViewHolder
{

    private final String TAG = "BaseViewHolder";
    private Context context = null;
    protected int curPosition = 0;
    public BaseViewHolder(View itemView) 
    {
        super(itemView);
    }

    public BaseViewHolder(ViewGroup parent, int layoutId) 
    {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        this.context = parent.getContext();
    }

    public void onInitializeView() 
    {

    }
    
    public <T extends View> T findViewById(@IdRes int resId) 
    {
        return (T) itemView.findViewById(resId);
    }

    public void bindHolder(final T data) 
    {
        /*itemView.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {
                onItemViewClick(data);
            }
        });*/
    }

    public void onItemViewClick(OnClickListener l) 
    {
    	itemView.setOnClickListener(l);
    }
    
    private View operationView = null;
    public void setOperationView(View operationView)
    {
    	this.operationView = operationView;
    }
    public View getOperationView()
    {
    	return operationView;
    }
    
    public void setTag(int pos, Object bean) 
	{
		// TODO Auto-generated method stub
    	itemView.setTag(pos);
    	this.curPosition = pos;
	}

}
