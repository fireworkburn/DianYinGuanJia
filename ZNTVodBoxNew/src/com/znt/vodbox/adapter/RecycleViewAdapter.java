package com.znt.vodbox.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.znt.vodbox.holder.BaseViewHolder;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
* @Author：yuyan
* @Time：2017年6月18日 下午3:50:23
* @Project：InforStream
* @Des:基础适配器
*/
public abstract class RecycleViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> 
{

    private static final String TAG = "RecyclerAdapter";

    private static final int BASE_ITEM_TYPE_HEADER = -1;
    private static final int BASE_ITEM_TYPE_FOOTER = -2;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<View>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<View>();
    protected List<T> mData = new ArrayList<T>();

    private Context mContext;

    public RecycleViewAdapter(Context context) 
    {
        mContext = context;
    }

    public RecycleViewAdapter(Context context, T[] data) 
    {
        this(context, Arrays.asList(data));
    }

    public RecycleViewAdapter(Context context, List<T> data)
    {
        mContext = context;
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) 
    {
        if (mHeaderViews.get(viewType) != null)
        {
            return new BaseViewHolder<T>(mHeaderViews.get(viewType));

        } 
        else if (mFootViews.get(viewType) != null)
        {
        	return new BaseViewHolder<T>(mFootViews.get(viewType));
        }
        else
        {
        	return onCreateBaseViewHolder(parent, viewType);
        }
    }

    public abstract BaseViewHolder<T> onCreateBaseViewHolder(ViewGroup parent, int viewType);

    /* ViewHolder 绑定数据，这里的 position 和 getItemViewType() 方法的 position 不一样
        	这里的 position 指当前可见的 item 的 position 的位置。
        	注意 ：每个 ViewHolder 绑定数据时值调用此方法一次
     */
    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, final int position)
    {
    	if (isHeaderViewPos(position))
        {
            return;
        }
        if (isFooterViewPos(position))
        {
            return;
        }
        holder.setTag(position, null);
    }

    /**
     * ViewHolder 更新 Item 的位置选择 ViewType , 和 UI 是同步的
     */
    @Override
    public int getItemViewType(int position)
    {
        if (isHeaderViewPos(position))
        {
            return mHeaderViews.keyAt(position);
        } 
        else if (isFooterViewPos(position))
        {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        //return super.getItemViewType(position - getHeadersCount());
        //return ((NewsDataBean)mData.get(position - getHeadersCount())).getItemType();
        return 0;
        
    }
    
    private int getRealItemCount()
    {
        return mData.size();
    }

    /**
     * 包含了 Header , Footer , 状态显示 Item
     */
    @Override
    public int getItemCount() 
    {
    	return getHeadersCount() + getFootersCount() + getRealItemCount();
    }


    public void add(T object) 
    {
    	mData.add(object);
        int position;
       /* if (hasFooter)
        {
            position = mViewCount - 1;
        } 
        else 
        {
            position = mViewCount;
        }*/
        //notifyItemInserted(position);
    }

    public void insert(T object, int itemPosition) 
    {
        /*if (mData != null && itemPosition < mViewCount)
        {
            int dataPosition;
            if(hasHeader)
            {
                dataPosition = itemPosition - 1;
            }
            else 
            {
                dataPosition = itemPosition;
            }
            mData.add(dataPosition, object);
            mViewCount++;
            notifyItemInserted(itemPosition);
        }*/
    }

    public void addAll(List<T> data) 
    {
        int size = data.size();
        mData.addAll(data);
        notifyItemRangeInserted(getItemCount(), getItemCount() + data.size());
    }

    public void addAll(T[] objects) 
    {
        addAll(Arrays.asList(objects));
    }

    public void replace(T object, int itemPosition) 
    {
        if(mData != null)
        {
            if(itemPosition < mData.size())
            {
                mData.set(itemPosition, object);
                notifyItemChanged(itemPosition);
            }
        }
    }

    //positionItem start with 0
    public void remove(int itemPosition) 
    {
        int dataPosition;
        int dataSize = mData.size();
        dataPosition = itemPosition;// - mHeaderViews.size();
        if (dataPosition >= 0 && dataPosition < dataSize)
        {
            mData.remove(dataPosition);
            notifyItemRemoved(itemPosition);
            notifyDataSetChanged();
        } 
        else if (dataPosition >= dataSize) 
        {
            Toast.makeText(getContext(),"删除失败",Toast.LENGTH_SHORT).show();
        } 
        else 
        {
            throw new IndexOutOfBoundsException("RecyclerView has header,position is should more than 0 ." +
                    "if you want remove header , pleasure user removeHeader()");
        }
    }

    public void clear() 
    {
        if (mData == null) 
        {
            return;
        }
        mData.clear();
        notifyDataSetChanged();
    }

    public List<T> getData() 
    {
        return mData;
    }

    public Context getContext() 
    {
        return mContext;
    }

    private boolean isHeaderViewPos(int position)
    {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position)
    {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public void addHeaderView(View view)
    {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(View view)
    {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public int getHeadersCount()
    {
        return mHeaderViews.size();
    }

    public int getFootersCount()
    {
        return mFootViews.size();
    }
}
