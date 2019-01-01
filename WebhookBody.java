//$Id$
package com.zoho.abtest.report;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import com.zoho.abtest.OAUTH;
import com.zoho.abtest.WEBHOOK;
import com.zoho.abtest.common.ZABModel;
import com.zoho.abtest.utility.ZABUtil;
import com.zoho.abtest.webhook.Webhook;
import com.zoho.abtest.webhook.WebhookConstants;
import com.zoho.abtest.webhook.WebhookConstants.AuthenticationType;
import org.json.JSONArray;

public class WebhookBody extends ZABModel{
	private static final Logger LOGGER = Logger.getLogger(ReportRawDataAction.class.getName());

	private static final long serialVersionUID = 1L;
	
	
  
	/**
	 * @param hs
	 * @param body
	 * @return replacing variables in body with its value in hashmap
	 */
	public static String getString(HashMap<String,String> hs,String body){
	
		try{
			 Iterator<Map.Entry<String, String>> iterator = hs.entrySet().iterator();
             
			    while (iterator.hasNext()) {
			        Map.Entry<String, String> entry = iterator.next();
			        if(body.contains(entry.getKey())){
			        	body=body.replace("{{"+entry.getKey()+"}}", entry.getValue());
			        }
			  
			 }
					
	}
		catch(Exception e){
		LOGGER.log(Level.SEVERE, e.getMessage(),e);
	}
		return body;
	}
	
	// HTTP GET request
		private static void sendGet(String body,String portal,String url,HashMap<String ,String> h) throws Exception {

			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET"); //NO I18N
			//add request header
			con.setRequestProperty("User-Agent", portal); //NO I18N
		    con.setRequestProperty("body", body);//NO I18N
					
			Iterator it = h.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		    	con.setRequestProperty(pair.getKey().toString() , pair.getValue().toString());
		        it.remove(); 
		    }
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

		}
	private static void sendPost(String body,String url,HashMap<String ,String> h) throws Exception {
		
		try {
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			//add reuqest header
			
			con.setRequestMethod("POST"); //NO I18N
			Iterator it = h.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		    	con.setRequestProperty(pair.getKey().toString() , pair.getValue().toString());
		    }
			// Send post request
			con.setDoOutput(true);
			con.setConnectTimeout(3000);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            try{
            wr.writeBytes(body);
}
			finally{
			
			wr.flush();
			wr.close();
			}
			int responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
				e.printStackTrace();
		}

	

	}

	/**
	 * @param s
	 * @return converting string to hashmap
	 * @throws Exception
	 */
	public static HashMap<String,String> hashHeaders(String s) throws Exception{
		int j;
		HashMap<String,String> h=new HashMap();
		if(s.length()==0){
			return null;}
		JSONArray jsonarray=new JSONArray(s);
		for(int i=0;i<jsonarray.length();i++){
           JSONObject  jsonObject=jsonarray.getJSONObject(i);
		   Iterator<?> keys = jsonObject.keys();
	        while(keys.hasNext()){
	        String key = (String)keys.next();
	        String val = null;
	        	val	=	(String) jsonObject.getString(key);
	        if(val != null && key!=null){
	        	h.put(key,val);
	        }
	    }
		}
        return h;	
	}
	
	
	/**
	 * @param hs
	 * @param webhook
	 */
	public static void call(HashMap<String,String> hs,Webhook webhook){
		
		final Logger LOGGER = Logger.getLogger(WebhookBody.class.getName());
		HashMap<String,String> headers;
		try{
	                   String portal=hs.get("PORTAL");
				       String body=(webhook.getWebhookBody());
				       String url=webhook.getWebhookUrl();				    			
						String method=webhook.getMethod();
						JSONObject json=new JSONObject(webhook.getHeaders());
				       headers=ZABUtil.convertJSONToHashMap(json);
				       body=getString(hs,body);
				       if(method.equalsIgnoreCase("get")){
				    	   sendGet(body,portal,url,headers);
				       }
				       else{
			    	   sendPost(body,url,headers);
			    	   }
						
	}
		catch(Exception e){
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return;
	}
	
	
/**
 * @param hs
 * @param webhookid
 */
public static void callCustom(HashMap<String,String> hs,Long webhookid){
		
		final Logger LOGGER = Logger.getLogger(WebhookBody.class.getName());
		HashMap<String,String> headers;
		try{
			
			Criteria c1 = new Criteria(new Column(WEBHOOK.TABLE,WEBHOOK.ID),webhookid, QueryConstants.EQUAL);
			DataObject dobj1=getRow(WEBHOOK.TABLE,c1);
			Iterator it1 =dobj1.getRows(WEBHOOK.TABLE);
	          String portal=hs.get("PORTAL");
			if(it1.hasNext() ){
				Row row1=(Row) it1.next();
				String body=(String)row1.get(WEBHOOK.BODY);
				String url=(String)row1.get(WEBHOOK.URL);
				String method=(String)row1.get(WEBHOOK.METHOD);
				headers=hashHeaders((String)row1.get(WEBHOOK.HEADERS));
			    body=getString(hs,body);
			    if(method=="get"){
			    	   sendGet(body,portal,url,headers);
			       }
			       else{
		    	   sendPost(body,url,headers);
		    	   }				
				
			}
			
	}
		catch(Exception e){
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return;
	}
	
	
}
