package com.js.baseballdb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FavoritePlayerActivity extends Activity
{
	//Global
	public static ArrayList<Player> m_playerList = new ArrayList<Player>();
	private SharedPreferences pref;
	public static ListView list;
	public static TextView tv;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout);
        
        createFavoriteList();	//리스트 불러옴
        list = (ListView)findViewById(R.id.list);
        tv = (TextView) findViewById(R.id.tv_not_found);
        
        sortList();
        list.setAdapter(new PlayerAdapter(this, R.layout.custom_list, m_playerList));
        list.setOnItemClickListener(mItemClickListener);
        registerForContextMenu(list);

        if (m_playerList.size() > 0)
        {
        	tv.setVisibility(View.GONE);
        }
        else
        {
        	list.setVisibility(View.GONE);
        	tv.setText("등록된 선수가 없습니다.");
        	tv.setVisibility(View.VISIBLE);
        }
    }
    
    // Preference 에서 읽어오자.
    private void createFavoriteList()
    {
    	pref = getSharedPreferences("KBO_2012", MODE_PRIVATE);
    	String str = pref.getString("str", "");
    	
    	StringTokenizer tk = new StringTokenizer(str, "|");
    	FavoriteList.arrFavorite = new ArrayList<Player>();
    	m_playerList.clear();
    	
    	String id = "", num = "", name = "", pos = "", team="";
    	int type=0;
    	while(tk.hasMoreTokens())
    	{
    		id = tk.nextToken();
    		num = tk.nextToken();
    		name = tk.nextToken();
    		pos = tk.nextToken();
    		team = tk.nextToken();
    		type = Integer.parseInt(tk.nextToken());
    		
    		Player p = new Player(id, num, name, pos, team, type);
    		m_playerList.add(p);
    		FavoriteList.arrFavorite.add(p);
    	}
    }
    
    public static void sortList()
    {
    	int i=0, j=0;
    	int sizeList = m_playerList.size();
    	
    	//Bubble sort
    	for(i=0;i<(sizeList-1);i++)
    	{
    		for(j=0;j<(sizeList-1-i);j++)
    		{
    			if(m_playerList.get(j).getName().compareTo(m_playerList.get(j+1).getName())>0)
    			{
    				Player tmp_player = m_playerList.get(j);
    				m_playerList.set(j, m_playerList.get(j+1));
    				m_playerList.set(j+1, tmp_player);
    			}
    		}
    	}
    	
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
    {
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    	String name = m_playerList.get(info.position).getName();
    	if (v.getId()==R.id.list) {
    		menu.setHeaderTitle(name);
    		menu.add(0, 1, 0, "즐겨찾기 삭제");
    	}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
    	int menuItemIndex = item.getItemId();

    	switch(menuItemIndex)
    	{
    	case 1:
    		String pID = m_playerList.get(info.position).getID();
    		m_playerList.remove(info.position);
    		
    		for(int i=0; i<FavoriteList.arrFavorite.size(); i++)
    		{
    			if(FavoriteList.arrFavorite.get(i).getID().equalsIgnoreCase(pID))
    			{
    				FavoriteList.arrFavorite.remove(i);
    				this.saveToPreference();
    				break;
    			}
    		}
    		Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
    		refreshList();
    		break;
    	}
    	return true;
    }
    
    public static void addedFromStat()
    {
    	list.setVisibility(View.VISIBLE);
    	tv.setVisibility(View.GONE);
    }
    public void refreshList()
    {
    	createFavoriteList();
    	if (m_playerList.size() > 0)
        {
    		sortList();
	        list.setAdapter(new PlayerAdapter(this, R.layout.custom_list, m_playerList));
	        list.setOnItemClickListener(mItemClickListener);
	        registerForContextMenu(list);
	        
	        TextView tv = (TextView) findViewById(R.id.tv_not_found);
	    	tv.setVisibility(View.GONE);
        }
        else
        {
        	list.setVisibility(View.GONE);
        	TextView tv = (TextView) findViewById(R.id.tv_not_found);
        	tv.setText("등록된 선수가 없습니다.");
        	tv.setVisibility(View.VISIBLE);
        }
    }
    
    private void saveToPreference()
	{
    	SharedPreferences pref = getSharedPreferences("KBO_2012", MODE_PRIVATE);
    	SharedPreferences.Editor edt = pref.edit();

    	String rtn = FavoriteList.convToString();
    	edt.putString("str", rtn);
    	edt.commit();
	}

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// TODO Auto-generated method stub
			Player p = m_playerList.get(position);
			
			Intent intent = new Intent(FavoritePlayerActivity.this, PlayerStatActivity.class);
			Bundle extra = new Bundle();
			extra.putSerializable("player", (Serializable) p);
			intent.putExtras(extra);
        	intent.putExtra("currentTab", 0); // 0: Favorite Tab
        	intent.putExtra("currentView", 0);	// 0: 팀 검색   1: 플레이어 검색
            View newView = FavoriteGroup.favGroup.getLocalActivityManager()
            	.startActivity("PlayerStatActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            	.getDecorView();
            
            // Again, replace the view
            FavoriteGroup.favGroup.replaceView(newView);
		}
    	
	};
	
	public void onBackPressed(){
		FavoriteGroup.favGroup.back();
	}
	
	private int convDPtoPixel(float dp){
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		return (int) (metrics.density * dp + 0.5f);
	}
	
	public void loadFontSize(){
		//폰트
        SharedPreferences  pref = getSharedPreferences("KBO_2012", MODE_PRIVATE);
    	String confFontSize = pref.getString("fontsize", "보통");
    	
    	// 		0: 이름 출력 사이즈, 1: 백넘버, 포지션, 2: 정보캡션, 3: 정보
    	int[] fontSize = new int[4];
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
	}
}
