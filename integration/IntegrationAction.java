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

import com.opensymphony.xwork2.ActionSupport;
import com.zoho.abtest.common.ZABAction;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.utility.ZABUtil;

public class IntegrationAction extends ActionSupport implements ServletResponseAware, ServletRequestAware{
	
	/**
	 * 
	 */
	private static final Logger LOGGER = Logger.getLogger(IntegrationAction.class.getName());
	
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
	
	public String execute() throws IOException,JSONException {
		ArrayList<Integration> integrations = new ArrayList<Integration>();
		HashMap<String,String> hs = ZABAction.getRequestParser(request).parseIntegration(request);

		String project_id = request.getParameter(IntegrationConstants.PROJECT_ID);
		String project_link_name = request.getParameter(IntegrationConstants.PROJECT_LINK_NAME);
		String experiment_link_name = request.getParameter(IntegrationConstants.EXPERIMENT_LINK_NAME);
		String integration_id = request.getParameter(IntegrationConstants.INTEGRATION_ID);
		
		try {		
			switch(ZABAction.getHTTPMethod(request)) {			
			case POST:	
				if(hs.containsKey(ZABConstants.SUCCESS)&&!ZABUtil.parseBoolean(hs.get(ZABConstants.SUCCESS),ZABConstants.SUCCESS)){
					Integration integration = new Integration();
					integration.setSuccess(Boolean.FALSE);
					integration.setResponseString(hs.get(ZABConstants.RESPONSE_STRING));
					integrations.add(integration);
				}
				else{
					integrations.addAll(Integration.createIntegration(hs));
				}
				break;
			case GET:
				
				integrations.addAll(Integration.getIntegration(project_id,project_link_name,experiment_link_name,integration_id,request));
				break;
				
			case DELETE:

				integrations.addAll(Integration.deleteIntegration(integration_id,project_link_name,experiment_link_name));
				break;
				
			}
		}	
		catch(Exception ex){
			LOGGER.log(Level.SEVERE, "Exception occured: ",ex);
			ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getExceptionString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE), IntegrationConstants.API_MODULE));
			return null; 	
		}		
		ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getIntegrationResponse(request, integrations));		
	    return null;
	}

}
