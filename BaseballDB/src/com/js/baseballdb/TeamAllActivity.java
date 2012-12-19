package com.js.baseballdb;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TeamAllActivity extends Activity
{
	private ProgressDialog m_progressDialog;
	protected InitTask m_initTask;
	private ArrayList<Team> m_teamList;
	private ArrayList<String> teamAll;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_all_layout);
		m_teamList = new ArrayList<Team>();

		m_progressDialog = ProgressDialog.show(getParent(), "Loading..", "�� ������ �ε��ϰ� �ֽ��ϴ�.", true, false);
		m_initTask = new InitTask();
		m_initTask.execute( this );

	}
	private void makeList()
	{
		//���� ������ HTML �� �о�� ���� Team ArrayList �� �����Ѵ�. 
		teamAll = (new TeamStandingParser()).getTeamStanding();
		for (int i=0;i<teamAll.size(); i = i + 11)
		{
			Team t = new Team(teamAll.get(i), teamAll.get(i+1), teamAll.get(i+2), teamAll.get(i+3),
					teamAll.get(i+4), teamAll.get(i+5), teamAll.get(i+6), teamAll.get(i+7),
					teamAll.get(i+8), teamAll.get(i+9), teamAll.get(i+10));
			m_teamList.add(t);
		}
	}

	private void createList()
	{
		TableLayout table_layout = (TableLayout) findViewById(R.id.team_table_layout);
		table_layout.setStretchAllColumns(true);
    	//Attr ���� �߰�
    	for(int i=0; i<teamAll.size(); i = i + 11)
    	{
    		TableRow tr = new TableRow(this);
    		for(int j=i;j<(i+11);j++)
    		{
    			if(j > i+8)	// �������� ���� �������� ����
    				continue;
    			
    			TextView tv = new TextView(this);
    			tv.setPadding(3, 0, 3, 0);
    			tv.setText(teamAll.get(j));
    			if (i%2==1)
    				tv.setTextColor(Color.BLACK);
    			else
    				tv.setTextColor(Color.BLUE);
    			tv.setGravity(17);
    			tr.addView(tv);
    		}
			table_layout.addView(tr);
    	}
	}
	
	protected class InitTask extends AsyncTask<Context, Integer, String>
	{

		@Override
		protected String doInBackground(Context... arg0) {
			// TODO Auto-generated method stub
			makeList();
			return null;
		}
		@Override
		protected void onPostExecute( String result ) 
		{
			super.onPostExecute(result);
			m_progressDialog.dismiss();
			createList();
		}

		protected void onCancelled() {
			cancel(true);
			m_progressDialog.dismiss();
		}
	}

}
