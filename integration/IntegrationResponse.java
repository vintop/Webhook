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
import com.zoho.abtest.utility.ZABUtil;

public class IntegrationResponse {
	private static final Logger LOGGER = Logger.getLogger(IntegrationResponse.class.getName());
	public static String jsonResponse(HttpServletRequest request,ArrayList<Integration> lst) {			
		StringBuffer returnBuffer = new StringBuffer();
		try{
			JSONArray array = getJSONArray(lst);			
			JSONObject json = ZABResponse.updateMetaInfo(request, IntegrationConstants.API_MODULE, array);
			returnBuffer.append(json);
			json=null;
		}catch(Exception ex){
			LOGGER.log(Level.SEVERE, "Exception occured: ",ex);
			
		}
		return returnBuffer.toString();
	}
	
	public static JSONArray getJSONArray(ArrayList<Integration> lst) throws JSONException {
		JSONArray array = new JSONArray();
		int size =lst.size();
		for (int i=0;i<size;i++) {
			Integration ld=lst.get(i);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put(IntegrationConstants.INTEGRATION_ID, ld.getIntegrationId());
			jsonObj.put(IntegrationConstants.CUSTOM_DIMENSION,ld.getCustomDimension());
			jsonObj.put(IntegrationConstants.CUSTOM_TRACKER, ld.getCustomTracker());
			jsonObj.put(IntegrationConstants.IS_DIMENSION,ld.getIsDimension());
			jsonObj.put(IntegrationConstants.IS_AUTHENTICATED,ld.getIsAuthenticated());
			jsonObj.put(IntegrationConstants.EMAIL_ID,ld.getEmailId());
			jsonObj.put(IntegrationConstants.WORKSPACE,ld.getWorkSpace());
			jsonObj.put(IntegrationConstants.INTEGRATION_NAME, ld.getIntegrationName());
			jsonObj.put(IntegrationConstants.INTEGRATION_DESCRIPTION, ld.getIntegrationDescription());
			jsonObj.put(IntegrationConstants.ICON_URL, ld.getIconURL());
			jsonObj.put(IntegrationConstants.TERM_URL, ld.getTermURL());
			jsonObj.put(IntegrationConstants.PRIVACY_URL, ld.getPrivacyURL());
			jsonObj.put(IntegrationConstants.AUTHENTICATION_URL, ld.getAuthenticationURL());
			jsonObj.put(IntegrationConstants.PROJECT_INTEGRATION_ID, ld.getProjectIntegrationId());
			jsonObj.put(IntegrationConstants.PROJECT_ID, ld.getProjectId());
			jsonObj.put(IntegrationConstants.EXPERIMENT_ID, ld.getExperimentId());
			jsonObj.put(IntegrationConstants.EXPERIMENT_PROJECT_INTEGRATION_ID, ld.getExperimentProjectIntegrationId());
			jsonObj.put(ZABConstants.RESPONSE_STRING, ld.getResponseString());
			jsonObj.put(ZABConstants.STATUS_CODE, ld.getResponseCode());
			jsonObj.put(ZABConstants.SUCCESS, ld.getSuccess());
			array.put(jsonObj);
		}
		return array;
	}
}
