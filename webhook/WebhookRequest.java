//$Id$
package com.zoho.abtest.webhook;

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
import com.zoho.abtest.webhook.WebhookConstants;


public class WebhookRequest extends ZABRequest {

	@Override
	public void updateFromRequest(HashMap<String, String> map,
			HttpServletRequest request) {
		String linkName = (String)request.getAttribute(ZABConstants.LINKNAME);
		if(linkName!=null) {			
			map.put(ZABConstants.LINKNAME, linkName);
		}
	}

	@Override
	public void specificValidation(HashMap<String, String> map,
			HttpServletRequest request) throws IOException, JSONException {
			
			String httpMethod = ZABAction.getHTTPMethod(request).toString();
			
			if(httpMethod.equalsIgnoreCase("POST")){
					ArrayList<String> fields = new ArrayList<String>();
					if(!map.containsKey(WebhookConstants.WEBHOOK_NAME) || map.get(WebhookConstants.WEBHOOK_NAME).isEmpty()){
						fields.add(WebhookConstants.WEBHOOK_NAME);
					}
					
					if(!fields.isEmpty()) {
						ZABRequest.updateError(map, ZABAction.getAppropriateMessage(ZABConstants.ErrorMessages.MANDATORY_FIELD_MISSING.getErrorString(),fields));
					}
					
				}
			
			if(httpMethod.equalsIgnoreCase("PUT")){
				ArrayList<String> fields = new ArrayList<String>();
				if(!map.containsKey(ZABConstants.LINKNAME)) {
						fields.add(ZABConstants.LINKNAME);
				}
				
				if(!map.containsKey(WebhookConstants.WEBHOOK_NAME) || map.get(WebhookConstants.WEBHOOK_NAME).isEmpty()){
					fields.add(WebhookConstants.WEBHOOK_NAME);
				}
				
				
				
				if(!fields.isEmpty()) {
					ZABRequest.updateError(map, ZABAction.getAppropriateMessage(ZABConstants.ErrorMessages.MANDATORY_FIELD_MISSING.getErrorString(),fields));
				}
			}
			
		
	}

	
}
