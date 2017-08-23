package com.znt.vodbox.activity;

import com.znt.diange.mina.entity.UserInfor;
import com.znt.vodbox.R;
import com.znt.vodbox.entity.LocalDataEntity;
import com.znt.vodbox.fragment.MusicFragment;
import com.znt.vodbox.fragment.MyMusicFragment;
import com.znt.vodbox.fragment.PlanListFragment;
import com.znt.vodbox.fragment.PlanetFragment;
import com.znt.vodbox.fragment.SetFragment;
import com.znt.vodbox.fragment.ShopFragment;
import com.znt.vodbox.mina.client.MinaClient;
import com.znt.vodbox.utils.StringUtils;
import com.znt.vodbox.utils.SystemUtils;
import com.znt.vodbox.utils.ViewUtils;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity 
{

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private TextView tvTitle = null;
    private ImageView ivTopRight = null;
    private TextView tvName = null;
    private TextView tvActCode = null;
    private TextView tvUserType = null;
    private View viewLogin = null;
    private View viewUnlogin = null;
    private View viewCode = null;
    
    private SetFragment setFragment = null;
	private ShopFragment shopFragment = null;
	private MusicFragment musicFragment = null;
	private MyMusicFragment myMusicFragment = null;
	private PlanListFragment planListFragment = null;
	
	private FragmentTransaction transaction;
	
	private MenuItem lastItem;
	
	private int curNav = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setupToolbar();
        setupDrawerContent(mNavigationView);
        mToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawerLayout.setDrawerListener(mToggle);
        //showDefaultFragment();
        lastItem = mNavigationView.getMenu().getItem(1);
        loadFragment(lastItem);
        initUerInfor();
    }
    
    private void initHeaderView()
    {
    	viewLogin = findViewById(R.id.view_header_login);
    	viewUnlogin = findViewById(R.id.view_header_unlogin);
    	viewCode = findViewById(R.id.view_account_header_act_code_bg);
    	tvName = (TextView)findViewById(R.id.tv_account_header_name);
    	tvActCode = (TextView)findViewById(R.id.tv_account_header_act_code);
    	//tvUserType = (TextView)findViewById(R.id.tv_account_header_name);
    	
    	viewLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putBoolean("INIT", false);
				ViewUtils.startActivity(MainActivity.this, AccountActivity.class, bundle, 1);
			}
		});
    	viewUnlogin.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			Bundle bundle = new Bundle();
    			bundle.putBoolean("INIT", false);
    			ViewUtils.startActivity(MainActivity.this, AccountActivity.class, bundle, 1);
    		}
    	});
    	viewCode.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			String content = tvActCode.getText().toString();
    			if(!TextUtils.isEmpty(content))
    			{
    				StringUtils.copy(content, getApplicationContext());
    				//Toast.makeText(getApplicationContext(), "激活码已复制到剪贴板", 0).show();
    				Snackbar.make(viewCode, "激活码已复制到剪贴板", 0).show();
    			}
    		}
    	});
    }

    private void findViews() 
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        tvTitle = (TextView) findViewById(R.id.tv_main_title);
        ivTopRight = (ImageView) findViewById(R.id.iv_main_right);
        initHeaderView();
    }
    
    private void initUerInfor()
    {
    	if(MyApplication.isLogin)
    	{
    		viewLogin.setVisibility(View.VISIBLE);
    		viewUnlogin.setVisibility(View.GONE);
    		UserInfor userInfor = LocalDataEntity.newInstance(getApplicationContext()).getUserInfor();
    		tvName.setText(userInfor.getUserName());
    		tvActCode.setText(LocalDataEntity.newInstance(getApplicationContext()).getPcCode());
    		
    	}
    	else
    	{
    		viewLogin.setVisibility(View.GONE);
    		viewUnlogin.setVisibility(View.VISIBLE);
    	}
    	
    }
    
    @Override
    protected void onPostResume() 
    {
    	// TODO Auto-generated method stub
    	super.onPostResume();
    	initUerInfor();
    }

    private void setupToolbar()
    {
        // Set a Toolbar to replace the ActionBar.
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        
        setSupportActionBar(mToolbar);
    }
    
    public void showTopRightView(boolean show)
    {
    	if(!show)
    		ivTopRight.setVisibility(View.GONE);
    	else
    		ivTopRight.setVisibility(View.VISIBLE);
    }
    public ImageView getTopRightView()
    {
    	return ivTopRight;
    }
    
    public void showTopRightSearchShop()
    {
    	ivTopRight.setImageResource(R.drawable.icon_search_shop_white);
    }
    public void showTopRightCreateAlbum()
    {
    	ivTopRight.setImageResource(R.drawable.icon_add_on);
    }
    public void showTopRightCreatePlan()
    {
    	ivTopRight.setImageResource(R.drawable.icon_add_on);
    }
    
    public void loadFragment(MenuItem menuItem)
    {
    	transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, 0);
    	switch (menuItem.getItemId()) 
        {
	        case R.id.nav_first_fragment:
	        	if(shopFragment == null)
	    			shopFragment = new ShopFragment();
	    		transaction.replace(R.id.flContent, shopFragment, "0");
	    		transaction.addToBackStack("0");// 
	    		transaction.commit();
	    		mDrawerLayout.closeDrawers();
	    		tvTitle.setText(getResources().getString(R.string.nav_my_shops));
	    		showTopRightView(true);
	    		showTopRightSearchShop();
	    		getTopRightView().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ViewUtils.startActivity(MainActivity.this, SearchShopActivity.class, null);
					}
				});
	    		curNav = 0;
	            break;
	        case R.id.nav_second_fragment:
            	if(planListFragment == null)
            		planListFragment = new PlanListFragment();
        		transaction.replace(R.id.flContent, planListFragment, "1");
        		transaction.addToBackStack("1");// 
        		transaction.commit();
        		mDrawerLayout.closeDrawers();
        		tvTitle.setText(getResources().getString(R.string.nav_main_plans));
        		showTopRightView(true);
        		showTopRightCreatePlan();
        		getTopRightView().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						planListFragment.startCreatePlan();
					}
				});
        		curNav = 1;
                break;
            case R.id.nav_third_fragment:
            	if(myMusicFragment == null)
        			myMusicFragment = new MyMusicFragment();
        		transaction.replace(R.id.flContent, myMusicFragment, "2");
        		transaction.addToBackStack("2");// 
        		transaction.commit();
        		mDrawerLayout.closeDrawers();
        		tvTitle.setText(getResources().getString(R.string.nav_my_albums));
        		showTopRightView(true);
        		showTopRightCreateAlbum();
        		getTopRightView().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						myMusicFragment.showCreateAlbumDialog();
					}
				});
        		curNav = 2;
                break;
            case R.id.nav_fourth_fragment:
            	if(musicFragment == null)
        			musicFragment = new MusicFragment();
    			transaction.replace(R.id.flContent, musicFragment, "3");
    			transaction.addToBackStack("3");// 
    			transaction.commit();
    			mDrawerLayout.closeDrawers();
    			tvTitle.setText(getResources().getString(R.string.nav_system_albums));
    			showTopRightView(false);
    			curNav = 3;
                break;
            case R.id.nav_sub_first_fragment:
            	String url1 = getString(R.string.get_box);
    			Bundle bundle1 = new Bundle();
    			bundle1.putString("TITLE", getResources().getString(R.string.apply_online));
    			bundle1.putString("URL", url1);
    			ViewUtils.startActivity(MainActivity.this, WebViewActivity.class, bundle1);
            	
                break;
            case R.id.nav_sub_second_fragment:
            	String url = getString(R.string.system_detail);
    			Bundle bundle = new Bundle();
    			bundle.putString("TITLE", getResources().getString(R.string.system_des));
    			bundle.putString("URL", url);
    			ViewUtils.startActivity(MainActivity.this, WebViewActivity.class, bundle);
                break;
            case R.id.nav_sub_third_fragment:
                //setFragmentArgs(fragment, args, 6);
                break;
            case R.id.nav_sys_first_fragment:
            	if(SystemUtils.isNetConnected(getApplicationContext()))
            		ViewUtils.launchAppDetail(getApplicationContext(), "com.znt.vodbox", "");
            	else
            		Snackbar.make(mDrawerLayout, getResources().getString(R.string.net_work_error), 0).show();
                break;
            case R.id.nav_sys_second_fragment:
            ViewUtils.startActivity(MainActivity.this, AboutActivity.class, null);
            	break;
            case R.id.nav_top_first_fragment:
            	ViewUtils.startActivity(MainActivity.this, DeviceEditActivity.class, null);
            	break;
            default:
            {
            	if(shopFragment == null)
        			shopFragment = new ShopFragment();
        		transaction.replace(R.id.flContent, shopFragment, "2");
        		transaction.addToBackStack("2");// 
        		transaction.commit();
            }
        }
    	
    }
    
    private void setupDrawerContent(NavigationView navigationView) 
    {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() 
                {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) 
                    {
                        //selectDrawerItem(menuItem);
                        loadFragment(menuItem);
                        menuItem.setChecked(true);
                        return true;
                    }
                });
    }

    private ActionBarDrawerToggle setupDrawerToggle() 
    {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
    }

    private void showDefaultFragment() 
    {
    	if(shopFragment == null)
			shopFragment = new ShopFragment();
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, shopFragment).commit();
        curNav = 0;
    }

    private long touchTime = 0;
    
    @Override
    public void onBackPressed() 
    {
        if (isNavDrawerOpen()) 
        {
            closeNavDrawer();
        } 
        else if(curNav != 0)
        {
        	//showDefaultFragment();
        	loadFragment(lastItem);
        }
        else 
        {
        	if((System.currentTimeMillis() - touchTime) < 2000)
			{
				 closeApp();
				 super.onBackPressed();
				// TODO Auto-generated method stub
			}
		    else
			{
				Toast.makeText(getApplicationContext(), "再按一次退出 店音管家", 0).show();
				touchTime = System.currentTimeMillis();
			}
        }
    }

    protected boolean isNavDrawerOpen() 
    {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() 
    {
        if (mDrawerLayout != null) 
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) 
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) 
    {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.

        // Handle action buttons
        switch (item.getItemId()) 
        {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) 
                {
                    startActivity(intent);
                } 
                else 
                {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	private void closeApp()
	{
		if(MinaClient.getInstance().isConnected())
			MinaClient.getInstance().close();
		
		MyActivityManager.getScreenManager().popAllActivityExceptionOne(null);
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
