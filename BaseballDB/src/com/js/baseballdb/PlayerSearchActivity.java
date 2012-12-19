package com.js.baseballdb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class PlayerSearchActivity extends Activity
{
	private InputMethodManager imm ;
	public static EditText txtPlayerSearch; 
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_search_layout);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        
        txtPlayerSearch = (EditText) findViewById(R.id.txtSearchPlayer);
        imm.showSoftInput(txtPlayerSearch, InputMethodManager.SHOW_FORCED);
        
        Button cmdPlayerSearch = (Button) findViewById(R.id.cmdSearchPlayer);
        cmdPlayerSearch.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	String searchPlayerName = txtPlayerSearch.getText().toString();
            	
            	Intent intent = new Intent(PlayerSearchActivity.this, PlayerSearchListActivity.class);
            	intent.putExtra("searchName", searchPlayerName);
            	intent.putExtra("currentTab",3); //PlayerSearch

            	//Å°º¸µå´Â ¼û±è
            	imm.hideSoftInputFromWindow(txtPlayerSearch.getWindowToken(), 0);
            	
                View view = PlayerGroup.playerGroup.getLocalActivityManager()
                	.startActivity("PlayerSearchListActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                	.getDecorView();
                
                // Again, replace the view
                PlayerGroup.playerGroup.replaceView(view);
            }
        });
        
        Button cmdNewSearch = (Button) findViewById(R.id.cmdNewSearchPlayer);
        
        cmdNewSearch.setOnClickListener(new OnClickListener(){
        	public void onClick(View v)
        	{
        		Intent refresh = new Intent(PlayerSearchActivity.this, PlayerSearchActivity.class);
        		 
        		View view = PlayerGroup.playerGroup.getLocalActivityManager()
	         	.startActivity("PlayerSearchActivity", refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
	         	.getDecorView();
	         
        		//PlayerGroup.playerGroup.back();
	             // Again, replace the view
	             PlayerGroup.playerGroup.replaceView(view);
        		 PlayerGroup.playerGroup.removePageForNewSearch();
        		//PlayerSearchActivity.this.finish();
        	}
        });
    }
    
    public void onBackPressed(){
		PlayerGroup.playerGroup.back();
	}
}
