package com.znt.vodbox.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.znt.vodbox.R;

public class AddDeviceActivity extends BaseActivity
{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_add);
		
		showTopView(true);
        setCenterString("�����豸");
        setRightTopIcon(R.drawable.icon_edit_suc_white);
        
        getRightView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showToast("�����豸�ɹ�");
			}
		});
	}
	
}