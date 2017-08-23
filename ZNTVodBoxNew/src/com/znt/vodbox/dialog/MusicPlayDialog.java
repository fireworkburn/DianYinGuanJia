
package com.znt.vodbox.dialog; 

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.znt.diange.mina.entity.MediaInfor;
import com.znt.vodbox.R;
import com.znt.vodbox.http.HttpMsg;
import com.znt.vodbox.player.PlayerHelper;
import com.znt.vodbox.player.PlayerHelper.OnMusicPrepareListener;
import com.znt.vodbox.utils.DownHelper;
import com.znt.vodbox.utils.NetWorkUtils;
import com.znt.vodbox.utils.StringUtils;
import com.znt.vodbox.utils.ViewUtils;

/** 
 * @ClassName: MusicPlayDialog 
 * @Description: TODO
 * @author yan.yu 
 * @date 2015-7-20 ����3:30:43  
 */
public class MusicPlayDialog extends Dialog implements OnMusicPrepareListener
{

	private TextView textTitle = null;
	private TextView textInfor = null;
	private SeekBar seekBar = null;
	private Button btnLeft = null;
	private Button btnRight = null;
	private android.view.View.OnClickListener listener = null;
	
	private PlayerHelper playerHelper = null;
	private DownHelper downHelper = null;
	
	private MediaInfor musicInfor = null;
	private Activity context = null;
	private boolean isDismissed = false;
	private boolean isStop = false;
	private boolean hasUrl = true;
	private int duration = 0;
	private String terminalId = null;
	
