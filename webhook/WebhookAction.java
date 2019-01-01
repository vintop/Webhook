//$Id$
package com.zoho.abtest.webhook;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONException;

import com.adventnet.iam.IAMUtil;
import com.adventnet.iam.xss.IAMEncoder;
import com.opensymphony.xwork2.ActionSupport;
import com.zoho.abtest.common.ZABAction;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.elastic.ElasticSearchIndexConstants;
import com.zoho.abtest.license.LicenseConstants;
import com.zoho.abtest.license.PortalLicenseMapping;
import com.zoho.abtest.project.Project;
import com.zoho.abtest.project.ProjectAction;
import com.zoho.abtest.project.ProjectConstants;
import com.zoho.abtest.user.ZABUserConstants;
import com.zoho.abtest.utility.ZABUtil;

public class WebhookAction extends ActionSupport implements ServletResponseAware, ServletRequestAware{

private static final Logger LOGGER = Logger.getLogger(WebhookAction.class.getName());
	
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
		LOGGER.log(Level.INFO, "Webhook Action Start");
		ArrayList<Webhook> webhooks = new ArrayList<Webhook>();
		try {			
			switch(ZABAction.getHTTPMethod(request)) {
			case POST:	
				HashMap<String,String> hs = ZABAction.getRequestParser(request).parseWebhook(request);
				if(hs.containsKey(ZABConstants.SUCCESS)&&!ZABUtil.parseBoolean(hs.get(ZABConstants.SUCCESS),ZABConstants.SUCCESS)){
					Webhook webhook = new Webhook();
					webhook.setSuccess(Boolean.FALSE);
					webhook.setResponseString(hs.get(ZABConstants.RESPONSE_STRING));
					webhooks.add(webhook);
				}else{
	
					webhooks.add(Webhook.createWebhook(hs));
				}
				break;
			case GET:
				if(linkname==null || linkname.isEmpty()) {	
					webhooks.addAll(Webhook.getWebhooks());
				} else {
					Webhook webhook = Webhook.getWebhookByLinkname(linkname);
					webhooks.add(webhook);
				}
				break;
			case DELETE:
				Webhook.deleteWebhook(linkname);
				break;
			case PUT:
				HashMap<String,String> hs1 = ZABAction.getRequestParser(request).parseWebhook(request);
				if(hs1.containsKey(ZABConstants.SUCCESS)&&!ZABUtil.parseBoolean(hs1.get(ZABConstants.SUCCESS),ZABConstants.SUCCESS)){
					Webhook webhook = new Webhook();
					webhook.setSuccess(Boolean.FALSE);
					webhook.setResponseString(hs1.get(ZABConstants.RESPONSE_STRING));
					webhooks.add(webhook);
				}else{					
					webhooks.add(Webhook.updateWebhook(hs1));
				}
				break;
			}
		}
          catch(JSONException ex){	
			ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getInvalidInputFormatException(WebhookConstants.API_MODULE_PLURAL));
			return null; 	
		}  catch(Exception ex){
			ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getExceptionString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE), WebhookConstants.API_MODULE_PLURAL));
			return null; 	
		}		
		LOGGER.log(Level.INFO, "Webhook Action Ends"); //No I18N
	    ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getWebhookResponse(request, webhooks));	
	    return null;
}
}