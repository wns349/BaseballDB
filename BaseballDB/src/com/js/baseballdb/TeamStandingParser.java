package com.js.baseballdb;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TeamStandingParser {
	//ÆÀ ¼øÀ§
	private ArrayList<String> m_teamStanding = new ArrayList<String>();	//ÆÀ ¼øÀ§ ¹× ½ºÅÈ
	
	public TeamStandingParser()
	{
		this.createInfo();
	}
	
	private void createInfo()
	{
		try {
			String thisLine = "";
			String addr = "http://www.koreabaseball.com/Teamrank/TeamRank.aspx";	//Á¢¼Ó ÁÖ¼Ò

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
					boolean stopSearch = false;

					while (true)
					{
						thisLine = br.readLine();

						// EOF
						if (thisLine == null || stopSearch)
							break;

						if (thisLine.contains("<tbody>"))
						{
							while(!thisLine.contains("</tbody>"))
							{
								if(thisLine.contains("<td"))
								{
									temp = thisLine.substring(thisLine.indexOf('>') + 1, thisLine.lastIndexOf('<'));
									temp = temp.replace("&nbsp;", "");
									m_teamStanding.add(temp);
								}
								thisLine = br.readLine();
							}
							stopSearch = true;
						}
					}
					br.close();
				}
				conn.disconnect();
			}
			//System.out.println("DONE");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{

		}
	}

	public ArrayList<String> getTeamStanding()
	{
		return m_teamStanding;
	}
}