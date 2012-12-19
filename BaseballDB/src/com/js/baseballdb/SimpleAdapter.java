package com.js.baseballdb;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SimpleAdapter extends ArrayAdapter<String>
{
	Activity context;
	ArrayList<String> arList;
	public SimpleAdapter (Activity c, ArrayList<String> arList)
	{
		super(c, R.layout.simple_list, arList);
		this.context = c;
		this.arList = arList;
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inf = context.getLayoutInflater();
		View customcell = inf.inflate(R.layout.simple_list, null);
		
		TextView tvText = (TextView) customcell.findViewById(R.id.simple_text);
		
		tvText.setText(arList.get(position));
		
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