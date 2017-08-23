
package com.znt.vodbox.activity; 

import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.znt.vodbox.R;
import com.znt.vodbox.dialog.AddressInputDialog;
import com.znt.vodbox.entity.Constant;

/** 
 * @ClassName: AboutActivity 
 * @Description: TODO
 * @author yan.yu 
 * @date 2015-10-22 ����3:00:56  
 */
public class AboutActivity extends BaseActivity
{
	private ImageView ivLogo = null;
	private int hitCount = 0;
	/**
	*callbacks
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about);
		
		setCenterString("��������");
		
		ivLogo = (ImageView)findViewById(R.id.iv_about_logo);
		
		ivLogo.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				hitCount ++;
				if(hitCount >= 10)
				{
					Constant.isInnerVersion = true;
					showToast("�ڲ��汾����");
				}
				if(hitCount >= 15)
				{
					showCreateAlbumDialog();
					showToast("�������ģʽ");
				}
			}
		});
	}
	
	private AddressInputDialog dialog = null;
	private void showCreateAlbumDialog()
	{
		if(dialog == null || dialog.isDismissed())
			dialog = new AddressInputDialog(getActivity());
		//playDialog.updateProgress("00:02:18 / 00:05:12");
		if(dialog.isShowing())
			dialog.dismiss();
		dialog.show();
		
		WindowManager windowManager = getActivity().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int)(display.getWidth()); //���ÿ��
		lp.height = (int)(display.getHeight()); //���ø߶�
		dialog.getWindow().setAttributes(lp);
	}
}
 
