//$Id$
package com.zoho.abtest.report;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import com.zoho.abtest.OAUTH;
import com.zoho.abtest.OAUTH_SYS;
import com.zoho.abtest.webhook.Webhook;
import com.zoho.logs.common.jsonorg.JSONObject;

public class Oauth extends CustomAuth {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(Oauth.class.getName());
	
	private static StringBuffer getToken(String accessurl){
		StringBuffer response = new StringBuffer();
		String s=null;
		try {
		     accessurl=accessurl.trim();
			URL obj = new URL(accessurl);
			s=obj.getProtocol();
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST"); //NO I18N
	        con.setDoOutput(true);
			con.setConnectTimeout(3000);
			
			int responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
				e.printStackTrace();
		}
	return response;
	}
	
	
	public static Webhook userAuth(Webhook webhook) throws Exception{
		
		try{
	        HashMap<String,String> headers=hashHeaders(webhook.getHeaders());
			Criteria c = new Criteria(new Column(OAUTH.TABLE,OAUTH.WEBHOOK_ID),webhook.getWebhookId(), QueryConstants.EQUAL);
			DataObject dobj=getRow(OAUTH.TABLE,c);
			Iterator it =dobj.getRows(OAUTH.TABLE);
			if(it.hasNext())   
			  {
				Row row=(Row) it.next();
                 String accessurl=(String)row.get(OAUTH.ACCESS_TOKEN_URL);
                String refreshtoken=(String)row.get(OAUTH.REFRESH_TOKEN);
                 accessurl=replaceToken(accessurl,refreshtoken);
                 headers=replaceToken(headers, refreshtoken);
	             String tokens=getToken(accessurl).toString();
	             JSONObject json=new JSONObject(tokens);
				String access_token =json.get("access_token").toString();
	     //Getting access token using access url and refresh token
	             webhook.setWebhookUrl(replaceToken(webhook.getWebhookUrl(),access_token));
                 headers=replaceToken(headers,access_token);
                 JSONObject json1=new JSONObject(headers);
                 webhook.setHeaders(json1.toString());
			}
	     
		}
		catch (Exception e) {
			e.printStackTrace();
	}
		return webhook;
	}
	
	
	public static Webhook sysAuth(Webhook webhook) throws Exception{
		
		try{

	        HashMap<String,String> headers=hashHeaders(webhook.getHeaders());
	        String webhookname=webhook.getWebhookLinkName();
	        Criteria c = new Criteria(new Column(OAUTH_SYS.TABLE,OAUTH_SYS.WEBHOOK_NAME),webhookname, QueryConstants.EQUAL);
			DataObject dobj=getRow(OAUTH_SYS.TABLE,c);
			Iterator it =dobj.getRows(OAUTH_SYS.TABLE);
			if(it.hasNext())   
			  {
				Row row=(Row) it.next();
                 String accessurl=webhook.getAccessTokenUrl();
                String refreshtoken=(String)row.get(OAUTH_SYS.REFRESH_TOKEN);
                 accessurl=replaceToken(accessurl,refreshtoken);
	             String tokens=getToken(accessurl).toString();  //post method
	             JSONObject json=new JSONObject(tokens);
				String access_token =json.get("access_token").toString();
	     //Getting access token using access url and refresh token
				
	             webhook.setWebhookUrl(replaceToken(webhook.getWebhookUrl(),access_token));
                 headers=replaceToken(headers,access_token);
                 headers=replaceToken(headers, refreshtoken);
                 JSONObject json1=new JSONObject(headers);
                 webhook.setHeaders(json1.toString());
			}
	     
		}
		catch (Exception e) {
			e.printStackTrace();
	}
		return webhook;
	}
	
public Webhook authentication(Webhook webhook){
		
		final Logger LOGGER = Logger.getLogger(Oauth.class.getName());
		try{
			         if(!webhook.isSystem()){
			        	 webhook=userAuth(webhook);
			         }
			         else{
			        	 webhook=sysAuth(webhook);
			         }
	
	}
		catch(Exception e){
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return webhook;
	}



}
