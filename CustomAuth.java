//$Id$
package com.zoho.abtest.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zoho.abtest.common.ZABModel;
import com.zoho.abtest.utility.ZABUtil;
import com.zoho.abtest.webhook.Webhook;
import com.zoho.abtest.webhook.WebhookConstants;

/**
 * Base authentication class 
 *
 */
public abstract class CustomAuth  extends ZABModel {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(CustomAuth.class.getName());
	
	public abstract Webhook  authentication(Webhook object) throws Exception;

	
	
 public static String replaceToken(String url,String token){
		 
		 try{
			 if(url.contains(WebhookConstants.REFRESH_TOKEN)){
				url= url.replace("{{"+WebhookConstants.REFRESH_TOKEN+"}}",token);
			 }
			 if(url.contains(WebhookConstants.ACCESS_TOKEN)){
				url= url.replace("{{"+WebhookConstants.ACCESS_TOKEN+"}}",token);
				 
			 }
		
		 }
		 catch (Exception e) {
				e.printStackTrace();
		}
		 return url;
	 }
	 /**
	 * @param hs
	 * @param token
	 * @return replacing token names in headers with its value
	 */
	public static HashMap<String,String> replaceToken(HashMap<String,String> hs ,String token){
		 
		 try{
			 Iterator<Map.Entry<String, String>> iterator = hs.entrySet().iterator();
			    while (iterator.hasNext()) {
			        Map.Entry<String, String> entry = iterator.next();
			      if(entry.getValue().contains(WebhookConstants.REFRESH_TOKEN)){
			    	  hs.put(entry.getKey(),entry.getValue().replace("{{"+WebhookConstants.REFRESH_TOKEN+"}}", token));
			    	  break;
			    }
			      if(entry.getValue().contains(WebhookConstants.ACCESS_TOKEN)){
			    	  hs.put(entry.getKey(),entry.getValue().replace("{{"+WebhookConstants.ACCESS_TOKEN+"}}", token));
			    	  break;
			    }
			 }
		
		 }
		 catch (Exception e) {
				e.printStackTrace();
		}
		 return hs;
	 }
	 
	 
	
		/**
		 * @param s
		 * @return converting string to hashmap
		 * @throws JSONException
		 */
		public static HashMap<String,String> hashHeaders(String s) throws JSONException{
			HashMap<String,String> h=new HashMap();
			if(s.length()==0){
				return null;}
			JSONArray jsonarray=new JSONArray(s);
			for(int i=0;i<jsonarray.length();i++){
				JSONObject json=(JSONObject) jsonarray.get(i);
				Iterator<?> keys=json.keys();
				while(keys.hasNext()){
					String key=(String) keys.next();
					h.put(key,json.getString(key));
				}
			}
	        return h;	
		}
	 

	 
}
