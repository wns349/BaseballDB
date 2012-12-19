package com.js.baseballdb;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlayerAdapter extends ArrayAdapter<Player>
{
	Activity mActivity;
	int mLayoutResourceId;    
	ArrayList<Player> mData = null;

	public PlayerAdapter(Activity activity, int layoutResourceId, ArrayList<Player> data) {
		super(activity, layoutResourceId, data);
		this.mLayoutResourceId = layoutResourceId;
		this.mData = data;
		this.mActivity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PlayerHolder holder = null;

		if(row == null)
		{
			LayoutInflater inflater = mActivity.getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);

			//create temporary holder
			holder = new PlayerHolder();
			TextView topRow = (TextView)row.findViewById(R.id.cell_top_row);
			holder.setTopRow(topRow);
			holder.setBottomRow((TextView)row.findViewById(R.id.cell_bottom_row));
			row.setTag(holder);
		}
		else
		{
			holder = (PlayerHolder)row.getTag();

			final Player player = mData.get(position);
			if(player != null){
				//Show Name on top row
				holder.updateTopRow(player.getName() + "  ("+player.getTeam()+")");
				//Show Back number and team(position)
				holder.updateBottomRow(player.getNum()+" - "+player.getPos().charAt(0));
			}
		}

		//Get current bizCell
		Player player = mData.get(position);

		//Update the content of the cell
		//Show Name on top row
		holder.updateTopRow(player.getName() + "  ("+player.getTeam()+")");
		//Show Back number and team(position)
		holder.updateBottomRow(player.getNum()+" - "+player.getPos().charAt(0));
		return row;
	}

	//Use this holder class for optimization. 
	static class PlayerHolder
	{
		TextView topRow, bottomRow;

		public void setTopRow(TextView topRow){
			this.topRow = topRow;
		}

		public void setBottomRow(TextView bottomRow){
			this.bottomRow = bottomRow;
		}

		public void updateTopRow(String top){
			this.topRow.setText(top);
		}

		public void updateBottomRow(String bottom){
			this.bottomRow.setText(bottom);
		}
	}
}