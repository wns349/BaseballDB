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
	private   ArrayList<ValuePair> playerBasicInfo = new ArrayList<ValuePair>();	// ���� �⺻ ����.
	private   ArrayList<ValuePair> playerThisYearInfo = new ArrayList<ValuePair>(); 	// ���� ���� ����.
	private   ArrayList<ValuePair> playerRecentGamesInfo = new ArrayList<ValuePair>();	// �ֱ� 5���
	private   ArrayList<ValuePair> playerHistoryInfo	= new ArrayList<ValuePair>();	// �� ����
	String[] playerThisYearInfoKeys = null;
	String[] playerRecentGamesInfoKeys = null;
	String[] playerHistoryInfoKeys = null;

	private   boolean isPlaying = false; // ���� ����

	public PlayerParser(String playerID, int playerType)
	{
		this.playerID = playerID;
		this.playerType = playerType;
		this.createInfo();
	}

	private   void createInfo()
	{
		if (playerType == CONST.PLAYER_TYPE_PITCHER){
			String[] playerThisYearInfo = {"����", "�����","���", "����", "�Ϻ�", "��", "��", "��", "Ȧ", "�·�", "Ÿ��", "������", "�̴�", "�Ǿ�Ÿ", "2��Ÿ", "3��Ÿ", "��Ȩ��", 
					"��Ÿ", "���", "����", "����4��", "�籸", "Ż����", "����", "��ũ", "����", "��å��", "��м��̺�", "WHIP", "�Ǿ�Ÿ��", "QS"};

			String[] playerRecentGamesInfo = {"����", "���", "�����", "���", "Ÿ��", "�̴�", "�Ǿ�Ÿ", "��Ȩ��", "����", "�籸", "Ż����", "����", "��å��", "�Ǿ�Ÿ��"};

			String[] playerHistoryInfo = {"����", "����", "�����", "���", "����", "�Ϻ�", "��", "��", "��", "Ȧ", "�·�", "Ÿ��", "�̴�", "�Ǿ�Ÿ", "��Ȩ��", "����", "�籸", "Ż����", "����", "��å��"};

			playerThisYearInfoKeys = playerThisYearInfo;
			playerRecentGamesInfoKeys = playerRecentGamesInfo;
			playerHistoryInfoKeys = playerHistoryInfo;
		} else if (playerType == CONST.PLAYER_TYPE_HITTER){
			String[] playerThisYearInfo = {"����", "Ÿ��", "���", "Ÿ��", "Ÿ��", "����", "��Ÿ", "2��Ÿ", "3��Ÿ", "Ȩ��", "��Ÿ", "Ÿ��", "����", "����", "ȸŸ", "ȸ��",
					"����", "����4��", "�籸", "����", "����", "��Ÿ��", "�����", "��å", "���缺����", "K/BB", "��Ÿ/��Ÿ", "��Ƽ��Ʈ", "OPS", "������Ÿ��", "��ŸŸ��"};

			String[] playerRecentGamesInfo = {"����", "���", "Ÿ��", "Ÿ��", "����", "��Ÿ", "2��Ÿ", "3��Ÿ", "Ȩ��", "Ÿ��", "����", "����", "����", "�籸", "����", "����"};

			String[] playerHistoryInfo = {"����", "����", "Ÿ��", "���", "Ÿ��", "����", "��Ÿ", "2��Ÿ", "3��Ÿ", "Ȩ��", "��Ÿ��", "Ÿ��", "����", "����", "����", "�籸", "����", "����", "��å"};

			playerThisYearInfoKeys = playerThisYearInfo;
			playerRecentGamesInfoKeys = playerRecentGamesInfo;
			playerHistoryInfoKeys = playerHistoryInfo;
		}

		try 
		{
			String searchID = playerID; // 76232: ������   83460 : ��ȿ��   76715 : ������
			String thisLine = "";
			searchID = URLEncoder.encode(searchID, "utf-8");
			String addr =""; 
			int tempCountForThisYear = 0;

			if(playerType==CONST.PLAYER_TYPE_HITTER)	// �߼�
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

						//���� �⺻ ��
						addPlayerBasicInfoIfAvailable(thisLine);

						if (thisLine.contains("2012 ����"))
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
		String[] wordList = {"������:", "�������:", "����/ü��:", "�Դ� ����:", "�������:", "���ȣ:", "������:", "���:", "����:"};
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
