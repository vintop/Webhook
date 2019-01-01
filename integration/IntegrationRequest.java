//$Id$
package com.zoho.abtest.integration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;

import com.zoho.abtest.common.ZABAction;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.common.ZABRequest;

public class IntegrationRequest extends ZABRequest{

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
			

	}

}