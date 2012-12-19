package com.js.baseballdb;

import java.util.ArrayList;

public class FavoriteList 
{
	public static ArrayList<Player> arrFavorite;

	public static String convToString(){
		int size = arrFavorite.size();
		String rtn = "";
		for( int i=0; i<size; i++){
			rtn += arrFavorite.get(i).getID() + "|";
			rtn += arrFavorite.get(i).getNum() + "|";
			rtn += arrFavorite.get(i).getName() + "|";
			rtn += arrFavorite.get(i).getPos() + "|";
			rtn += arrFavorite.get(i).getTeam() +"|";
			rtn += arrFavorite.get(i).getType()+ "|";
		}
		return rtn;
	}
}
