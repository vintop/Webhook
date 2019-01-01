//$Id$
package com.zoho.abtest.integration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONException;

import com.zoho.abtest.common.ZABAction;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.utility.ZABUtil;

public class IntercomAction implements ServletResponseAware, ServletRequestAware {
	

	private static final Logger LOGGER = Logger.getLogger(IntercomAction.class.getName());
	
	private static final long serialVersionUID = 1L;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private String linkname;
	
	public String getLinkname() {
		return linkname;
	}

	public void setLinkname(String linkname) {
		this.linkname = linkname;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		request = arg0;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		response = arg0;
	}

	public String execute() throws IOException, JSONException {
		LOGGER.log(Level.INFO, "Intercom Action Start"); //No I18Nbb
		HashMap<String,String> hs = ZABAction.getRequestParser(request).parseIntercom(request);

		String projectId = request.getParameter(IntegrationConstants.PROJECT_ID);

		
		ArrayList<Intercom> intercom = new ArrayList<Intercom>();
		try {			
			switch(ZABAction.getHTTPMethod(request)) {
			case POST:	
				
				intercom.add(Intercom.create(hs));
				break;
				
			case GET:

				if(projectId != null){
					Long proId = Long.parseLong(projectId);
					intercom.add(Intercom.getIntercom(proId));
				}				
				break;
				
			case DELETE:
				intercom.add(Intercom.delete(projectId));
				break;
			
			}
		}  catch(Exception ex){
			ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getExceptionString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE), IntegrationConstants.API_MODULE));
			return null; 	
		}		
		LOGGER.log(Level.INFO, "Intercom Action Ends"); //No I18N
		ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getIntercomResponse(request, intercom));	
	    return null;
	}

}
