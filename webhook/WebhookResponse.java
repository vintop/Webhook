//$Id$
package com.zoho.abtest.webhook;

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

public class WebhookResponse {
	
	private static final Logger LOGGER = Logger.getLogger(WebhookResponse.class.getName());
	

	
	public static String jsonResponse(HttpServletRequest request,ArrayList<Webhook> lst) {			
		StringBuffer returnBuffer = new StringBuffer();
		try{
			JSONArray array = getJSONArray(lst);			
			JSONObject json = ZABResponse.updateMetaInfo(request, WebhookConstants.API_MODULE_PLURAL, array);
			returnBuffer.append(json);
			json=null;
		}catch(Exception ex){}
		return returnBuffer.toString();
	}
	
	private static JSONObject getJSONObject(Webhook ld) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		
		LOGGER.log(Level.INFO, ">> WEBHOOK ID in response:"+ld.getWebhookId());
		
		if(ld.getWebhookId() != -100000) {
			jsonObj.put(WebhookConstants.WEBHOOK_ID, String.valueOf(ld.getWebhookId()));
		}
		
		jsonObj.put(WebhookConstants.WEBHOOK_NAME, ld.getWebhookName());
		jsonObj.put(WebhookConstants.WEBHOOK_LINK_NAME, ld.getWebhookLinkName());
		jsonObj.put(WebhookConstants.WEBHOOK_BODY, ld.getWebhookBody());
		jsonObj.put(WebhookConstants.WEBHOOK_URL, ld.getWebhookUrl());
		jsonObj.put(WebhookConstants.WEBHOOK_PROJECT_LINKNAME, ld.getWebhookProjectLinkName());
		jsonObj.put(WebhookConstants.WEBHOOK_PROJECT_ID, ld.getWebhookProjectId());
		jsonObj.put(WebhookConstants.AUTHENTICATION_TYPE, ld.getAuthType());

	//	jsonObj.put(WebhookConstants.WEBHOOK_KEY, ld.getWebhookKey());
	//	jsonObj.put(ZABUserConstants.CREATED_TIME, ZABUtil.getDateTimeFormatted(ld.getCreatedTime()));
	//	jsonObj.put(ZABUserConstants.MODIFIED_TIME,ZABUtil.getDateTimeFormatted(ld.getModifiedTime()));
	//	jsonObj.put(ZABUserConstants.CREATED_BY, ZABUser.getUserName(ld.getCreatedById()));
	
		jsonObj.put(ZABConstants.RESPONSE_STRING, ld.getResponseString());
		jsonObj.put(ZABConstants.STATUS_CODE, ld.getResponseCode());
		jsonObj.put(ZABConstants.SUCCESS, ld.getSuccess());
		
		LOGGER.log(Level.INFO, "Project response json:"+ld.getWebhookId());
		
		return jsonObj;
	}
	
	public static JSONArray getJSONArray(ArrayList<Webhook> lst) throws JSONException {
		JSONArray array = new JSONArray();
		int size =lst.size();
		for (int i=0;i<size;i++) {
			Webhook ld=lst.get(i);
			if(ld!=null) {				
				array.put(getJSONObject(ld));
			}
		}
		return array;
	}
}