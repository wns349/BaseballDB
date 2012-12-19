package com.js.baseballdb;

public class Team 
{
	private String rank, teamName, totalGames, win, lose, draw, ratio, diff, cont, walk, hit;
	public Team(String rank, String teamName, String totalGames, String win, String lose, 
			String draw, String ratio, String diff, String cont, String walk, String hit)
	{
		this.rank = rank;
		this.teamName = teamName;
		this.totalGames = totalGames;
		this.win = win;
		this.lose = lose;
		this.draw = draw;
		this.ratio = ratio;
		this.diff = diff;
		this.cont = cont;
		this.walk = walk;
		this.hit = hit;
	}
	
	public String getRank()
	{
		return this.rank;
	}
	public String getTeamName()
	{
		return this.teamName;
	}
	public String getTotalGames()
	{
		return this.totalGames;
	}
	public String getWin()
	{
		return this.win;
	}
	public String getLose()
	{
		return this.lose;
	}
	public String getDraw()
	{
		return this.draw;
	}
	public String getRatio()
	{
		return this.ratio;
	}
	public String getDiff()
	{
		return this.diff;
	}
	public String getCont()
	{
		return this.cont;
	}
	public String getWalk()
	{
		return this.walk;
	}
	public String getHit()
	{
		return this.hit;
	}
}
