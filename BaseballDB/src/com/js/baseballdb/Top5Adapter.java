package com.js.baseballdb;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Top5Adapter extends ArrayAdapter<String>
{
	Activity context;
	ArrayList<String> arList;
	public Top5Adapter (Activity c, ArrayList<String> arList)
	{
		super(c, R.layout.top5_list, arList);
		this.context = c;
		this.arList = arList;
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inf = context.getLayoutInflater();
		View customcell = inf.inflate(R.layout.top5_list, null);
		
		TextView tvPlayerName = (TextView) customcell.findViewById(R.id.top5_listText);
		
		tvPlayerName.setText(arList.get(position));

		
		return customcell;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arList.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return arList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}