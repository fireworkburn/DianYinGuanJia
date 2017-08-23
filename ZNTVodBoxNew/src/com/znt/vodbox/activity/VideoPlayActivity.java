/*  
* @Project: ZNTVodBox 
* @User: Administrator 
* @Description: ��ͥ����
* @Author�� yan.yu
* @Company��http://www.zhunit.com/
* @Date 2016-12-11 ����10:31:11 
* @Version V1.1   
*/ 

package com.znt.vodbox.activity; 

import java.io.IOException;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.znt.diange.mina.entity.MediaInfor;
import com.znt.vodbox.R;
import com.znt.vodbox.dialog.ShopListDialog;

/** 
 * @ClassName: VideoPlayActivity 
 * @Description: TODO
 * @author yan.yu 
 * @date 2016-12-11 ����10:31:11  
 */
public class VideoPlayActivity  extends BaseActivity implements OnCompletionListener,OnErrorListener,OnInfoListener,    
OnPreparedListener, OnSeekCompleteListener,OnVideoSizeChangedListener,SurfaceHolder.Callback
{    
	private Display currDisplay;    
	private SurfaceView surfaceView;    
	private SurfaceHolder holder;    
	private MediaPlayer player;    
	private ProgressBar progressBar = null;
	private int vWidth,vHeight;   
	private MediaInfor musicInfor = null;
	private boolean isTopViewShow = true;
	private TextView tvOritation = null;
	//private boolean readyToPlay = false;    
	        
	public void onCreate(Bundle savedInstanceState)
	{    
	    super.onCreate(savedInstanceState);  
	    
	    this.setContentView(R.layout.video_surface);    
	    
	    surfaceView = (SurfaceView)this.findViewById(R.id.surfaceView);   
	    progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
	    tvOritation = (TextView)this.findViewById(R.id.tv_video_orientation);
	    //��SurfaceView���CallBack����    
	    holder = surfaceView.getHolder();    
	    holder.setKeepScreenOn(true);
	    holder.addCallback(this);    
	    //Ϊ�˿��Բ�����Ƶ����ʹ��CameraԤ����������Ҫָ����Buffer����    
	    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);    
	    
	    showRightView(true);
	    showRightImageView(false);
	    setRightText("�岥");
	    getRightView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showShopListDialog(musicInfor);
				releasePlayer();
			}
		});
	    tvOritation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
				{
					  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				}
				else
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		});
	    surfaceView.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				if(isTopViewShow)
				{
					showTopView(false);
					isTopViewShow = false;
				}
				else
				{
					showTopView(true);
					isTopViewShow = true;
				}
			}
		});
	        
	    //���濪ʼʵ����MediaPlayer����    
	    player = new MediaPlayer();    
	    player.setOnCompletionListener(this);    
	    player.setOnErrorListener(this);    
	    player.setOnInfoListener(this);    
	    player.setOnPreparedListener(this);    
	    player.setOnSeekCompleteListener(this);    
	    player.setOnVideoSizeChangedListener(this);    
	    //Ȼ��ָ����Ҫ�����ļ���·������ʼ��MediaPlayer    
	    musicInfor = (MediaInfor)getIntent().getSerializableExtra("MEDIA_INFOR");
	    String videoName = musicInfor.getMediaName();
	    if(videoName == null)
	    	videoName = "��Ƶ����";
	    setCenterString(videoName);
	    String dataPath = musicInfor.getMediaUrl();    
	    try 
	    {    
	        player.setDataSource(dataPath);    
	    } 
	    catch (IllegalArgumentException e) 
	    {    
	        e.printStackTrace();    
	    } 
	    catch (IllegalStateException e) 
	    {    
	        e.printStackTrace();    
	    } 
	    catch (IOException e) 
	    {    
	        e.printStackTrace();    
	    }    
	    //Ȼ������ȡ�õ�ǰDisplay����    
	    currDisplay = this.getWindowManager().getDefaultDisplay();    
	    
	} 
	
	@Override    
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) 
	{    
	    // ��Surface�ߴ�Ȳ����ı�ʱ����    
	        
	}    
	@Override    
	public void surfaceCreated(SurfaceHolder holder)
	{    
	    // ��SurfaceView�е�Surface��������ʱ�򱻵���    
	    //����������ָ��MediaPlayer�ڵ�ǰ��Surface�н��в���    
	    player.setDisplay(holder);    
	    //��ָ����MediaPlayer���ŵ����������ǾͿ���ʹ��prepare����prepareAsync��׼��������    
	    player.prepareAsync();    
	        
	}    
	@Override    
	public void surfaceDestroyed(SurfaceHolder holder)
	{    
	        
	}    
	@Override    
	public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2)
	{    
	    // ��video��С�ı�ʱ����    
	    //�������������player��source�����ٴ���һ��    
	        
	}    
	@Override    
	public void onSeekComplete(MediaPlayer arg0) 
	{    
	    // seek�������ʱ����    
	        
	}    
	@Override    
	public void onPrepared(MediaPlayer player)
	{    
	    // ��prepare��ɺ󣬸÷������������������ǲ�����Ƶ    
	        
	    //����ȡ��video�Ŀ�͸�    
	    /*vWidth = player.getVideoWidth();    
	    vHeight = player.getVideoHeight();    
	    
	    if(vWidth > currDisplay.getWidth() || vHeight > currDisplay.getHeight())
	    {    
	        //���video�Ŀ���߸߳����˵�ǰ��Ļ�Ĵ�С����Ҫ��������    
	        float wRatio = (float)vWidth/(float)currDisplay.getWidth();    
	        float hRatio = (float)vHeight/(float)currDisplay.getHeight();    
	            
	        //ѡ����һ����������    
	        float ratio = Math.max(wRatio, hRatio);    
	            
	        vWidth = (int)Math.ceil((float)vWidth/ratio);    
	        vHeight = (int)Math.ceil((float)vHeight/ratio);    
	            
	    }  */  
	    //����surfaceView�Ĳ��ֲ���    
        //surfaceView.setLayoutParams(new FrameLayout.LayoutParams(vWidth, vHeight));    
            
        //Ȼ��ʼ������Ƶ    
	    progressBar.setVisibility(View.GONE);
        player.start(); 
	}    
	@Override    
	public boolean onInfo(MediaPlayer player, int whatInfo, int extra) 
	{    
	    // ��һЩ�ض���Ϣ���ֻ��߾���ʱ����    
	    switch(whatInfo){    
	    case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:    
	        break;    
	    case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:      
	        break;    
	    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:    
	        break;    
	    case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:     
	        break;    
	    }    
	    return false;    
	}    
	@Override    
	public boolean onError(MediaPlayer player, int whatError, int extra) 
	{    
	    switch (whatError) 
	    {    
	    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:    
	        break;    
	    case MediaPlayer.MEDIA_ERROR_UNKNOWN:    
	        break;    
	    default:    
	        break;    
	    }    
	    return false;    
	}    
	@Override    
	public void onCompletion(MediaPlayer player) 
	{    
	    // ��MediaPlayer������ɺ󴥷�    
	    this.finish();    
	}  
	
	private void releasePlayer()
	{
		if(player != null)
		{
			if (player.isPlaying())  
				player.stop();
			player.release();  
			player = null;
		}
	}
	
	/**
	*callbacks
	*/
	@Override
	protected void onDestroy() 
	{
		releasePlayer();
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void showShopListDialog(MediaInfor musicInfor)
	{
		final ShopListDialog playDialog = new ShopListDialog(getActivity(), musicInfor);
		//playDialog.updateProgress("00:02:18 / 00:05:12");
		if(playDialog.isShowing())
			playDialog.dismiss();
		playDialog.show();
		
		WindowManager windowManager = (getActivity()).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = playDialog.getWindow().getAttributes();
		lp.width = (int)(display.getWidth()); //���ÿ��
		lp.height = (int)(display.getHeight()); //���ø߶�
		playDialog.getWindow().setAttributes(lp);
	}
}    
