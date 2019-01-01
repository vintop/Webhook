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

public class GoogleAdwordsResponse {
	
	private static final Logger LOGGER = Logger.getLogger(GoogleAdwordsResponse.class.getName());

	public static String jsonResponse(HttpServletRequest request,ArrayList<GoogleAdwords> lst) {			
		StringBuffer returnBuffer = new StringBuffer();
		try{
			JSONArray array = getJSONArray(lst);			
			JSONObject json = ZABResponse.updateMetaInfo(request, IntegrationConstants.API_MODULE_GOOGLEADWORDS, array);
			returnBuffer.append(json);
			json=null;
		}catch(Exception ex){
			
			LOGGER.log(Level.SEVERE,"Exception Occurred",ex);
			
		}
		return returnBuffer.toString();
	}
	
	public static JSONArray getJSONArray(ArrayList<GoogleAdwords> lst) throws JSONException {
		JSONArray array = new JSONArray();
		int size =lst.size();
		for (int i=0;i<size;i++) {
			GoogleAdwords ld=lst.get(i);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put(IntegrationConstants.PROJECT_ID, ld.getProjectId());
			jsonObj.put(IntegrationConstants.EMAIL_ID, ld.getEmailId());
			jsonObj.put(IntegrationConstants.CLIENT_ID, ld.getClientId());
			jsonObj.put(IntegrationConstants.CUSTOMER_NAME, ld.getCustomerName());
			jsonObj.put(IntegrationConstants.ISMCC, ld.getIsMCC());
			if(ld.getCampaignName() != null){
				jsonObj.put(IntegrationConstants.CAMPAIGN_NAME, ld.getCampaignName());
				jsonObj.put(IntegrationConstants.ADGROUP_ARRAY, ld.getAdGroupArray());
				jsonObj.put(IntegrationConstants.ADGROUP_NAME, ld.getAdGroupName());
				jsonObj.put(IntegrationConstants.CLICKS, ld.getClicks());
				jsonObj.put(IntegrationConstants.GOOGLECLICKS, ld.getGoogleClicks());
				jsonObj.put(IntegrationConstants.COSTS, ld.getCosts());
				jsonObj.put(IntegrationConstants.CURRENCY, ld.getCurrency());
				jsonObj.put(IntegrationConstants.IMPRESSIONS, ld.getImpressions());
				jsonObj.put(IntegrationConstants.CONVERSIONS, ld.getConversions());
				jsonObj.put(IntegrationConstants.GOOGLECONVERSIONS, ld.getGoogleConversions());
				jsonObj.put(IntegrationConstants.CTR, ld.getCTR());
				jsonObj.put(IntegrationConstants.CPC, ld.getCPC());
			}
			jsonObj.put(ZABConstants.RESPONSE_STRING, ld.getResponseString());
			jsonObj.put(ZABConstants.STATUS_CODE, ld.getResponseCode());
			jsonObj.put(ZABConstants.SUCCESS, ld.getSuccess());
			array.put(jsonObj);
		}
		return array;
	}
	
	
	

}
