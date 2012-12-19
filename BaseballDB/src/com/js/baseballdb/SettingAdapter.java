package com.js.baseballdb;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SettingAdapter extends ArrayAdapter<SettingObject>
{
	Activity context;
	ArrayList<SettingObject> arList;
	public SettingAdapter (Activity c, ArrayList<SettingObject> arList)
	{
		super(c, R.layout.custom_list, arList);
		this.context = c;
		this.arList = arList;
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inf = context.getLayoutInflater();
		View settingcell = inf.inflate(R.layout.setting_list, null);
		
		TextView tvName = (TextView) settingcell.findViewById(R.id.setting_name);
		TextView tvValue = (TextView) settingcell.findViewById(R.id.setting_value);
		
		tvName.setText(arList.get(position).getName());
		tvValue.setText(arList.get(position).getValue());
		
		
		return settingcell;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arList.size();
	}

	@Override
	public SettingObject getItem(int position) {
		// TODO Auto-generated method stub
		return arList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}