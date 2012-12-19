package com.js.baseballdb;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Top5Activity extends Activity{
	//Global
	public static ArrayList<String> m_top5List = new ArrayList<String>();
	//public static TextView top5_text;
	//public static ListView top5_list;
	private static Button cmdPitcher, cmdHitter;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top5_layout);

		cmdPitcher = (Button) findViewById(R.id.cmdPitcher);
		cmdHitter = (Button) findViewById(R.id.cmdHitter);
		
		cmdPitcher.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(Top5Activity.this, Top5SubListActivity.class);
				intent.putExtra("playerType", CONST.PLAYER_TYPE_PITCHER); //1: pitcher 		0: hitter
				View newView = Top5Group.top5Group.getLocalActivityManager()
				.startActivity("Top5SubListActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();
				// Again, replace the view
				Top5Group.top5Group.replaceView(newView);
			}
		});
		cmdHitter.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(Top5Activity.this, Top5SubListActivity.class);
				intent.putExtra("playerType", CONST.PLAYER_TYPE_HITTER); //1: pitcher 		0: hitter
				View newView = Top5Group.top5Group.getLocalActivityManager()
				.startActivity("Top5SubListActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();
				// Again, replace the view
				Top5Group.top5Group.replaceView(newView);
			}
		});
//		top5_list = (ListView) findViewById(R.id.top5_list);
//		top5_text = (TextView) findViewById(R.id.top5_text);
//
//		top5_text.setText("TOP5 기록실");
//
//		m_top5List.clear();
//		m_top5List.add("투수");
//		m_top5List.add("타자");
//
//		top5_list.setAdapter(new Top5Adapter(this, m_top5List));
//		top5_list.setOnItemClickListener(mTop5ListClickListener);
//		top5_list.setDivider(new ColorDrawable(0xFFFFFF));
//		top5_list.setDividerHeight(5);

	}
//
//	AdapterView.OnItemClickListener mTop5ListClickListener = new AdapterView.OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//		{
//			// TODO Auto-generated method stub
//
//			Intent intent = new Intent(Top5Activity.this, Top5SubListActivity.class);
//
//			intent.putExtra("position", position); //0: pitcher 		1: hitter
//			View newView = Top5Group.top5Group.getLocalActivityManager()
//			.startActivity("Top5SubListActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//			.getDecorView();
//
//			// Again, replace the view
//			Top5Group.top5Group.replaceView(newView);
//		}
//	};
}

