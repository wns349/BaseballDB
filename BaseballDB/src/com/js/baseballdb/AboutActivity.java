package com.js.baseballdb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;

public class AboutActivity extends Activity 
{
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
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

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
