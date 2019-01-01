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

public class GoogleAdwordsAction implements ServletResponseAware, ServletRequestAware {
	

	private static final Logger LOGGER = Logger.getLogger(GoogleAdwordsAction.class.getName());
	
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
		LOGGER.log(Level.INFO, "Google Adwords Action Start"); //No I18Nbb
		HashMap<String,String> hs = ZABAction.getRequestParser(request).parseGoogleAdwords(request);

		String projectId = request.getParameter(IntegrationConstants.PROJECT_ID);
		String start_date = request.getParameter(IntegrationConstants.START_DATE);
		String mode = request.getParameter(IntegrationConstants.MODE);
		String email_id = request.getParameter(IntegrationConstants.EMAIL_ID);
		String client_id = request.getParameter(IntegrationConstants.CLIENT_ID);
		
		ArrayList<GoogleAdwords> googleadwords = new ArrayList<GoogleAdwords>();
		try {			
			switch(ZABAction.getHTTPMethod(request)) {
			case POST:	
				
				mode = (String)hs.get(IntegrationConstants.MODE);
				if(mode != null && mode.equals("creategadwords")){
					googleadwords.add(GoogleAdwords.createGoogleAdwords(hs));
				}else{
					googleadwords.addAll(GoogleAdwords.getGoogleAdwordsReport(hs,request));
				}
				
				
				break;
				
			case GET:

				if(mode != null && mode.equals("customerinfo")){
					googleadwords.addAll(GoogleAdwords.getCustomerInfo(email_id));
				}else if(mode != null && mode.equals("accountinfo")){
					googleadwords.addAll(GoogleAdwords.getAccountInfo(email_id,client_id));
				}else if(mode != null && mode.equals("addgclidwithdate")){
					googleadwords.add(GoogleAdwords.addGClidJobWithDate(start_date));
				}else if(mode != null && mode.equals("getgadwordsdetails")){
					googleadwords.addAll(GoogleAdwords.getGoogleAdwords(projectId));
				}
				
				
//				if(start_date != null ){
//					googleadwords.add(GoogleAdwords.addGClidJobWithDate(start_date));
//				}else{
//					googleadwords.addAll(GoogleAdwords.getGoogleAdwords(projectId));
//				}
				
				break;
				
			case DELETE:
				googleadwords.add(GoogleAdwords.deleteGoogleAdwords(projectId));
				break;
			
			}
		}  catch(Exception ex){
			ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getExceptionString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE), IntegrationConstants.API_MODULE));
			return null; 	
		}		
		LOGGER.log(Level.INFO, "Google Adwords  Action Ends"); //No I18N
		ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getGoogleAdwordsResponse(request, googleadwords));	
	    return null;
	}

}
