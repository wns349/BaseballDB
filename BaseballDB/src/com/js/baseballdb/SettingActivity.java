package com.js.baseballdb;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class SettingActivity extends Activity
{
	private ArrayList<SettingObject> m_settingScreen;
	private ArrayList<SettingObject> m_settingFont;
	private SharedPreferences pref;
	private String confFontSize, confScreenOrientation;
	private String confFontColor1, confFontColor2;
	private ListView listFont, listScreen;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);

		m_settingScreen = new ArrayList<SettingObject>();
		m_settingFont = new ArrayList<SettingObject>();
		pref = getSharedPreferences("KBO_2012", MODE_PRIVATE);
		
		createScreenList();
		createFontList();

		//화면 설정
		listScreen = (ListView)findViewById(R.id.listScreen);
		listScreen.setAdapter(new SettingAdapter(this, m_settingScreen));
		listScreen.setOnItemClickListener(mScreenItemClickListener);

		//폰트 설정
		listFont = (ListView)findViewById(R.id.listFont);
		listFont.setAdapter(new SettingAdapter(this, m_settingFont));
		listFont.setOnItemClickListener(mFontItemClickListener);

	}


	private void createScreenList()
	{
		confScreenOrientation = pref.getString("screen_orientation", "고정 안함");
		m_settingScreen.add(new SettingObject("화면 고정", confScreenOrientation));
	}

	private void createFontList()
	{
		confFontSize = pref.getString("fontsize", "보통");
		m_settingFont.add(new SettingObject("폰트 크기", confFontSize));
		
		confFontColor1 = pref.getString("fontcolor1", "검정");
		m_settingFont.add(new SettingObject("폰트 색상1", confFontColor1));
		
		confFontColor2 = pref.getString("fontcolor2", "파랑");
		m_settingFont.add(new SettingObject("폰트 색상2", confFontColor2));
	}

	AdapterView.OnItemClickListener mFontItemClickListener = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// TODO Auto-generated method stub
			switch(position)
			{
			case 0:	//폰트 크기
				changeFontSize();
				break;
			case 1: // 폰트 색상1
				changeFontColor1();
				break;
			case 2: // 폰트 색상2
				changeFontColor2();
				break;
			}
		}

	};
	
	AdapterView.OnItemClickListener mScreenItemClickListener = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// TODO Auto-generated method stub
			switch(position)
			{
			case 0:	//화면 고정
				changeScreenOrientation();
				break;
			}
		}

	};
	
	private void changeFontColor1()
	{
		final CharSequence[] items = {"검정", "파랑", "청록", "녹색", "노랑", "빨강", "자홍"};

		int index = 0 ;
		for(index = 0; index < items.length; index ++)
		{
			if (((String)items[index]).equalsIgnoreCase(confFontColor1))
				break;
		}
		if(index == items.length)
			index = -1;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("폰트 크기 설정");
		builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	
		    	SharedPreferences.Editor edt = pref.edit();
		    	edt.putString("fontcolor1", (String) items[item]);
		    	edt.commit();
		        Toast.makeText(getApplicationContext(), "저장 되었습니다.", Toast.LENGTH_SHORT).show();
		        
		        //리스트 refresh
		        m_settingFont.get(1).setValue((String) items[item]);
		        refreshFont();
		        confFontColor1 = (String) items[item];
		        
		        dialog.dismiss();
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void changeFontColor2()
	{
		final CharSequence[] items = {"검정", "파랑", "청록", "녹색", "노랑", "빨강", "자홍"};

		int index = 0 ;
		for(index = 0; index < items.length; index ++)
		{
			if (((String)items[index]).equalsIgnoreCase(confFontColor2))
				break;
		}
		if(index == items.length)
			index = -1;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("폰트 크기 설정");
		builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	
		    	SharedPreferences.Editor edt = pref.edit();
		    	edt.putString("fontcolor2", (String) items[item]);
		    	edt.commit();
		        Toast.makeText(getApplicationContext(), "저장 되었습니다.", Toast.LENGTH_SHORT).show();
		        
		        //리스트 refresh
		        m_settingFont.get(2).setValue((String) items[item]);
		        refreshFont();
		        confFontColor2 = (String) items[item];
		        
		        dialog.dismiss();
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void changeFontSize()
	{
		final CharSequence[] items = {"보통", "크게", "아주 크게"};

		int index = 0 ;
		for(index = 0; index < items.length; index ++)
		{
			if (((String)items[index]).equalsIgnoreCase(confFontSize))
				break;
		}
		if(index == items.length)
			index = -1;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("폰트 크기 설정");
		builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	
		    	SharedPreferences.Editor edt = pref.edit();
		    	edt.putString("fontsize", (String) items[item]);
		    	edt.commit();
		        Toast.makeText(getApplicationContext(), "저장 되었습니다.", Toast.LENGTH_SHORT).show();
		        
		        //리스트 refresh
		        m_settingFont.get(0).setValue((String) items[item]);
		        refreshFont();
		        confFontSize = (String) items[item];
		        
		        dialog.dismiss();
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void changeScreenOrientation()
	{
		final CharSequence[] items = {"가로 고정", "세로 고정", "고정 안함"};

		int index = 0 ;
		for(index = 0; index < items.length; index ++)
		{
			if (((String)items[index]).equalsIgnoreCase(confScreenOrientation))
				break;
		}
		if(index == items.length)
			index = -1;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("화면 고정 설정");
		builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	
		    	SharedPreferences.Editor edt = pref.edit();
		    	edt.putString("screen_orientation", (String) items[item]);
		    	edt.commit();
		        Toast.makeText(getApplicationContext(), "저장 되었습니다. 프로그램을 재시작해야 적용됩니다.", Toast.LENGTH_SHORT).show();
		        
		        //리스트 refresh
		        m_settingScreen.get(0).setValue((String) items[item]);
		        refreshScreen();
		        confScreenOrientation = (String) items[item];
		        
		        dialog.dismiss();
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void refreshFont()
	{
		listFont.setAdapter(new SettingAdapter(this, m_settingFont));
	}
	
	private void refreshScreen()
	{
		listScreen.setAdapter(new SettingAdapter(this, m_settingScreen));
		
		if(confScreenOrientation.equalsIgnoreCase("가로 고정"))
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		else if(confScreenOrientation.equalsIgnoreCase("세로 고정"))
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		else
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}
	}
}
