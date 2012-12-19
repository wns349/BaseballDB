package com.js.baseballdb;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

public class Main extends TabActivity  {

	public TabHost tabHost;
	public boolean isConnected = false;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		//NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		//NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo ni = manager.getActiveNetworkInfo();
		if(ni!= null && ni.isConnected())
		{
			// 어디 한군데라도 연결되어 있는 경우
			isConnected = true;
		} 
		else
		{ 
			// 아무 네트워크에도 연결안되어 있는 경우
			Toast.makeText(getApplicationContext(), "3G/Wi-fi 에 접속 할 수 없습니다. 다시 실행해 주세요.", Toast.LENGTH_LONG).show();
			isConnected = false;
		}
		
		//화면 고정 여부 (세팅)
		SharedPreferences  pref = getSharedPreferences("KBO_2012", MODE_PRIVATE);
    	String confOrientation = pref.getString("screen_orientation","고정 안함");
    	
    	if (confOrientation.equalsIgnoreCase("가로 고정")){
    		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	}
    	else if (confOrientation.equalsIgnoreCase("세로 고정")){
    		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	}
    	else {
    		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    	}

		Resources res = getResources(); // Resource object to get Drawables
		this.tabHost = getTabHost(); 
		TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, FavoriteGroup.class);
		spec = tabHost.newTabSpec("FavGroup").setIndicator("즐겨찾기",
				res.getDrawable(R.drawable.star1))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, TeamGroup.class);
		spec = tabHost.newTabSpec("TeamGroup").setIndicator("팀 순위",
				res.getDrawable(R.drawable.team1))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, Top5Group.class); 
		spec = tabHost.newTabSpec("TopGroup").setIndicator("Top5",
				res.getDrawable(R.drawable.top5))
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, PlayerGroup.class); 
		spec = tabHost.newTabSpec("PlayerGroup").setIndicator("선수 검색",
				res.getDrawable(R.drawable.player1))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, AboutActivity.class);
		spec = tabHost.newTabSpec("About").setIndicator("About",
				res.getDrawable(R.drawable.about1))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

		//연결이 안�으면 정보창만 보여준다.
		if(!isConnected)
		{
			tabHost.clearAllTabs();

			intent = new Intent().setClass(this, AboutActivity.class);
			spec = tabHost.newTabSpec("About").setIndicator("About",
					res.getDrawable(R.drawable.about1))
					.setContent(intent);
			tabHost.addTab(spec);

			tabHost.setCurrentTab(0);
		}


	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add( 0, 1, 0, "설정" ).setIcon(android.R.drawable.ic_menu_edit);
		menu.add(0,2,0, "종료").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case 1: 	// 설정 옵션
			Intent intent = new Intent().setClass(this, SettingActivity.class);
			startActivity(intent);
			break;
		case 2:	//종료
			askFinish();
			break;
		}
		return true;
	}
	
	// 화면 가로/세로 변경시 새로 onCreate 하지 않게 override.
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			int tabIndex = getTabHost().getCurrentTab();
			if (isConnected)
			{
				switch(tabIndex)
				{
					case 0:	//Fav
						FavoriteGroup.favGroup.back();
						break;
					case 1: //Team
						TeamGroup.teamGroup.back();
						break;
					case 2: //Top5
						Top5Group.top5Group.back();
						break;
					case 3: //Player Search
						PlayerGroup.playerGroup.back();
						break;
					case 4:	// About
						askFinish();
						break;
				}
			}
			else	//No internet!
			{
				askFinish();
			}
			
			//Toast.makeText(getApplicationContext(), tabTag.toString(), Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void askFinish()
	{
		AlertDialog.Builder ad=new AlertDialog.Builder(this);
		ad.setTitle("종료");
		ad.setMessage("종료하시겠습니까?");
		ad.setPositiveButton("Yes", new OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ad.setNegativeButton("No",new OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub		
				
			}
		});
		ad.show();
	}
}