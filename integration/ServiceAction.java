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
import com.zoho.abtest.audience.Audience;
import com.zoho.abtest.common.ZABAction;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.portal.Portal;
import com.zoho.abtest.portal.PortalConstants;
import com.zoho.abtest.utility.ZABUtil;

public class ServiceAction extends ActionSupport implements ServletResponseAware, ServletRequestAware{
	
	/**
	 * 
	 */
	private static final Logger LOGGER = Logger.getLogger(ServiceAction.class.getName());
	
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
	
	public String execute() throws Exception {
		ArrayList<Portal> portals = new ArrayList<Portal>();
		ZABUtil.setCurrentRequest(request);
		ZABUtil.setDBCallCountMap(null);
		Long reqstartTime = ZABUtil.getCurrentTimeInMilliSeconds();
		ZABUtil.getCurrentRequest().setAttribute("RequestStartTime", reqstartTime);
		String inputString = null;
		if(ZABAction.getHTTPMethod(request).equals(ZABAction.HTTPMethod.POST) || ZABAction.getHTTPMethod(request).equals(ZABAction.HTTPMethod.PUT)) {
			inputString = ZABAction.getInputString(request);
		}
		ZABUtil.setCurrentInputString(inputString);
		//We don't need to set current user
		ZABUtil.setCurrentUser(null);
		HashMap<String,String> hs = ZABAction.getRequestParser(request).parsePortal(request);

		String portalName = request.getParameter(PortalConstants.PORTALNAME);
		String projectName = request.getParameter(PortalConstants.PROJECTNAME);
		
		try {		

			switch(ZABAction.getHTTPMethod(request)) {			
			case POST:	
				Portal portal = new Portal();
				if(hs.containsKey(ZABConstants.SUCCESS)&&!ZABUtil.parseBoolean(hs.get(ZABConstants.SUCCESS),ZABConstants.SUCCESS)){
					portal.setSuccess(Boolean.FALSE);
					portal.setResponseString(hs.get(ZABConstants.RESPONSE_STRING));
					portals.add(portal);
				}else{
					String portalname = hs.get(PortalConstants.PORTALNAME);
					String projectname = hs.get(PortalConstants.PROJECTNAME);
					portals = Portal.createServicePortal(portalname,projectname,request);
				}
				break;
			case GET:
				ZABUtil.setCurrentRequest(request);
				if(portalName == null ){
					portals.addAll(Portal.getPortals());
				}else{
					portals.addAll(Portal.getProjects(portalName,null));
				}
				break;
				
				
			}
		}	
		catch(Exception ex){
			LOGGER.log(Level.SEVERE, "Exception occured: ",ex);
			ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getExceptionString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE), PortalConstants.API_MODULE));
			return null; 	
		}		
		ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getPortalResponse(request, portals));
	    return null;
	}

}
