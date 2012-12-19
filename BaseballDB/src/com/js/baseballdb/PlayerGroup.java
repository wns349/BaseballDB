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

public class PlayerGroup extends ActivityGroup 
{
	public static PlayerGroup playerGroup;
	private ArrayList<View> playerActivityHistory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.playerActivityHistory = new ArrayList<View>();
		playerGroup = this;

		// Start the root activity withing the group and get its view
		View view = getLocalActivityManager().startActivity("PlayerSearchActivity", new 
				Intent(this,PlayerSearchActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();

		// Replace the view of this ActivityGroup
		replaceView(view);

	}

	public void replaceView(View v) 
	{
		// Adds the old one to history
		playerActivityHistory.add(v);
		// Changes this Groups View to the new View.
		setContentView(v);
	}

	public void back() 
	{
		if(playerActivityHistory.size() > 1) 
		{
			playerActivityHistory.remove(playerActivityHistory.size()-1);
			setContentView(playerActivityHistory.get(playerActivityHistory.size()-1));
		}
		else 
		{
			askFinish();
		}
	}
	
	public void removePageForNewSearch()
	{
		if(playerActivityHistory.size()>1)
			playerActivityHistory.remove(playerActivityHistory.size()-2);
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
			PlayerGroup.playerGroup.back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public View getPrevView()
	{
		if(playerActivityHistory.size() > 0) 
		{
			return playerActivityHistory.get(playerActivityHistory.size()-1);
		}
		return null;
	}
}