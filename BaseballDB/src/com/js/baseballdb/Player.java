package com.js.baseballdb;

import java.io.Serializable;

public class Player implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6853107818235787288L;
	private String playerID; 
	private String playerNum;
	private String playerName;
	private String playerPos;
	private String playerTeam;
	private int playerType;
	
	public Player(String playerId){
		this.playerID = playerId;
	}
	
	public Player(String playerID, String playerNum, String playerName, String playerPos, String playerTeam, int playerType)
	{
		this.playerID = playerID;
		this.playerNum = playerNum;
		this.playerName = playerName;
		this.playerPos = playerPos;
		this.playerTeam = playerTeam;
		this.playerType = playerType;
	}
	
	public void setTeam(String playerTeam){
		this.playerTeam = playerTeam;
	}
	
	public void setName(String playerName){
		this.playerName = playerName;
	}
	public void setPos(String playerPos){
		this.playerPos = playerPos;
	}
	public void setNum(String playerNum){
		this.playerNum = playerNum;
	}
	
	public void setType(int playerType){
		this.playerType = playerType;
	}
		
	public String getID()
	{
		return playerID;
	}
	public String getNum()
	{
		return playerNum;
	}
	public String getName()
	{
		return playerName;
	}
	public String getPos()
	{
		return playerPos;
	}
	public String getTeam(){
		return playerTeam;
	}
	
	public int getType()
	{
		return playerType;
	}
}