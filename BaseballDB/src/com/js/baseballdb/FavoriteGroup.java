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

public class FavoriteGroup extends ActivityGroup 
{
	public static FavoriteGroup favGroup;
	private ArrayList<View> favActivityHistory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.favActivityHistory = new ArrayList<View>();
		favGroup = this;

		// Start the root activity withing the group and get its view
		View view = getLocalActivityManager().startActivity("FavGroupActivity", new 
				Intent(this,FavoritePlayerActivity.class)		//TODO 바꾸자.
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();

		// Replace the view of this ActivityGroup
		replaceView(view);

	}

	public void replaceView(View v) 
	{
		// Adds the old one to history
		favActivityHistory.add(v);
		// Changes this Groups View to the new View.
		setContentView(v);
	}
	
	public void addToHistory(View v)
	{
		favActivityHistory.add(v);
	}

	public void back() 
	{
		if(favActivityHistory.size() > 1) 
		{
			favActivityHistory.remove(favActivityHistory.size()-1);
			setContentView(favActivityHistory.get(favActivityHistory.size()-1));
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
			FavoriteGroup.favGroup.back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}