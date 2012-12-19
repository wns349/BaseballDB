package com.js.baseballdb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerSearchListActivity extends Activity 
{
	//Global
	ArrayList<Player> m_playerSearchedList = new ArrayList<Player>();
	private ProgressDialog m_progressDialog;
	protected InitTask m_initTask;
	private String m_playerSearchName;
	private int currentTab;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_layout);

		Intent intent = getIntent();
		m_playerSearchName = intent.getStringExtra("searchName");
		currentTab = intent.getIntExtra("currentTab",-1);
		
		m_progressDialog = ProgressDialog.show(getParent(), "Loading..", "선수 검색하고 있습니다.", true, false);
		m_initTask = new InitTask();
		m_initTask.execute( this );
	}

	private void doAfterLoading()
	{
		if (m_playerSearchedList.size() > 0)
		{
			ListView list = (ListView)findViewById(R.id.list);
			list.setAdapter(new PlayerAdapter(this, R.layout.custom_list , m_playerSearchedList));
			list.setOnItemClickListener(mItemClickListener);
		
			TextView tv = (TextView) findViewById(R.id.tv_not_found);
			tv.setVisibility(View.GONE);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_LONG).show();
			ListView list = (ListView)findViewById(R.id.list);
			TextView tv = (TextView) findViewById(R.id.tv_not_found);

			tv.setText("검색 결과가 없습니다.");

			list.setVisibility(View.GONE);
			tv.setVisibility(View.VISIBLE);
		}
	}

	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// TODO Auto-generated method stub
			//String mes = "Select Item = " + m_teamList.get(position);
			//Toast.makeText(getApplicationContext(), mes , Toast.LENGTH_SHORT).show();

			Player p = m_playerSearchedList.get(position);

			Intent intent = new Intent(PlayerSearchListActivity.this, PlayerStatActivity.class);
			Bundle extra = new Bundle();
			extra.putSerializable("player", (Serializable) p);
			intent.putExtras(extra);
			intent.putExtra("currentTab", currentTab); // 3: Player Search Tab
			intent.putExtra("currentView", 1);	// 0: 팀 검색   1: 플레이어 검색
			
			View newView;
			// Again, replace the view
			switch(currentTab)
			{
			case 0: //Fav
				newView = FavoriteGroup.favGroup.getLocalActivityManager()
				.startActivity("PlayerStatActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();
				FavoriteGroup.favGroup.replaceView(newView);
				break;
			case 1:	//Team
				break;
			case 2: //Top5
				newView = Top5Group.top5Group.getLocalActivityManager()
				.startActivity("PlayerStatActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();
				Top5Group.top5Group.replaceView(newView);
				break;
			case 3: //Player
				newView = PlayerGroup.playerGroup.getLocalActivityManager()
				.startActivity("PlayerStatActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();
				PlayerGroup.playerGroup.replaceView(newView);
				break;
			default:	//Do nothing
			}
		}

	};

	// teamList 에 데이터를 넣는다. 
	private void createList(String searchName)
	{
		String thisLine = "";
		String playerID = "0";
		String playerNum = "0";
		String playerName="이름 없음", playerPos="투수";
		String playerTeam="";
		int playerType=CONST.PLAYER_TYPE_HITTER;

		try
		{
			searchName = URLEncoder.encode(searchName, "utf-8");
			String addr = "http://www.koreabaseball.com/Record/SearchResult.aspx?PLAYER_S_WORD="+searchName;	//접속 주소

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
					while (true)
					{
						thisLine = br.readLine();

						// EOF
						if (thisLine == null)
							break;

						// Data Found
						if (thisLine.contains("/Record/HitterDetail1.aspx?pcode=") || thisLine.contains("/Record/PitcherDetail1.aspx?pcode="))
						{
							if (thisLine.contains("/Record/HitterDetail1.aspx?pcode="))
								playerType = CONST.PLAYER_TYPE_HITTER;
							else
								playerType = CONST.PLAYER_TYPE_PITCHER;
							
							// 선수 ID
							Pattern pattern = Pattern.compile("pcode=\\d*\"");
							Matcher matcher = pattern.matcher(thisLine);
							if (matcher.find())
							{
								temp = matcher.group();
								temp = temp.substring(6, temp.length()-1);

								playerID = temp;
							}

							// 선수 이름
							pattern = Pattern.compile(">\\S*</a");
							matcher = pattern.matcher(thisLine);
							if (matcher.find())
							{
								temp = matcher.group();
								playerName = temp.substring(1, temp.length()-3);
							}

							// 선수 등번호
							thisLine = br.readLine();
							thisLine = thisLine.replace("&nbsp;", "");
							pattern = Pattern.compile(">\\d*</");
							matcher = pattern.matcher(thisLine);
							if (matcher.find())
							{
								temp = matcher.group();
								temp = temp.substring(1,temp.length()-2);

								playerNum = temp;
							}

							// 선수 포지션
							thisLine = br.readLine();
							thisLine = thisLine.replace("&nbsp;", "");
							pattern = Pattern.compile(">\\S*</");
							matcher = pattern.matcher(thisLine);
							if (matcher.find())
							{
								temp = matcher.group();
								playerPos = temp.substring(1, temp.length()-2);
							}

							// 선수 소속 팀
							thisLine = br.readLine();
							thisLine = thisLine.replace("&nbsp;", "");
							pattern = Pattern.compile(">\\S*</");
							matcher = pattern.matcher(thisLine);
							if (matcher.find())
							{
								temp = matcher.group();
								playerTeam = temp.substring(1, temp.length()-2);
							}

							//String combine = playerTeam + "("+playerPos+")";

							//팀에 있는 선수만 리스트에 추가한다
							if (playerTeam.length()>1)
								m_playerSearchedList.add(new Player(playerID, "No."+playerNum, playerName, playerPos.charAt(0)+"", playerTeam, playerType));
						}
					}
					br.close();
				}
				conn.disconnect();
			}
		}
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected class InitTask extends AsyncTask<Context, Integer, String>
	{

		@Override
		protected String doInBackground(Context... arg0) {
			// TODO Auto-generated method stub
			createList(m_playerSearchName);
			return null;
		}
		@Override
		protected void onPostExecute( String result ) 
		{
			super.onPostExecute(result);
			m_progressDialog.dismiss();
			doAfterLoading();
		}
	}
}
