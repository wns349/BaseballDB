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

		//ȭ�� ����
		listScreen = (ListView)findViewById(R.id.listScreen);
		listScreen.setAdapter(new SettingAdapter(this, m_settingScreen));
		listScreen.setOnItemClickListener(mScreenItemClickListener);

		//��Ʈ ����
		listFont = (ListView)findViewById(R.id.listFont);
		listFont.setAdapter(new SettingAdapter(this, m_settingFont));
		listFont.setOnItemClickListener(mFontItemClickListener);

	}


	private void createScreenList()
	{
		confScreenOrientation = pref.getString("screen_orientation", "���� ����");
		m_settingScreen.add(new SettingObject("ȭ�� ����", confScreenOrientation));
	}

	private void createFontList()
	{
		confFontSize = pref.getString("fontsize", "����");
		m_settingFont.add(new SettingObject("��Ʈ ũ��", confFontSize));
		
		confFontColor1 = pref.getString("fontcolor1", "����");
		m_settingFont.add(new SettingObject("��Ʈ ����1", confFontColor1));
		
		confFontColor2 = pref.getString("fontcolor2", "�Ķ�");
		m_settingFont.add(new SettingObject("��Ʈ ����2", confFontColor2));
	}

	AdapterView.OnItemClickListener mFontItemClickListener = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// TODO Auto-generated method stub
			switch(position)
			{
			case 0:	//��Ʈ ũ��
				changeFontSize();
				break;
			case 1: // ��Ʈ ����1
				changeFontColor1();
				break;
			case 2: // ��Ʈ ����2
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
			case 0:	//ȭ�� ����
				changeScreenOrientation();
				break;
			}
		}

	};
	
	private void changeFontColor1()
	{
		final CharSequence[] items = {"����", "�Ķ�", "û��", "���", "���", "����", "��ȫ"};

		int index = 0 ;
		for(index = 0; index < items.length; index ++)
		{
			if (((String)items[index]).equalsIgnoreCase(confFontColor1))
				break;
		}
		if(index == items.length)
			index = -1;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("��Ʈ ũ�� ����");
		builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	
		    	SharedPreferences.Editor edt = pref.edit();
		    	edt.putString("fontcolor1", (String) items[item]);
		    	edt.commit();
		        Toast.makeText(getApplicationContext(), "���� �Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
		        
		        //����Ʈ refresh
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
		final CharSequence[] items = {"����", "�Ķ�", "û��", "���", "���", "����", "��ȫ"};

		int index = 0 ;
		for(index = 0; index < items.length; index ++)
		{
			if (((String)items[index]).equalsIgnoreCase(confFontColor2))
				break;
		}
		if(index == items.length)
			index = -1;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("��Ʈ ũ�� ����");
		builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	
		    	SharedPreferences.Editor edt = pref.edit();
		    	edt.putString("fontcolor2", (String) items[item]);
		    	edt.commit();
		        Toast.makeText(getApplicationContext(), "���� �Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
		        
		        //����Ʈ refresh
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
		final CharSequence[] items = {"����", "ũ��", "���� ũ��"};

		int index = 0 ;
		for(index = 0; index < items.length; index ++)
		{
			if (((String)items[index]).equalsIgnoreCase(confFontSize))
				break;
		}
		if(index == items.length)
			index = -1;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("��Ʈ ũ�� ����");
		builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	
		    	SharedPreferences.Editor edt = pref.edit();
		    	edt.putString("fontsize", (String) items[item]);
		    	edt.commit();
		        Toast.makeText(getApplicationContext(), "���� �Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
		        
		        //����Ʈ refresh
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
		final CharSequence[] items = {"���� ����", "���� ����", "���� ����"};

		int index = 0 ;
		for(index = 0; index < items.length; index ++)
		{
			if (((String)items[index]).equalsIgnoreCase(confScreenOrientation))
				break;
		}
		if(index == items.length)
			index = -1;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("ȭ�� ���� ����");
		builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	
		    	SharedPreferences.Editor edt = pref.edit();
		    	edt.putString("screen_orientation", (String) items[item]);
		    	edt.commit();
		        Toast.makeText(getApplicationContext(), "���� �Ǿ����ϴ�. ���α׷��� ������ؾ� ����˴ϴ�.", Toast.LENGTH_SHORT).show();
		        
		        //����Ʈ refresh
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
		
		if(confScreenOrientation.equalsIgnoreCase("���� ����"))
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		else if(confScreenOrientation.equalsIgnoreCase("���� ����"))
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		else
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}
	}
}
