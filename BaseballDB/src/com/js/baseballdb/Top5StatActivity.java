package com.js.baseballdb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Top5StatActivity extends Activity{

	private static TextView  tv_head;
	private static ListView top5_stat_list;
	private static ImageView img;
	private static int m_playerType;
	private static String m_filter, m_img;
	private static ArrayList<String> m_top5PlayerList = new ArrayList<String>();
	private static ArrayList<String> m_top5PlayerURLList = new ArrayList<String>();

	private ProgressDialog m_progressDialog;
	protected InitTask m_initTask;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top5_stat_layout);

		Intent intent = getIntent();
		m_filter = intent.getStringExtra("filter");
		m_playerType = intent.getIntExtra("playerType", CONST.PLAYER_TYPE_HITTER);

		top5_stat_list = (ListView) findViewById(R.id.top5_stat_list);
		img = (ImageView)findViewById(R.id.top5_image);
		tv_head = (TextView)findViewById(R.id.top5_stat_text);
		
		m_initTask = new InitTask();
		m_initTask.execute( this );

	}
	// 사진 불러오기 위한 메소드
    private Drawable LoadImageFromWebOperations(String url)
    {
    	try
    	{
    		InputStream is = (InputStream) new URL(url).getContent();
    		Drawable d = Drawable.createFromStream(is, "src name");
    		return d;
    	}catch (Exception e) {
    		return null;
    	}
    }

	private void createList()
	{
		m_top5PlayerList.clear();
		m_top5PlayerURLList.clear();
		String addr = "";
		if (m_playerType == CONST.PLAYER_TYPE_PITCHER)// 투수
		{
			addr = "http://www.koreabaseball.com/Record/PitcherTop5.aspx";
		}
		else
		{
			addr = "http://www.koreabaseball.com/Record/HitterTop5.aspx";
		}

		m_filter = m_filter.replace(" ", ""); // Get rid of spaces
		//m_filter = "\""+m_filter+"\"";	// "___TOP5"  format
		try {
			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if( conn != null)
			{
				conn.setConnectTimeout(10000);
				conn.setUseCaches(false);
				if( conn.getResponseCode() == HttpURLConnection.HTTP_OK)
				{
					BufferedReader br = new BufferedReader( new InputStreamReader(conn.getInputStream(), "utf-8"));
					String temp = "";
					String thisLine="";
					boolean stopSearch = false;

					while (true)
					{
						thisLine = br.readLine();

						// EOF
						if (thisLine == null || stopSearch)
							break;

						if (thisLine.contains(m_filter.substring(0, m_filter.length()-4)))	//Remove TOP5
						{
							while(!thisLine.contains("</div>"))
							{
								if (thisLine.contains("FILE/person/"))
								{
									m_img = thisLine.substring(thisLine.indexOf("\""), thisLine.length());
									m_img = m_img.substring(1, m_img.length());
									m_img = m_img.substring(0, m_img.indexOf("\""));
									m_img = "http://www.koreabaseball.com"+m_img;
								}
								if(thisLine.contains("\"rank"))
								{
									temp = br.readLine();
									String player_url = temp.substring(temp.indexOf("pcode="), temp.length());
									player_url = player_url.substring(6, player_url.length());
									player_url = player_url.substring(0, player_url.indexOf("\""));// 선수 아이디	
									
									temp = br.readLine();
									String player_name = temp.replace("&nbsp;", "");
									player_name = player_name.trim();
									temp = br.readLine();
									temp = br.readLine();
									temp = temp.trim();
									temp = temp.replace("<span>", "\t");
									temp = temp.replace("</span>", "");
									
									m_top5PlayerList.add(player_name+"\t"+temp);
									m_top5PlayerURLList.add(player_url);
								}
								thisLine = br.readLine();
							}
							stopSearch = true;
						}
					}
					br.close();
				}
				conn.disconnect();
			} 
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
		}
	}
	
	private void doAfterLoading(){

		tv_head.setText(m_filter);
		
		top5_stat_list.setAdapter(new SimpleAdapter(this, m_top5PlayerList));
    	top5_stat_list.setOnItemClickListener(mItemClickListener);;
    	
		Drawable drawable = LoadImageFromWebOperations(m_img);
    	if (drawable != null)
    	{
    		img.setImageDrawable(drawable);
    	}
    	else
    	{
    		img.setImageResource(R.drawable.nopic);
    	}
	}
	
	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// TODO Auto-generated method stub
			String pid = m_top5PlayerURLList.get(position);
			Player player = new Player(pid);
			player.setType(m_playerType);
			
			Intent intent = new Intent(Top5StatActivity.this, PlayerStatActivity.class);
			Bundle extra = new Bundle();
			extra.putSerializable("player", (Serializable) player);
			intent.putExtras(extra);
			intent.putExtra("currentTab", 2); // 2: Top5
			intent.putExtra("currentView", 1);	// 0: 팀 검색   1: 플레이어 검색
			View newView = Top5Group.top5Group.getLocalActivityManager()
			.startActivity("PlayerStatActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
			.getDecorView();

			// Again, replace the view
			Top5Group.top5Group.replaceView(newView);
		}

	};
	
	protected class InitTask extends AsyncTask<Context, Integer, String>
	{
		protected void onPreExecute(){
			m_progressDialog = ProgressDialog.show(getParent(), "Loading..", "순위를 로딩하고 있습니다.", true, false);
		}
		@Override
		protected String doInBackground(Context... arg0) {
			// TODO Auto-generated method stub
			createList();
			return null;
		}
		@Override
		protected void onPostExecute( String result ) 
		{
			super.onPostExecute(result);
			m_progressDialog.dismiss();
			doAfterLoading();
		}

		protected void onCancelled() 
		{ 
			super.onCancelled(); 
		}
	}
	
	public void onBackPressed(){
		Top5Group.top5Group.back();
	}
}