	private final int PREPARE_FINISH = 0;
	private final int PREPARE_FAIL = 1;
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == PREPARE_FINISH)
			{
				seekBar.setMax(duration);
				playerHelper.startPlay();
				
				startPlayPosition();
			}
			else if(msg.what == PREPARE_FAIL)
			{
				textTitle.setText("����ʧ�ܣ�������");
			}
		};
	};
	
	/** 
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param context 
	*/
	public MusicPlayDialog(Activity context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

    /** 
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param context
	* @param themeCustomdialog 
	*/
	public MusicPlayDialog(Activity context, int themeCustomdialog)
	{
		super(context, themeCustomdialog);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	public MusicPlayDialog(Activity context, int themeCustomdialog, boolean hasUrl)
	{
		super(context, themeCustomdialog);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.hasUrl = hasUrl;
	}
	
	public void setHasUrl(boolean hasUrl)
	{
		this.hasUrl = hasUrl;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
    {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dialog_music_play);
	    setScreenBrightness();
	    
	    
		seekBar = (SeekBar) MusicPlayDialog.this.findViewById(R.id.sb_dialog_music_play_progress);
		btnRight = (Button) MusicPlayDialog.this.findViewById(R.id.btn_dialog_music_play_right);
    	btnLeft = (Button) MusicPlayDialog.this.findViewById(R.id.btn_dialog_music_play_left);
    	textTitle = (TextView) MusicPlayDialog.this.findViewById(R.id.tv_dialog_music_play_title);
        textInfor = (TextView) MusicPlayDialog.this.findViewById(R.id.tv_dialog_music_play_progress);
		
        downHelper = new DownHelper();
        playerHelper = new PlayerHelper();
        playerHelper.setOnPrepareListener(this);
        
        if(musicInfor.getMediaType().equals(MediaInfor.MEDIA_TYPE_PHONE))
        {
        	btnRight.setText("ȥ����");
        }
        else
        {
        	btnRight.setText("�岥");
        }
        
	    this.setOnShowListener(new OnShowListener()
	    {
            @Override
            public void onShow(DialogInterface dialog)
            {
            	initViews();
            }
        });
	    this.setOnDismissListener(new OnDismissListener()
		{
			@Override
			public void onDismiss(DialogInterface arg0)
			{
				// TODO Auto-generated method stub
				if(playerHelper != null)
					playerHelper.closeMediaPlayer();
				stopPlayPosition();
			}
		});
	    
	    seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onStopTrackingTouch(SeekBar sb)
			{
				// TODO Auto-generated method stub
				if(playerHelper != null && playerHelper.getMediaPlayer() != null)
				{
					int temp = (int) ((duration * 1000) * ((float)sb.getProgress() / sb.getMax()));
					playerHelper.getMediaPlayer().seekTo(temp);
					isStop = false;
					//MyLog.e("onProgressChanged temp-->"+temp);
				}
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int position, boolean fromUser)
			{
				// TODO Auto-generated method stub
				if(fromUser)
				{
					//MyLog.e("onProgressChanged position-->"+position);
					isStop = true;
				}
			}
		});
	}
	
    private void initViews()
    {
        setCanceledOnTouchOutside(false);
        
        textTitle.setText(musicInfor.getMediaName());
        
        /*if(listener != null)
        	btnRight.setOnClickListener(listener);*/
        
        btnLeft.setOnClickListener(new android.view.View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				isDismissed = true;
				dismiss();
			}
		});
        btnRight.setOnClickListener(new android.view.View.OnClickListener()
        {
        	@Override
        	public void onClick(View arg0)
        	{
        		// TODO Auto-generated method stub
        		//checkUrl();
        		showShopListDialog();
        	}
        });
        
        if(hasUrl)
        	startPlayMusic(musicInfor.getMediaUrl());
        else
        	getMusicUrl();
    }
    
    public boolean isDismissed()
    {
    	return isDismissed;
    }
    
    public void setOnClickListener(android.view.View.OnClickListener listener)
    {
    	this.listener  = listener;
    }
    
    public void setInfor(MediaInfor musicInfor)
    {
    	this.musicInfor = musicInfor;
    }
    
    public void updateProgress(String text)
    {
    	this.textInfor.setText(text);
    }
    
    public Button getLeftButton()
    {
    	return btnLeft;
    }
    public Button getRightButton()
    {
    	return btnRight;
    }
    
    private void setScreenBrightness() 
    {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        /**
        *  �˴���������ֵ��dimAmount����ڰ�������Ҳ���ǻ谵�Ķ��٣�����Ϊ0�������ȫ������
        *  ��Χ��0.0��1.0
        */
        lp.dimAmount = 0;
        window.setAttributes(lp);
    }
    
    Runnable tast = new Runnable()
	{
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			
			if(!isDismissed && playerHelper != null && playerHelper.getMediaPlayer() != null && !isStop)
			{
				int progress = playerHelper.getMediaPlayer().getCurrentPosition() / 1000;
				seekBar.setProgress(progress);
				textInfor.setText(StringUtils.secToTime(progress) + " / " + StringUtils.secToTime(duration));
			}
			handler.postDelayed(tast, 800);
		}
	};
    private void startPlayPosition()
    {
    	handler.postDelayed(tast, 800);
    }
    private void stopPlayPosition()
    {
    	handler.removeCallbacks(tast);
    }
    
    private void getMusicUrl()
    {
    	new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				//��������Ҫ���»�ȡ����
				String musicUrl = downHelper.getDlAndPath(musicInfor.getMediaId());
				/*if(musicInfor.getResourceType() == ResoureType.KUWO)
					musicUrl = downHelper.getDlAndPath(musicInfor.getMediaId());
				else if(musicInfor.getResourceType() == ResoureType.WANGYI)
					musicUrl = musicInfor.getMediaUrl();*/
				startPlayMusic(musicUrl);
				
			}
		}).start();
    }
    private void startPlayMusic(String url)
    {
    	if(!isDismissed)
		{
			playerHelper.startInitPlayer(url);
		}
    }
    
    private boolean isCheckUrl = false;
    private void checkUrl()
    {
    	if(isCheckUrl)
    		return;
    	isCheckUrl = true;
    	new Thread(new Runnable() 
    	{
    		@Override
    		public void run() 
    		{
    			// TODO Auto-generated method stub
    			context.runOnUiThread(new Runnable() {
    				
    				@Override
    				public void run() {
    					// TODO Auto-generated method stub
    					textTitle.setText("���ڼ�������Ϣ");
    				}
    			});
    			final boolean isUrlValid = NetWorkUtils.checkURL(musicInfor.getMediaUrl());
    			context.runOnUiThread(new Runnable() 
    			{
    				@Override
    				public void run() 
    				{
    					// TODO Auto-generated method stub
    					textTitle.setText(musicInfor.getMediaName());
    					if(isUrlValid)
    					{
    						dismiss();
    						showShopListDialog();
    					}
    					else
    						Toast.makeText(context, "�ø���������ʧЧ", 0).show();
    					isCheckUrl = false;
    				}
    			});
    		}
    	}).start();
    }

	/**
	*callbacks
	*/
	@Override
	public void onPrepareFinish(MediaPlayer mp)
	{
		// TODO Auto-generated method stub
		duration = (int) Math.ceil((float)(mp.getDuration() / 1000));
		ViewUtils.sendMessage(handler, PREPARE_FINISH);
	}

	/**
	*callbacks
	*/
	@Override
	public void onPrepareFail(String error)
	{
		// TODO Auto-generated method stub
		ViewUtils.sendMessage(handler, PREPARE_FAIL);
	}
	
	private void showShopListDialog()
	{
		final ShopListDialog playDialog = new ShopListDialog(context, musicInfor);
		//playDialog.updateProgress("00:02:18 / 00:05:12");
		if(playDialog.isShowing())
			playDialog.dismiss();
		playDialog.show();
		dismiss();
		
		WindowManager windowManager = (context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = playDialog.getWindow().getAttributes();
		lp.width = (int)(display.getWidth()); //���ÿ��
		lp.height = (int)(display.getHeight()); //���ø߶�
		playDialog.getWindow().setAttributes(lp);
	}
	
}