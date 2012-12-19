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

public class TeamGroup extends ActivityGroup 
{
	public static TeamGroup teamGroup;
	private ArrayList<View> teamActivityHistory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.teamActivityHistory = new ArrayList<View>();
		teamGroup = this;

		// Start the root activity withing the group and get its view
		View view = getLocalActivityManager().startActivity("TeamAllActivity", new 
				Intent(this,TeamAllActivity.class)		//TODO 바꾸자.
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();

		// Replace the view of this ActivityGroup
		replaceView(view);

	}

	public void replaceView(View v) 
	{
		// Adds the old one to history
		teamActivityHistory.add(v);
		// Changes this Groups View to the new View.
		setContentView(v);
	}
	
	public void addToHistory(View v)
	{
		teamActivityHistory.add(v);
	}

	public void back() 
	{
		if(teamActivityHistory.size() > 1) 
		{
			teamActivityHistory.remove(teamActivityHistory.size()-1);
			setContentView(teamActivityHistory.get(teamActivityHistory.size()-1));
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
			TeamGroup.teamGroup.back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}