//$Id$
package com.zoho.abtest.trigger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zoho.abtest.common.ZABAction;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.common.ZABRequest;
import com.zoho.abtest.trigger.TriggerConstants;


public class TriggerRequest extends ZABRequest {

	@Override
	public void updateFromRequest(HashMap<String, String> map,
			HttpServletRequest request) {
		Long triggerid = (Long)request.getAttribute(ZABConstants.TRIGGERID);
		if(triggerid!=null) {			
			map.put(ZABConstants.TRIGGERID, (String.valueOf(triggerid)));
		}
	}

	@Override
	public void specificValidation(HashMap<String, String> map,
			HttpServletRequest request) throws IOException, JSONException {
			
			String httpMethod = ZABAction.getHTTPMethod(request).toString();
			
			if(httpMethod.equalsIgnoreCase("POST")){
					ArrayList<String> fields = new ArrayList<String>();
					if(!map.containsKey(TriggerConstants.WEBHOOK_LINK_NAME) || map.get(TriggerConstants.WEBHOOK_LINK_NAME).isEmpty()){
						fields.add(TriggerConstants.WEBHOOK_LINK_NAME);
					}
					
					if(!fields.isEmpty()) {
						ZABRequest.updateError(map, ZABAction.getAppropriateMessage(ZABConstants.ErrorMessages.MANDATORY_FIELD_MISSING.getErrorString(),fields));
					}
					
				}
			
			if(httpMethod.equalsIgnoreCase("PUT")){
				ArrayList<String> fields = new ArrayList<String>();
				if(!map.containsKey(ZABConstants.TRIGGERID)) {
						fields.add(ZABConstants.TRIGGERID);
				}
				
				if(!map.containsKey(TriggerConstants.WEBHOOK_LINK_NAME) || map.get(TriggerConstants.WEBHOOK_LINK_NAME).isEmpty()){
					fields.add(TriggerConstants.WEBHOOK_LINK_NAME);
				}
				
				
				
				if(!fields.isEmpty()) {
					ZABRequest.updateError(map, ZABAction.getAppropriateMessage(ZABConstants.ErrorMessages.MANDATORY_FIELD_MISSING.getErrorString(),fields));
				}
			}
			
		
	}

	
}
