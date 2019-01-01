//$Id$
package com.zoho.abtest.integration;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.common.ZABResponse;

public class GoogleTagManagerResponse {
	
	private static final Logger LOGGER = Logger.getLogger(GoogleTagManagerResponse.class.getName());

	public static String jsonResponse(HttpServletRequest request,ArrayList<GoogleTagManager> lst) {			
		StringBuffer returnBuffer = new StringBuffer();
		try{
			JSONArray array = getJSONArray(lst);			
			JSONObject json = ZABResponse.updateMetaInfo(request, IntegrationConstants.API_MODULE_GOOGLETAGMANAGER, array);
			returnBuffer.append(json);
			json=null;
		}catch(Exception ex){
			
			LOGGER.log(Level.SEVERE,"Exception Occurred",ex);
			
		}
		return returnBuffer.toString();
	}
	
	public static JSONArray getJSONArray(ArrayList<GoogleTagManager> lst) throws JSONException {
		JSONArray array = new JSONArray();
		int size =lst.size();
		for (int i=0;i<size;i++) {
			GoogleTagManager ld=lst.get(i);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put(IntegrationConstants.PROJECT_ID, ld.getProjectId());
			jsonObj.put(IntegrationConstants.EMAIL_ID, ld.getEmailId());
			jsonObj.put(IntegrationConstants.TAG_ID, ld.getTagId());
			jsonObj.put(IntegrationConstants.ACCOUNT_ID, ld.getAccountId());
			jsonObj.put(IntegrationConstants.CONTAINER_ID , ld.getContainerId());
			jsonObj.put(IntegrationConstants.WORKSPACE_ID , ld.getWorkSpaceId());
			jsonObj.put(IntegrationConstants.ACCOUNT_NAME, ld.getAccountName());
			jsonObj.put(IntegrationConstants.CONTAINER_NAME, ld.getContainerName());
			jsonObj.put(IntegrationConstants.WORKSPACE_NAME, ld.getWorkSpaceName());
			jsonObj.put(ZABConstants.RESPONSE_STRING, ld.getResponseString());
			jsonObj.put(ZABConstants.STATUS_CODE, ld.getResponseCode());
			jsonObj.put(ZABConstants.SUCCESS, Boolean.TRUE);
			array.put(jsonObj);
		}
		return array;
	}
	
	
	

}
