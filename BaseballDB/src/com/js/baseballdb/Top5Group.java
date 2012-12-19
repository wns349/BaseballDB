package com.js.baseballdb;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class Top5Group extends ActivityGroup 
{
	public static Top5Group top5Group;
	private ArrayList<View> top5ActivityHistory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.top5ActivityHistory = new ArrayList<View>();
		top5Group = this;

		// Start the root activity withing the group and get its view
		View view = getLocalActivityManager().startActivity("Top5Activity", new 
				Intent(this,Top5Activity.class)		//TODO 바꾸자.
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();

		// Replace the view of this ActivityGroup
		replaceView(view);

	}

	public void replaceView(View v) 
	{
		// Adds the old one to history
		top5ActivityHistory.add(v);
		// Changes this Groups View to the new View.
		setContentView(v);
	}
	
	public void addToHistory(View v)
	{
		top5ActivityHistory.add(v);
	}

	public void back() 
	{
		if(top5ActivityHistory.size() > 1) 
		{
			top5ActivityHistory.remove(top5ActivityHistory.size()-1);
			setContentView(top5ActivityHistory.get(top5ActivityHistory.size()-1));
		}
		else 
		{
			askFinish();
		}
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			Top5Group.top5Group.back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}