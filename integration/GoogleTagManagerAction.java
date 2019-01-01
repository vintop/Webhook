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

public class GoogleTagManagerAction implements ServletResponseAware, ServletRequestAware {
	

	private static final Logger LOGGER = Logger.getLogger(GoogleTagManagerAction.class.getName());
	
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
		LOGGER.log(Level.INFO, "Google Tag Manager Action Start"); //No I18Nbb
		HashMap<String,String> hs = ZABAction.getRequestParser(request).parseGoogleTagManager(request);

		String projectId = request.getParameter(IntegrationConstants.PROJECT_ID);
		String emailId = request.getParameter(IntegrationConstants.EMAIL_ID);
		String accId = request.getParameter(IntegrationConstants.ACCOUNT_ID);
		String containerId = request.getParameter(IntegrationConstants.CONTAINER_ID);
		
		ArrayList<GoogleTagManager> googletagmanager = new ArrayList<GoogleTagManager>();
		try {			
			switch(ZABAction.getHTTPMethod(request)) {
			case POST:	
			
				googletagmanager.add(GoogleTagManager.createGoogleTagManager(hs));
				break;
				
			case GET:
				
				if(emailId != null && containerId == null){
					googletagmanager.addAll(GoogleTagManager.getGoogleTagManagerForAccounts(emailId));
				}else if(accId != null && containerId == null){
					Long accountId = Long.parseLong(accId);
					googletagmanager.addAll(GoogleTagManager.getGoogleTagManagerForContainer(accountId));
				}else if(containerId != null){
					Long accountId = Long.parseLong(accId);
					Long containerid = Long.parseLong(containerId);
					googletagmanager.addAll(GoogleTagManager.getGoogleTagManagerForWorkSpace(accountId,containerid,emailId));
				}else if(projectId != null){
					Long proId = Long.parseLong(projectId);
					googletagmanager.add(GoogleTagManager.getGoogleTagManagerForProject(proId));
				}
				break;
				
			case DELETE:
				googletagmanager.add(GoogleTagManager.deleteGoogleTagManager(projectId));
				break;
			
			}
		}  catch(Exception ex){
			ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getExceptionString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE), IntegrationConstants.API_MODULE));
			return null; 	
		}		
		LOGGER.log(Level.INFO, "Google Tag Manager  Action Ends"); //No I18N
		ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getGoogleTagManagerResponse(request, googletagmanager));	
	    return null;
	}

}
