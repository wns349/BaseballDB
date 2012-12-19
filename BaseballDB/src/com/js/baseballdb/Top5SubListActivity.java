package com.js.baseballdb;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class Top5SubListActivity extends Activity{
	
	public static ArrayList<String> m_top5SubList = new ArrayList<String>();
	public static TableLayout top5_subLayout;
	public static TextView top5_subText;
	public static int m_position;
	
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top5_sublayout);
        
        top5_subLayout = (TableLayout) findViewById(R.id.top5_sublayout_button);
        top5_subText = (TextView) findViewById(R.id.top5_subtext);
        
        Intent intent = getIntent();
        m_position = intent.getIntExtra("playerType",CONST.PLAYER_TYPE_HITTER);
        
        m_top5SubList.clear();
        
        

        if (m_position == CONST.PLAYER_TYPE_PITCHER)
        {
        	top5_subText.setText("TOP5 투수 기록실");
            String[] str_list = {"평균자책점 TOP 5", "승리 TOP 5", "save TOP 5", "탈삼진 TOP 5", "홀드 TOP 5", "승률 TOP 5"};
            createButton(str_list);
        }
        else
        {
        	top5_subText.setText("TOP5 타자 기록실");
        	String[] str_list = {"타율 TOP 5","홈런 TOP 5","타점 TOP 5","득점 TOP 5","안타 TOP 5","출루율 TOP 5","장타율 TOP 5", "도루 TOP 5"};
        	createButton(str_list);
        }
    }
    private void createButton(String[] str_list)
    {
        top5_subLayout.setStretchAllColumns(true);
    	for(int i=0;i<str_list.length;i = i+2)
        {
        	TableRow tr = new TableRow(this);
        	Button bt = new Button(this);
        	bt.setPadding(3, 0, 3, 0);
        	bt.setText(str_list[i]);
        	bt.setGravity(17);
        	//bt.setLayoutParams(params);
        	bt.setOnClickListener(mButtonListener);
        	tr.addView(bt);
        	
        	if(i+1 <= str_list.length)
        	{
            	Button bt1 = new Button(this);
            	bt1.setPadding(3, 0, 3, 0);
            	bt1.setText(str_list[i+1]);
            	bt1.setGravity(17);
            	//bt1.setLayoutParams(params);
            	bt1.setOnClickListener(mButtonListener);
            	tr.addView(bt1);
        	}
        	
        	top5_subLayout.addView(tr);
        }
    }
    
    Button.OnClickListener mButtonListener = new Button.OnClickListener(){
		public void onClick(View v){
			// TODO Auto-generated method stub
			CharSequence p = ((Button)v).getText();

			Intent intent = new Intent(Top5SubListActivity.this, Top5StatActivity.class);
			intent.putExtra("filter", p);
			intent.putExtra("playerType", m_position); //1: pitcher 		0: hitter
			View newView = Top5Group.top5Group.getLocalActivityManager()
			.startActivity("Top5StatActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
			.getDecorView();

			// Again, replace the view
			Top5Group.top5Group.replaceView(newView);
		}
    };
    
    public void onBackPressed(){
		Top5Group.top5Group.back();
	}
}
