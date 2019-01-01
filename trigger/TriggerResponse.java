//$Id$
package com.zoho.abtest.trigger;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.common.ZABResponse;
import com.zoho.abtest.experiment.Experiment;
import com.zoho.abtest.experiment.ExperimentConstants;
import com.zoho.abtest.experiment.ExperimentResponse;
import com.zoho.abtest.user.ZABUser;
import com.zoho.abtest.user.ZABUserConstants;
import com.zoho.abtest.utility.ZABUtil;

public class TriggerResponse {
	
	private static final Logger LOGGER = Logger.getLogger(TriggerResponse.class.getName());
	

	
	public static String jsonResponse(HttpServletRequest request,ArrayList<Trigger> lst) {			
		StringBuffer returnBuffer = new StringBuffer();
		try{
			JSONArray array = getJSONArray(lst);			
			JSONObject json = ZABResponse.updateMetaInfo(request, TriggerConstants.API_MODULE_PLURAL, array);
			returnBuffer.append(json);
			json=null;
		}
		catch(Exception ex){			
			LOGGER.log(Level.SEVERE, ex.getMessage(),ex);
}
		return returnBuffer.toString();
	}
	
	private static JSONObject getJSONObject(Trigger ld) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		
		LOGGER.log(Level.INFO, ">>TRIGGER ID in response:"+ld.getId());
		
	
		
		
		jsonObj.put(TriggerConstants.TRIGGER_ID, ld.getId());
		jsonObj.put(TriggerConstants.TRIGGER_TYPE, ld.getTrigger_type());
		jsonObj.put(TriggerConstants.WEBHOOK_ID, ld.getWebhookid());
		jsonObj.put(TriggerConstants.GOAL_ID, ld.getGoalid());
		jsonObj.put(TriggerConstants.WEBHOOK_LINK_NAME, ld.getWebhooklinkname());
	//	jsonObj.put(WebhookConstants.WEBHOOK_KEY, ld.getWebhookKey());
     //	jsonObj.put(ZABUserConstants.CREATED_TIME, ZABUtil.getDateTimeFormatted(ld.getCreatedTime()));
	//	jsonObj.put(ZABUserConstants.MODIFIED_TIME,ZABUtil.getDateTimeFormatted(ld.getModifiedTime()));
	//	jsonObj.put(ZABUserConstants.CREATED_BY, ZABUser.getUserName(ld.getCreatedById()));
		
		jsonObj.put(ZABConstants.RESPONSE_STRING, ld.getResponseString());
		jsonObj.put(ZABConstants.STATUS_CODE, ld.getResponseCode());
		jsonObj.put(ZABConstants.SUCCESS, ld.getSuccess());
		
		LOGGER.log(Level.INFO, "Trigger response json:"+ld.getId());
		
		return jsonObj;
	}
	
	public static JSONArray getJSONArray(ArrayList<Trigger> lst) throws JSONException {
		JSONArray array = new JSONArray();
		int size =lst.size();
		for (int i=0;i<size;i++) {
			Trigger ld=lst.get(i);
			if(ld!=null) {				
				array.put(getJSONObject(ld));
			}
		}
		return array;
	}
}