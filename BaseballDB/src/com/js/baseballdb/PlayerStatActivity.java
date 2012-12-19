package com.js.baseballdb;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerStatActivity extends Activity
{
	private Player mPlayer;

	private ArrayList<ValuePair> playerBasicInfo = new ArrayList<ValuePair>();	// 선수 기본 정보.
	private ArrayList<ValuePair> playerThisYearInfo = new ArrayList<ValuePair>(); 	// 선수 올해 성적.
	private ArrayList<ValuePair> playerRecentGamesInfo = new ArrayList<ValuePair>();	// 최근 5경기
	private ArrayList<ValuePair> playerHistoryInfo	= new ArrayList<ValuePair>();	// 총 성적
	private int playerRecentGamesInfoAttrSize = 0;
	private int playerHistoryInfoAttrSize = 0;

	private ProgressDialog m_progressDialog;
	protected InitTask m_initTask;

	private EditText txtPlayerSearch;
	private Button cmdPlayerSearch;
	private InputMethodManager imm;

	private int currentTab = 0; //현재 Tab 0: Fav, 1: Team..

	private int[] fontSize;

	private int[] fontColor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_stat_layout);

		Intent intent = getIntent();
		mPlayer = (Player) getIntent().getExtras().get("player");
		currentTab = intent.getIntExtra("currentTab", 0);

		initSettings();
		initButtons();

		m_progressDialog = ProgressDialog.show(getParent(), "Loading..", "선수 로딩하고 있습니다.", true, false);
		m_initTask = new InitTask();
		m_initTask.execute( this );
	}

	private void initButtons(){
		//즐겨찾기
		final Button button = (Button) findViewById(R.id.cmdFav);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				for(int i=0; i < FavoriteList.arrFavorite.size(); i++)
				{
					//Already on List
					if (FavoriteList.convToString().contains(mPlayer.getID()))
					{
						Toast.makeText(getApplicationContext(), "이미 등록된 선수 입니다.", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				saveToPreference();
				Toast.makeText(getApplicationContext(), "등록 되었습니다.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private int convDPtoPixel(float dp){
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		return (int) (metrics.density * dp + 0.5f);
	}

	private void initSettings(){
		//폰트
		SharedPreferences  pref = getSharedPreferences("KBO_2012", MODE_PRIVATE);
		String confFontSize = pref.getString("fontsize", "보통");

		// 		0: 이름 출력 사이즈, 1: 백넘버, 포지션, 2: 정보캡션, 3: 정보
		fontSize = new int[4];

		if ( confFontSize.equalsIgnoreCase("크게"))
		{
			fontSize[0] = convDPtoPixel(22);			
			fontSize[1] = convDPtoPixel(18);
			fontSize[2] = convDPtoPixel(15);
			fontSize[3] = convDPtoPixel(15);

		}
		else if ( confFontSize.equalsIgnoreCase("아주 크게"))
		{
			fontSize[0] = convDPtoPixel(26);
			fontSize[1] = convDPtoPixel(20);
			fontSize[2] = convDPtoPixel(18);
			fontSize[3] = convDPtoPixel(18);
		}
		else
		{
			fontSize[0] = convDPtoPixel(20);
			fontSize[1] = convDPtoPixel(15);
			fontSize[2] = convDPtoPixel(12);
			fontSize[3] = convDPtoPixel(12);
		}

		//	0: 첫번째 	1: 두번째 색상
		fontColor = new int[2];
		String confFontColor1 = pref.getString("fontcolor1", "검정");
		String confFontColor2 = pref.getString("fontcolor2", "파랑");

		fontColor[0] = setFontColor(confFontColor1);
		fontColor[1] = setFontColor(confFontColor2);
	}

	private void setNewSearch()
	{
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

		txtPlayerSearch = (EditText) findViewById(R.id.txtSearchPlayerStat);
		txtPlayerSearch.setOnKeyListener(new OnKeyListener()
		{
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (event.getAction() == KeyEvent.ACTION_DOWN)
				{
					//check if the right key was pressed
					if (keyCode == KeyEvent.KEYCODE_BACK)
					{
						switch(currentTab)
						{
						case 0: //Fav
							FavoriteGroup.favGroup.back();
							break;
						case 1:	//Team
							break;
						case 2: //Top5
							Top5Group.top5Group.back();
							break;
						case 3: //Player
							PlayerGroup.playerGroup.back();
							break;
						default:	//Do nothing
						}
						return true;
					}
				}
				return false;
			}
		});
		imm.showSoftInput(txtPlayerSearch, InputMethodManager.SHOW_FORCED);

		cmdPlayerSearch = (Button) findViewById(R.id.cmdSearchPlayerStat);
		cmdPlayerSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				String searchPlayerName = txtPlayerSearch.getText().toString();

				Intent intent = new Intent(PlayerStatActivity.this, PlayerSearchListActivity.class);
				intent.putExtra("searchName", searchPlayerName);
				intent.putExtra("currentTab", currentTab);
				//키보드는 숨김
				imm.hideSoftInputFromWindow(txtPlayerSearch.getWindowToken(), 0);

				View view;
				// Again, replace the view
				switch(currentTab)
				{
				case 0: //Fav
					view = FavoriteGroup.favGroup.getLocalActivityManager()
					.startActivity("PlayerSearchListActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView();
					FavoriteGroup.favGroup.replaceView(view);
					break;
				case 1:	//Team
					break;
				case 2: //Top5
					view = Top5Group.top5Group.getLocalActivityManager()
					.startActivity("PlayerSearchListActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView();
					Top5Group.top5Group.replaceView(view);
					break;
				case 3: //Player
					view = PlayerGroup.playerGroup.getLocalActivityManager()
					.startActivity("PlayerSearchListActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView();
					PlayerGroup.playerGroup.replaceView(view);
					break;
				default:	//Do nothing
				}
			}
		});
	}

	private void doAfterLoading()
	{
		ImageView imgPlayer = (ImageView) findViewById(R.id.ps_image);

		// 사진
		Drawable drawable = LoadImageFromWebOperations("http://www.koreabaseball.com/file/person/middle/"+mPlayer.getID()+".jpg");
		if (drawable != null)
		{
			imgPlayer.setImageDrawable(drawable);
		}
		else
		{
			imgPlayer.setImageResource(R.drawable.nopic);
		}

		this.buildBasicLayout();	// 선수 기본 정보를 렌더한다.
		if (playerThisYearInfo.size() <= 0)	// 없으면 보여주지도 말자...
		{
			LinearLayout ll = (LinearLayout) findViewById(R.id.ps_this_year_layout);
			ll.setVisibility(View.GONE);
			ll = (LinearLayout) findViewById(R.id.ps_this_year_title_layout);
			ll.setVisibility(View.GONE);
		}

		if (playerRecentGamesInfo.size() <= 0)
		{
			LinearLayout ll = (LinearLayout) findViewById(R.id.ps_recent_layout);
			ll.setVisibility(View.GONE);
			ll = (LinearLayout) findViewById(R.id.ps_recent_title_layout);
			ll.setVisibility(View.GONE);
		}
		else
		{
			this.buildRecentLayout();
		}

		if (playerHistoryInfo.size() <= 0)
		{
			LinearLayout ll = (LinearLayout) findViewById(R.id.ps_history_layout);
			ll.setVisibility(View.GONE);
			ll = (LinearLayout) findViewById(R.id.ps_history_title_layout);
			ll.setVisibility(View.GONE);
		}
		else
		{
			this.buildHistoryLayout();
		}

		setNewSearch();
	}


	private int setFontColor(String color)
	{
		if (color.equalsIgnoreCase("검정"))
			return Color.BLACK;
		else if (color.equalsIgnoreCase("파랑"))
			return Color.BLUE;
		else if (color.equalsIgnoreCase("청록"))
			return Color.CYAN;
		else if (color.equalsIgnoreCase("녹색"))
			return Color.GREEN;
		else if (color.equalsIgnoreCase("노랑"))
			return Color.YELLOW;
		else if (color.equalsIgnoreCase("빨강"))
			return Color.RED;
		else if (color.equalsIgnoreCase("자홍"))
			return Color.MAGENTA;
		else
			return Color.BLACK;
	}

	private void saveToPreference()
	{
		SharedPreferences pref = getSharedPreferences("KBO_2012", MODE_PRIVATE);
		SharedPreferences.Editor edt = pref.edit();

		FavoriteList.arrFavorite.add(mPlayer);

		String rtn = FavoriteList.convToString();
		edt.putString("str", rtn);
		edt.commit();

		FavoritePlayerActivity.m_playerList.add(mPlayer);
		FavoritePlayerActivity.sortList();	//Sort list
		FavoritePlayerActivity.list.setAdapter(new PlayerAdapter(this, R.layout.custom_list, FavoritePlayerActivity.m_playerList));
		FavoritePlayerActivity.addedFromStat();
	}

	private void buildHistoryLayout()
	{
		TableLayout player_history = (TableLayout) findViewById(R.id.ps_history_table);
		TableLayout player_history_attr = (TableLayout) findViewById(R.id.ps_history_attr_table);

		TextView tvHistory = (TextView) findViewById(R.id.tvPlayerHistory);
		tvHistory.setTextSize(fontSize[2]);

		int playerHistoryGamesInfoLength = playerHistoryInfo.size();

		//Attr 먼저 추가
		for(int i=0; i<playerHistoryInfoAttrSize; i ++)
		{
			TableRow tr = new TableRow(this);
			TextView tv = new TextView(this);
			tv.setPadding(3, 0, 3, 0);
			tv.setText(playerHistoryInfo.get(i%playerHistoryInfoAttrSize).getKey());
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(fontSize[3]);
			tv.setGravity(5);
			tr.addView(tv);
			player_history_attr.addView(tr);
		}

		// 데이터
		for(int i=0; i < playerHistoryInfoAttrSize; i++)
		{
			TableRow tr = new TableRow(this);

			for(int j=0; j < (playerHistoryGamesInfoLength/playerHistoryInfoAttrSize); j++)
			{
				TextView tv = new TextView(this);
				tv.setPadding(3, 0, 3, 0);
				tv.setText(playerHistoryInfo.get(i + j * playerHistoryInfoAttrSize).getValue());
				tv.setTextSize(fontSize[3]);
				if ( j % 2 == 1)
					tv.setTextColor(fontColor[0]);
				else
					tv.setTextColor(fontColor[1]);
				tv.setGravity(5);
				tr.addView(tv);
			}

			player_history.addView(tr);
		}
	}

	private void buildRecentLayout()
	{
		TableLayout player_recent = (TableLayout) findViewById(R.id.ps_recent_table);
		TableLayout player_recent_attr = (TableLayout) findViewById(R.id.ps_recent_attr_table);

		TextView tvRecent = (TextView) findViewById(R.id.tvPlayerRecent);
		tvRecent.setTextSize(fontSize[2]);

		int playerRecentGamesInfoLength = playerRecentGamesInfo.size();
		//Attr 먼저 추가
		for(int i=0; i<playerRecentGamesInfoAttrSize; i ++)
		{
			TableRow tr = new TableRow(this);
			TextView tv = new TextView(this);
			tv.setPadding(3, 0, 3, 0);
			tv.setText(playerRecentGamesInfo.get(i%playerRecentGamesInfoAttrSize).getKey());
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(fontSize[3]);
			tv.setGravity(5);
			tr.addView(tv);
			player_recent_attr.addView(tr);
		}

		// 데이터
		for(int i=0; i < playerRecentGamesInfoAttrSize; i++)
		{
			TableRow tr = new TableRow(this);

			for(int j=0; j < (playerRecentGamesInfoLength/playerRecentGamesInfoAttrSize); j++)
			{
				TextView tv = new TextView(this);
				tv.setPadding(3, 0, 3, 0);
				tv.setText(playerRecentGamesInfo.get(i + j * playerRecentGamesInfoAttrSize).getValue());
				tv.setTextSize(fontSize[3]);
				if ( j % 2 == 1)
					tv.setTextColor(fontColor[0]);
				else
					tv.setTextColor(fontColor[1]);
				tv.setGravity(5);
				tr.addView(tv);
			}

			player_recent.addView(tr);
		}
	}

	private void updatePlayerInfo(){
		for(ValuePair v: playerBasicInfo){
			if (v.getKey().equals("선수명")){
				mPlayer.setName(v.getValue());	//Update just in case
			}
			else if (v.getKey().equals("포지션")){
				mPlayer.setPos(v.getValue());
			}
			else if (v.getKey().equals("등번호")){
				mPlayer.setNum(v.getValue());
			}
		}
		if(playerHistoryInfo.size()>0){
			//통산 기록에서 제일 마지막 년도 팀을 가져온다
			String team = playerHistoryInfo.get(playerHistoryInfo.size()-playerHistoryInfoAttrSize +1).getValue();
			mPlayer.setTeam(team);
		}
	}

	private void buildBasicLayout()
	{
		//레이아웃 시작
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvPos = (TextView) findViewById(R.id.tvPosition);
		TextView tvBirth = (TextView) findViewById(R.id.tvNumber);
		TextView tvBasic = (TextView) findViewById(R.id.tvPlayer_Basic);
		TextView tvThisYear = (TextView) findViewById(R.id.tvPlayer2010);

		//Retrive basic info to show on basic layout
		for (ValuePair v : playerBasicInfo){
			if (v.getKey().equals("선수명")){
				tvName.setText(v.getValue());
				tvName.setTextSize(fontSize[0]);
			}
			else if (v.getKey().equals("포지션")){
				tvPos.setText(v.getValue());
				tvPos.setTextSize(fontSize[1]);
			}
			else if (v.getKey().equals("생년월일")){
				tvBirth.setText(v.getValue());
				tvBirth.setTextSize(fontSize[1]);
			}
		}
		tvBasic.setTextSize(fontSize[2]);
		tvThisYear.setTextSize(fontSize[2]);

		//Basic Info
		LinearLayout player_basic = (LinearLayout) findViewById(R.id.ps_basic_layout);
		for(ValuePair v : playerBasicInfo)
		{
			if(!(v.getKey().equals("선수명") || v.getKey().equals("포지션") || v.getKey().equals("생년월일")))
				player_basic.addView(createTextView(v.getKey()+": "+v.getValue()));
		}

		//This year
		TableLayout player_thisyear = (TableLayout) findViewById(R.id.ps_this_year_table);
		for(int i=0; i<playerThisYearInfo.size(); )
		{
			TableRow tr = new TableRow(this);
			for(int j=0; j<4; j++)
			{
				if(i>= playerThisYearInfo.size())
					break;

				TextView tv = new TextView(this);
				if(j % 2 == 0) // 항목이 들어가야함
				{
					tv.setText(" "+playerThisYearInfo.get(i).getKey()+" ");
					tv.setTextColor(Color.BLACK);
					tv.setTextSize(fontSize[3]);
					tv.setGravity(5);

				}
				else	// 값
				{
					tv.setText(playerThisYearInfo.get(i).getValue()+" ");
					tv.setTextColor(fontColor[0]);
					tv.setTextSize(fontSize[3]);
					tv.setGravity(5);
					i++;	// i 도 증가해 줘야, 다음으로 넘어간다.
				}
				tr.addView(tv);
			}
			player_thisyear.addView(tr);
		}
	}

	private TextView createTextView(String text)
	{
		TextView tv = new TextView(this);
		tv.setText(text);
		tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tv.setTextColor(fontColor[0]);
		tv.setTextSize(fontSize[3]);
		tv.setPadding(10, 0, 0, 0);
		return tv;
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

	// 선수 정보 파싱
	private void createPlayerInfo(){
		PlayerParser pp = new PlayerParser(mPlayer.getID(), mPlayer.getType()); 
		playerBasicInfo = pp.getBasicInfo();
		playerThisYearInfo = pp.getThisYear();
		playerRecentGamesInfo = pp.getRecentInfo();
		playerHistoryInfo = pp.getHistoryInfo();
		playerRecentGamesInfoAttrSize = pp.getRecentInfoAttrSize();
		playerHistoryInfoAttrSize = pp.getHistoryInfoAttrSize();
		updatePlayerInfo();
	}

	protected class InitTask extends AsyncTask<Context, Integer, String>
	{

		@Override
		protected String doInBackground(Context... arg0) {
			// TODO Auto-generated method stub
			createPlayerInfo();
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
