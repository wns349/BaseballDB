package com.js.baseballdb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PlayerParser {
	private   String playerID;
	private   int playerType;
	private   ArrayList<ValuePair> playerBasicInfo = new ArrayList<ValuePair>();	// 선수 기본 정보.
	private   ArrayList<ValuePair> playerThisYearInfo = new ArrayList<ValuePair>(); 	// 선수 올해 성적.
	private   ArrayList<ValuePair> playerRecentGamesInfo = new ArrayList<ValuePair>();	// 최근 5경기
	private   ArrayList<ValuePair> playerHistoryInfo	= new ArrayList<ValuePair>();	// 총 성적
	String[] playerThisYearInfoKeys = null;
	String[] playerRecentGamesInfoKeys = null;
	String[] playerHistoryInfoKeys = null;

	private   boolean isPlaying = false; // 현역 선수

	public PlayerParser(String playerID, int playerType)
	{
		this.playerID = playerID;
		this.playerType = playerType;
		this.createInfo();
	}

	private   void createInfo()
	{
		if (playerType == CONST.PLAYER_TYPE_PITCHER){
			String[] playerThisYearInfo = {"팀명", "방어율","경기", "완투", "완봉", "승", "패", "세", "홀", "승률", "타자", "투구수", "이닝", "피안타", "2루타", "3루타", "피홈런", 
					"희타", "희비", "볼넷", "고의4구", "사구", "탈삼진", "폭투", "보크", "실점", "자책점", "블론세이브", "WHIP", "피안타율", "QS"};

			String[] playerRecentGamesInfo = {"일자", "상대", "방어율", "결과", "타자", "이닝", "피안타", "피홈런", "볼넷", "사구", "탈삼진", "실점", "자책점", "피안타율"};

			String[] playerHistoryInfo = {"연도", "팀명", "방어율", "경기", "완투", "완봉", "승", "패", "세", "홀", "승률", "타자", "이닝", "피안타", "피홈런", "볼넷", "사구", "탈삼진", "실점", "자책점"};

			playerThisYearInfoKeys = playerThisYearInfo;
			playerRecentGamesInfoKeys = playerRecentGamesInfo;
			playerHistoryInfoKeys = playerHistoryInfo;
		} else if (playerType == CONST.PLAYER_TYPE_HITTER){
			String[] playerThisYearInfo = {"팀명", "타율", "경기", "타석", "타수", "득점", "안타", "2루타", "3루타", "홈런", "루타", "타점", "도루", "도실", "회타", "회비",
					"볼넷", "고의4구", "사구", "삼진", "병살", "장타율", "출루율", "실책", "도루성공률", "K/BB", "장타/안타", "멀티히트", "OPS", "득점권타율", "대타타율"};

			String[] playerRecentGamesInfo = {"일자", "상대", "타율", "타수", "득점", "안타", "2루타", "3루타", "홈런", "타점", "도루", "도실", "볼넷", "사구", "삼진", "병살"};

			String[] playerHistoryInfo = {"연도", "팀명", "타율", "경기", "타수", "득점", "안타", "2루타", "3루타", "홈런", "루타수", "타점", "도루", "도실", "볼넷", "사구", "삼진", "병살", "실책"};

			playerThisYearInfoKeys = playerThisYearInfo;
			playerRecentGamesInfoKeys = playerRecentGamesInfo;
			playerHistoryInfoKeys = playerHistoryInfo;
		}

		try 
		{
			String searchID = playerID; // 76232: 양의지   83460 : 장효조   76715 : 류현진
			String thisLine = "";
			searchID = URLEncoder.encode(searchID, "utf-8");
			String addr =""; 
			int tempCountForThisYear = 0;

			if(playerType==CONST.PLAYER_TYPE_HITTER)	// 야수
			{
				addr = "http://www.koreabaseball.com/Record/HitterDetail1.aspx?pcode="+searchID;
			}
			else if(playerType==CONST.PLAYER_TYPE_PITCHER)
			{
				addr = "http://www.koreabaseball.com/Record/PitcherDetail1.aspx?pcode="+searchID;
			}

			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if( conn != null)
			{
				conn.setConnectTimeout(10000);
				conn.setUseCaches(false);
				if( conn.getResponseCode() == HttpURLConnection.HTTP_OK)
				{
					BufferedReader br = new BufferedReader( new InputStreamReader(conn.getInputStream(), "utf-8"));
					String temp = "";
					int countTbody = 0;

					while (true)
					{
						thisLine = br.readLine();

						// EOF
						if (thisLine == null)
							break;

						//선수 기본 정
						addPlayerBasicInfoIfAvailable(thisLine);

						if (thisLine.contains("2012 성적"))
							isPlaying = true;

						if (isPlaying)
						{
							
							if (countTbody < 4 && thisLine.contains("<tbody>"))
							{
								int itemCount = 0;
								thisLine = br.readLine();

								while(!thisLine.contains("</tbody>"))
								{
									if (thisLine.contains("td>") && thisLine.contains("</td"))
									{
										temp = thisLine.substring( thisLine.indexOf('>')+1 , thisLine.lastIndexOf('<'));
										temp = temp.replace("&nbsp;", "");

										if (countTbody == 0){
											tempCountForThisYear = itemCount;
											playerThisYearInfo.add(new ValuePair(playerThisYearInfoKeys[itemCount], temp));	// basic
										}
										else if (countTbody == 1) {
											if(itemCount == 0)	//To support second row of thisyear 
												itemCount = tempCountForThisYear+1;
											playerThisYearInfo.add(new ValuePair(playerThisYearInfoKeys[itemCount], temp));	// basic
										}
										else if (countTbody==2)
											playerRecentGamesInfo.add(new ValuePair(playerRecentGamesInfoKeys[itemCount%playerRecentGamesInfoKeys.length], temp));	//recent
										else if (countTbody==3){
											playerHistoryInfo.add(new ValuePair(playerHistoryInfoKeys[itemCount%playerHistoryInfoKeys.length], temp));	//history
										}

										itemCount ++;
										
									}

									//For playerHistoryInfo
									if (countTbody == 3 && (thisLine.contains("class=\"fir\">") && thisLine.contains("</th>"))){
										itemCount=0;
										temp = thisLine.substring( thisLine.indexOf('>')+1 , thisLine.lastIndexOf('<'));
										temp = temp.replace("&nbsp;", "");
										playerHistoryInfo.add(new ValuePair(playerHistoryInfoKeys[itemCount], temp));	//history
										itemCount++;
									}
									thisLine = br.readLine();
								} 

								countTbody ++;
							}
							if (thisLine.contains("<tfoot>"))
							{
								int itemCount = 0;
								thisLine = br.readLine();
								while(!thisLine.contains("</tfoot>"))
								{
									if (thisLine.contains("td>") && thisLine.contains("</td") || thisLine.contains("scope=\"row\">") && thisLine.contains("</th>"))
									{
										temp = thisLine.substring( thisLine.indexOf('>')+1 , thisLine.lastIndexOf('<'));
										temp = temp.replace("&nbsp;", "");
										playerHistoryInfo.add(new ValuePair(playerHistoryInfoKeys[itemCount], temp));	//history
										itemCount++;
									}
									thisLine = br.readLine();
								} 
							}
						}
						//For coaches or retired players (isPlaying = false)
						else{
							if (countTbody < 1 && thisLine.contains("<tbody>"))
							{
								int itemCount = 0;
								thisLine = br.readLine();

								while(!thisLine.contains("</tbody>"))
								{
									if (thisLine.contains("td>") && thisLine.contains("</td"))
									{
										temp = thisLine.substring( thisLine.indexOf('>')+1 , thisLine.lastIndexOf('<'));
										temp = temp.replace("&nbsp;", "");
										playerHistoryInfo.add(new ValuePair(playerHistoryInfoKeys[itemCount], temp));	//history
										itemCount ++;
									}

									//For playerHistoryInfo
									if (countTbody == 0 && (thisLine.contains("class=\"fir\">") && thisLine.contains("</th>"))){
										itemCount=0;
										temp = thisLine.substring( thisLine.indexOf('>')+1 , thisLine.lastIndexOf('<'));
										temp = temp.replace("&nbsp;", "");
										playerHistoryInfo.add(new ValuePair(playerHistoryInfoKeys[itemCount], temp));	//history
										itemCount++;
									}
									thisLine = br.readLine();
								} 

								countTbody ++;
							}
							if (thisLine.contains("<tfoot>"))
							{
								int itemCount = 0;
								thisLine = br.readLine();
								while(!thisLine.contains("</tfoot>"))
								{
									if (thisLine.contains("td>") && thisLine.contains("</td") || thisLine.contains("scope=\"row\">") && thisLine.contains("</th>"))
									{
										temp = thisLine.substring( thisLine.indexOf('>')+1 , thisLine.lastIndexOf('<'));
										temp = temp.replace("&nbsp;", "");
										playerHistoryInfo.add(new ValuePair(playerHistoryInfoKeys[itemCount], temp));	//history
										itemCount++;
									}
									thisLine = br.readLine();
								} 
							}
						}
					}
					br.close();
				}
				conn.disconnect();
			}
			System.out.println("DONE");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{

		}
	}

	// Basic player list
	private   boolean addPlayerBasicInfoIfAvailable(String line)
	{
		String[] wordList = {"선수명:", "생년월일:", "신장/체중:", "입단 계약금:", "지명순위:", "등번호:", "포지션:", "경력:", "연봉:"};
		String key, value;

		for(int i = 0 ; i < wordList.length; i++)
		{
			if (line.contains(wordList[i])){
				value = trimHtmlTags(line);
				value = value.replace(wordList[i], "");
				value = value.trim();

				key = wordList[i].replace(":", "");
				playerBasicInfo.add(new ValuePair(key, value));
				return true;
			}
		}
		return false;
	}

	//Returns string only after removing all HTML tags
	private   String trimHtmlTags(String text){
		String tag;
		int startTag, endTag;

		while(text.contains("<") || text.contains(">")){
			startTag = text.indexOf('<');
			endTag = text.indexOf('>');
			tag = text.substring(startTag, endTag+1);
			text = text.replace(tag, "");
		}
		text = text.trim();

		return text;
	}

	public ArrayList<ValuePair> getBasicInfo()
	{
		return playerBasicInfo;
	}

	public ArrayList<ValuePair> getRecentInfo()
	{
		return playerRecentGamesInfo;
	}

	public ArrayList<ValuePair> getHistoryInfo()
	{
		return playerHistoryInfo;
	}

	public ArrayList<ValuePair> getThisYear()
	{
		return playerThisYearInfo;
	}

	public int getThisYearAttrSize(){
		return playerThisYearInfoKeys.length;
	}
	public int getRecentInfoAttrSize(){
		return playerRecentGamesInfoKeys.length;
	}

	public int getHistoryInfoAttrSize(){
		return playerHistoryInfoKeys.length;
	}
}
