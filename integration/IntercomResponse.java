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

public class IntercomResponse {
	
	private static final Logger LOGGER = Logger.getLogger(IntercomResponse.class.getName());

	public static String jsonResponse(HttpServletRequest request,ArrayList<Intercom> lst) {			
		StringBuffer returnBuffer = new StringBuffer();
		try{
			JSONArray array = getJSONArray(lst);			
			JSONObject json = ZABResponse.updateMetaInfo(request, IntegrationConstants.API_MODULE_INTERCOM, array);
			returnBuffer.append(json);
			json=null;
		}catch(Exception ex){
			
			LOGGER.log(Level.SEVERE,"Exception Occurred",ex);
			
		}
		return returnBuffer.toString();
	}
	
	public static JSONArray getJSONArray(ArrayList<Intercom> lst) throws JSONException {
		JSONArray array = new JSONArray();
		int size =lst.size();
		for (int i=0;i<size;i++) {
			Intercom ld=lst.get(i);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put(IntegrationConstants.PROJECT_ID, ld.getProjectId());
			jsonObj.put(IntegrationConstants.USER_ID, ld.getUserId());		
			jsonObj.put(IntegrationConstants.EMAIL_ID, ld.getEmailId());
			jsonObj.put(ZABConstants.RESPONSE_STRING, ld.getResponseString());
			jsonObj.put(ZABConstants.STATUS_CODE, ld.getResponseCode());
			jsonObj.put(ZABConstants.SUCCESS, ld.getSuccess());
			array.put(jsonObj);
		}
		return array;
	}
	
	
	

}
